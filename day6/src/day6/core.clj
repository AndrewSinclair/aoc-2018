(ns day6.core)

(defn parse-input
  [text]
  (->> (clojure.string/split-lines text)
       (map #(read-string (str "(" % ")")))))

(def input (parse-input (slurp "input.txt")))

(defn manhattan-distance
  [[x1 y1] [x2 y2]]
  (+ (Math/abs (- x1 x2)) (Math/abs (- y1 y2))))

(defn bounding-box
  [input]
  [(->> input
        (map first)
        (apply max))
   (->> input
        (map second)
        (apply max))])

(defn find-closest-points
  [input [i j]]
  (->> input
       (map #(vector % (manhattan-distance [i j] %)))
       (sort-by second)
       (partition-by second)
       first
       (map first)))

(defn map-closest-points
  [input]
  (->> (for [i (range -1 (inc (first (bounding-box input))))
             j (range -1 (inc (second (bounding-box input))))]
         [i j])
       (map #(vector % (find-closest-points input %)))
       (into {})))

(defn convex-hull
  [input]
  (let [bbox (bounding-box input)]
    (->> (for [i (range -1 (inc (first bbox)))
               j (range -1 (inc (second bbox)))
               :when (or (= i -1)
                         (= i (first bbox))
                         (= j -1)
                         (= j (second bbox)))]
           (find-closest-points input [i j]))
         (apply concat)
         distinct
         set)))

(defn part1
  []
  (let [hull (convex-hull input)]
    (->> input
         map-closest-points
         (remove #(> (count (second %)) 1))
         (map (fn [[a b]] [(first b) a]))
         (reduce (fn [acc [k v]] (update acc k (fnil conj []) v)) {})
         (map (fn [[pt ns]] [pt (count ns)]))
         (sort-by second >)
         (remove #(hull (first %))))))

(defn part2
  []
  (->>
   (for [i (range (first (bounding-box input)))
         j (range (second (bounding-box input)))]
     [i j])
   (map (partial total-distance-to-all input))
   (filter #(< % 10000))
   count))

(defn total-distance-to-all
  [input [i j]]
  (->> input
       (map #(vector % (manhattan-distance [i j] %)))
       (map second)
       (reduce +)))

(vector
  (part1)
  (part2))

