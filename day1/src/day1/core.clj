(ns day1.core
    (:require [clojure.tools.trace :refer [trace]]))

(defn parse-input
  [text]
  ;(clojure.string/split-lines text)
  ;(clojure.string/split text #"\s")
  ;(Integer. (str \1))
  ;(re-matches re s)
  )

(def input (parse-input (slurp "input.txt")))

(defn part1
  []
  nil)

(defn part2
  []
  nil)

(do
   (println (part1))
   (println (part2)))
