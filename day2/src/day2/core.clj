(ns day2.core)

(defn parse-input
  [text]
  (clojure.string/split-lines text))

(def input (parse-input (slurp "input.txt")))

(defn checksum
  [n xs]
  (->> xs
      (map frequencies)
      (filter #(some (fn [[_ b]] (= n b)) %))
      count))

(defn get-diff
  [a b]
  (let [[left right sames] (clojure.data/diff (vec a) (vec b))]
    (when (and (->> left (filter identity) count (= 1))
               (->> right (filter identity) count (= 1)))
          sames)))

(defn combos
  [f xs]
  (for [i (range (count xs))
        j (range (count xs))
        :when (< i j)]
    (f (nth xs i) (nth xs j))))

(defn part1
  []
  (* (checksum 2 input)
     (checksum 3 input)))

(defn part2
  []
  (->> input
       (combos get-diff)
       (filter identity)
       first
       (apply str)))

(do
   (println (part1))
   (println (part2)))
