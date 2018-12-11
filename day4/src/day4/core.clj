(ns day4.core
    (:require [clojure.tools.trace :refer [trace]]))

(defn parse-line
  [line]
  (if-let [guard (second (re-matches #".+Guard #(\d+).+" line))]
    {:guard (Long/parseLong guard)}
    (Long/parseLong (second (re-matches #"\[\d+-\d+-\d+ \d+:(\d+)\].+" line)))))

(defn drop-shift
  [xs]
  (drop-while #(nil? (:guard %)) xs))

(defn get-shift
  [xs]
  (take-while #(nil? (:guard %)) xs))

(defn reduce-shifts
  [data]
  (loop [[x & xs] data
         acc {}]
    (if (nil? x) acc
        (recur (drop-shift xs) (update acc (:guard x) (fnil #(concat % (get-shift xs)) []))))))

(defn parse-input
  [text]
  (->> (clojure.string/split-lines text)
       sort
       (map parse-line)
       reduce-shifts
       (map #(vector (first %) (partition 2 (second %))))
       (into {})))

(def input (parse-input (slurp "input.txt")))

(defn guard-with-most-minutes
  [data]
  (->> data
       (map (fn [[guard naps]]
              [guard
               (reduce (fn [acc [start end]] (+ acc (- end start))) 0 naps)]))
       (sort-by second >)
       ffirst))

(defn minutes-overlapped
  [data]
  (->> data
       (mapcat (fn [[start end]] (range start end)))
       (reduce (fn [acc n] (update acc n (fnil inc 0))) {})
       (sort-by second >)
       first))

(defn part1
  []
  (let [guard (guard-with-most-minutes input)
        minutes (first (minutes-overlapped (get input guard)))]
    (* guard minutes)))

(defn part2
  []
  (let [[guard [minute _]]
        (->> input
            (map (fn [[guard naps]] [guard (minutes-overlapped naps)]))
            (sort-by (comp second second))
            last)]
    (* guard minute)))

(vector
   (part1)
   (part2))
