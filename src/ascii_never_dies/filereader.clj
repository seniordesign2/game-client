(ns ascii-never-dies.filereader
  (:require [clojure.java.io :as io]))

(defmacro load-maps
  "Retrieves every map located in PROJECT_ROOT/assets/maps/"
  []
  (vec (for [map-file (sort-by #(io/.getName %)
                               (rest (file-seq (io/file "assets/maps/"))))]
    (slurp map-file))))
