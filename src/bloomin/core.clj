(ns bloomin.core
  (:require [bloomin.hash :as bhash]))


(defprotocol BloomFilterStore
  "Protocol defining the behavior of the underlying storage of a bloom filter"
  (bfsInsert [this indices] "Set the given index to 1")
  (bfsExists? [this indices] "Check if a value exists given a set of indices")
  (bfsClear [this] "Clean out the bloom filter and re initialize it"))


(defn optimal-size
  "Given a number of entries and a % error, give the optimal number of bits"
  [^long entries ^double errorp]
  (int (Math/ceil
         (* -1
            (/
             (* entries (Math/log errorp))
             (Math/pow (Math/log 2) 2))))))


(defn optimal-hashes
  "Given a number of bits, and a number of entries calcualte the # of hash functions.
  The formula for this is ceil( (bits / entries) * log(2))."
  [^long entries ^long bits]
  (int (Math/ceil
         (*
          (/ bits entries)
          (Math/log 2)))))


(defn new-bloom-filter
  "Create a new bloom filter. The bfs-func is the function used to create an instance
  of something conforming to the BloomFilterStore protocol."
  [bfs-func ^double err entries]
  (let [size (optimal-size entries err)]
    {:size size
     :hashes (optimal-hashes entries size)
     :error err
     :entries entries
     :bfs ((resolve bfs-func) entries err)}))


(defn bf-insert
  "Insert some data into a bloom filter"
  [bf data]
  (.bfsInsert (:bfs bf) (bhash/bloom-hash data (:size bf) (:hashes bf))))


(defn bf-exists?
  "Check if some data exists in a bloom filter"
  [bf data]
  (.bfsExists? (:bfs bf) (bhash/bloom-hash data (:size bf) (:hashes bf))))


(defn bf-clear
  "Clear a bloom filter"
  [bf]
  (.bfsClear (:bfs bf)))
