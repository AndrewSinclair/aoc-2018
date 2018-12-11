(ns day7.core
    (:require [clojure.tools.trace :refer [trace]]))

(defn parse-line
  [line]
  (rest (re-matches #"Step (\w) must be finished before step (\w) can begin\." line)))

(defn successors
  [v edges]
  (->> edges
       (filter (fn [[start _]] (= start v)))
       (map second)
       (into (sorted-set))))

(defn mk-digraph
  [edges]
  (let [vertices (apply sorted-set (apply concat edges))
        nodes (->> vertices (map (fn [v] [v (successors v edges)])) (into {}))]
    {:vertices vertices
     :nodes nodes}))

(defn parse-input
  [text]
  (->> (clojure.string/split-lines text)
       (map parse-line)
       mk-digraph))

(def test-input
  [["A" "B"]
   ["A" "D"]
   ["B" "E"]
   ["C" "A"]
   ["C" "F"]
   ["D" "E"]
   ["F" "E"]])

(mk-digraph test-input)

(defn remove-node
  [nodes vertex]
  (->> nodes
       (map (fn [[node edges]] [node (remove-edge edges vertex)]))
       (remove (fn [[node _]] (= node vertex)))
       (into {})))

(defn remove-edge
  [edges vertex]
  (->> edges
       (remove (partial = vertex))
       (into (sorted-set))))

(defn remove-vertex
  [{:keys [vertices nodes] :as graph} vertex]
  (let [new-vertices (apply sorted-set (remove (partial = vertex) vertices))
        new-nodes    (remove-node nodes vertex)]
    {:vertices new-vertices
     :nodes new-nodes}))

(defn graph-empty?
  [graph]
  (zero? (count (:vertices graph))))

(defn in-degrees
  [graph vertex]
  (->> graph
       :nodes
       (map second)
       (filter #(contains? % vertex))
       count))

(def input (parse-input (slurp "input.txt")))

(defn sort-by-indegree
  [graph]
  (let [vertices (:vertices graph)]
    (->> vertices
         (map #(vector % (in-degrees graph %)))
         (sort (fn [[v1 degrees1] [v2 degrees2]] (if (= degrees1 degrees2) (compare v1 v2) (compare degrees1 degrees2))))
         (map first))))

(def test-graph (mk-digraph test-input))

(defn topsort
  [input]
  (loop [g input
         next-v (first (sort-by-indegree g))
         acc []]
    (if (nil? next-v)
      acc
      (let [next-g (remove-vertex g next-v)]
        (recur next-g (first (sort-by-indegree next-g)) (conj acc next-v))))))

(defn work-time
  [vertex]
  (-> vertex
      first
      int
      (- (int \A))))

(defn available-worker
  [workers]
  (->> workers
       (map-indexed (fn [i n] [i n]))
       (filter (fn [[i n]] (nil? n)))
       ffirst))

(defn update-workers
  [worker time workers]
  (let [dec-workers (map #(when-not (or (nil? %) (zero? %)) (dec %)) workers)]
    (if (nil? worker)
      dec-workers
      (assoc (vec dec-workers) worker time))))

(loop [g input
       workers [nil nil nil nil nil]
       next-v (first (sort-by-indegree g))
       acc 0]
  (if (and (graph-empty? g) (= (vec workers) [nil nil nil nil nil]))
    acc
    (if (graph-empty? g)
      (recur g (update-workers nil nil workers) nil (inc acc))
      (let [next-g (remove-vertex g next-v)
            time   (+ 60 (work-time next-v))
            worker (available-worker workers)
            next-workers (update-workers worker time workers)]
        (recur next-g next-workers (first (sort-by-indegree next-g)) (inc acc))))))


(defn part1
  []
  (->> input
       topsort
       (apply str)))

(defn part2
  []
  nil)

(do
  (println (part1))
  (println (part2)))
