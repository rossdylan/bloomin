(ns bloomin.sbs
  (:require [bloomin.core :as bloom])
  (:import (com.zaxxer.sparsebits SparseBitSet)))


; Define a record that implements the BloomFilterStore Protocol using a sparse
; bit set.
(defrecord SBSBloomFilterStore [^SparseBitSet sbs]
  bloom/BloomFilterStore
    (bfsInsert [this indices]
      (map (fn [i] (.set sbs i true)) indices))
    (bfsExists? [this indices]
      (every? true? (map (fn [i] (.get sbs i)) indices)))
    (bfsClear [this] (.clear sbs)))


(defn sbs-bfs
  "Create a new Sparse Bit Set bloom filter store. This is a convience function
  to make it easy to generate a new SBSBloomFilterStore"
  [^long entries ^double errorp]
  (SBSBloomFilterStore. (SparseBitSet. (bloom/optimal-size entries errorp))))
