(ns ascii-never-dies.filereader
  (:require [clojure.java.io :as io]))

; Reads in a map#.txt file and updates the game map
(defmacro load-map [map-file]
  (slurp map-file)
  #_(with-open [rdr (io/reader map-file)]
    (doseq [line (line-seq rdr)]
            line)))
