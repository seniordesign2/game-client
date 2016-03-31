(ns ascii-never-dies.filereader
  (:require [clojure.java.io :as io]))

; Reads in a map#.txt file and updates the game map
(defmacro load-map [map-file]
  (slurp (io/file map-file)))
