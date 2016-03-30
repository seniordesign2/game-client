(ns ascii-never-dies.filereader
  (:require [clojure.java.io :as]))

(def map-file "/assets/map1.txt")

; Reads in a map#.txt file and updates the game map
(defmacro load-map
  (with-open [rdr (io/reader map-file)]
    (doseq [line (line-seq rdr)]
            (println line))))
