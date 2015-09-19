# bloomin

A Clojure library that creates a common abstraction for bloom filters.
Provides a single implementation that uses com.zaxxer.sparsebits to provide a
sparse bit set.

## BloomFilterStorage Abstraction

The core of bloomin is the abstraction provided by BloomFilterStorage. It is a
simple protocol that defines the 3 operations needed for a bloom filter. These
are, `bfsInsert` `bfsExists` and `bfsClear`. An implementation of BloomFilterStorage
should also provide a function that takes [^long entries ^double errorp]. That function
is used with the `new-bloom-filter` function in bloomin.core. At that point the
functions in bloomin.core can be used to work with the bloom filter.


## Using bloomin.sbs

bloomin provides a single bloom filter implementation by default. It uses a
sparse bit set to efficently store the actual bloom filter. An example of using it
follows:

```clojure
(ns bloomin.example
    (:require '[[bloomin.core :as bloomin]
                [bloomin.sbs :as sbs]]))

(def bf (bloomin/new-bloom-filter sbs/sbs-bfs 0.99 1000)
(def names ["mike", "eric", "sarah", "kimberly", "max", "alex", "emma", "lucy"]


(defn -main
    ""
    []
    (do
        (map (partial bloomin/bf-insert bf) names)
        (map (comp println (partial bloomin/bf-exists? bf)) names)
        (bloomin/bf-clear bf)
        (map (comp println (partial bloomin/bf-exists? bf)) names)))
```
