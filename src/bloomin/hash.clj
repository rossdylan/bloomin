(ns bloomin.hash
  (:import (net.jpountz.xxhash XXHashFactory)
           (java.nio ByteBuffer)
           clojure.lang.Murmur3))

; setup some constants for xxhash
(def ^:private xxseed 0x88fd9d5c334adc)

(def ^:private xxhasher (.hash64 (XXHashFactory/fastestInstance)))

(defn- xx-hash
  "Run the xxhash function on the given data"
  [data]
  (.hash xxhasher (ByteBuffer/wrap (.getBytes data)) xxseed))

(defn- murmur-hash
  "Run the murmur3 hash function on the givend data"
  [data]
  (-> data
      (Murmur3/hashUnencodedChars)
      (Math/abs)))

(defn- single-hash
  "Single hash function. A single hash is a combination of our 2 hash functions.
  The algorithm is as follows: hash1(data) + (i * hash2(data) + l. i is the index
  of this hash, l is the length of the length of the bloom filter."
  [i l d]
  (mod (+ (murmur-hash d) (* i (xx-hash d))) l))

(defn- create-hashes
  "Create a sequence of hash functions to use in a bloom filter."
  [len k]
  (map
    (fn [i] (partial single-hash i len))
    (range k)))

(defn bloom-hash
  "Use the technique found in http://www.eecs.harvard.edu/~michaelm/postscripts/rsa2008.pdf to combine xxhash and murmur3 into as many hash functions as we need for a bloom filter"
  [data len k]
  (map (fn [f] (f data)) (create-hashes len k)))
