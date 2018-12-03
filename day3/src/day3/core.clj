(ns day3.core)

(defn parse-parts
  [[id _ xy wh]]
  (let [id' (subs id 1)
        [_ x y] (re-matches #"(\d+),(\d+):" xy)
        [_ w h] (re-matches #"(\d+)x(\d+)" wh)]
    {:id (read-string id')
     :x  (read-string x)
     :y  (read-string y)
     :w  (read-string w)
     :h  (read-string h)}))

(defn parse-input
  [text]
  (->> (clojure.string/split-lines text)
       (map #(clojure.string/split % #"\s"))
       (map parse-parts)))

(def input (parse-input (slurp "input.txt")))

(defn mk-rectangle
  [{:keys [id x y w h]}]
  {:id id
   :top-left {:x x :y y}
   :bottom-right {:x (dec (+ x w)) :y (dec (+ y h))}})

(defn overlap?
  [a b]
  (let [a1-x (get-in a [:top-left :x])
        a1-y (get-in a [:top-left :y])
        a2-x (get-in a [:bottom-right :x])
        a2-y (get-in a [:bottom-right :y])
        b1-x (get-in b [:top-left :x])
        b1-y (get-in b [:top-left :y])
        b2-x (get-in b [:bottom-right :x])
        b2-y (get-in b [:bottom-right :y])]
    (not (or (> a1-x b2-x) (< a2-x b1-x)
             (> a1-y b2-y) (< a2-y b1-y)
             (= (:id a) (:id b))))))

(defn remove-overlaps
  [xs]
  (->> xs
      (keep (fn [x]
              (if-not (some (partial overlap? x) xs)
                (:id x))))))

(defn part1
  []
  (->> input
       (reduce (fn [acc {:keys [id x y w h]}]
                 (concat
                  acc
                  (for [i (range w)
                        j (range h)]
                    (str (+ i x) "," (+ j y)))))
               [])
       frequencies
       (filter (fn [[_ n]] (>= n 2)))
       count))

(defn part2
  []
  (->> input
       (map mk-rectangle)
       remove-overlaps
       first))

(vector
  (part1)
  (part2))
