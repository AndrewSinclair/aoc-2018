(ns day5.core
  (:require [clojure.string :as s]))

(def input  (slurp "input.txt"))

(defn same?
  [a b]
  (and (not= a b) (= (s/upper-case a) (s/upper-case b))))

(defn one-reaction
  [text]
  (loop [[a b & xs] text
         acc []]
    (if (nil? b)
      (conj acc a)
      (if (same? a b)
        (recur xs acc)
        (recur (cons b xs) (conj acc a))))))

(defn fully-react
  [text]
  (->> text
      (iterate one-reaction)
      (map count)
      (partition 2 1)
      (filter #(apply = %))
      ffirst))

(defn remove-units
  [unit text]
  (->> text
       (remove #{(first (s/lower-case unit)) (first (s/upper-case unit))})))

(defn part1
  []
  (fully-react input))

(defn part2
  []
  (->> "abcdefghijklmnopqrstuvwxyz"
      (map #(remove-units % input))
      (map fully-react)
      sort))

(vector
  (part1)
  (part2))
