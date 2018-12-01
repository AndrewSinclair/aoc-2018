(ns day1.core)

(defn parse-input
  [text]
  (->>
    (clojure.string/split-lines text)
    (map read-string)))

(def input (parse-input (slurp "input.txt")))

(defn part1
  []
  (->> input
       (apply +)))

(defn part2
  []
  (->> input
       cycle
       (reductions +)
       (reduce (fn [acc x]
                 (if (contains? acc x)
                   (reduced x)
                   (conj acc x)))
               #{})))

(part1)
(part2)
