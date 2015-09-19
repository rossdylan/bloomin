(ns bloomin.core-test
  (:require [clojure.test :refer :all]
            [bloomin.core :refer :all]
            [bloomin.sbs :refer :all]))

(def names ["mike", "eric", "sarah", "kimberly", "max", "alex", "emma", "lucy"])

(defn- insert-errorp
  "Test insertion/existance at a given errorp"
  [^double errorp]
  (let [bf (new-bloom-filter sbs-bfs errorp 1000)]
    (do
      (println bf)
      (dorun (map (partial bf-insert bf) names))
      (is (every? (partial = true) (map (partial bf-exists? bf) names))))))

(deftest insertion-01
  (testing "Testing Insertion at errop=0.01"
    (insert-errorp 0.01)))

(deftest insertion-001
  (testing "Testing Insertion at errop=0.001"
    (insert-errorp 0.001)))

(deftest insertion-0001
  (testing "Testing Insertion at errop=0.001"
    (insert-errorp 0.0001)))
