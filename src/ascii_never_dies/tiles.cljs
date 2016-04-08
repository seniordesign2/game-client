(ns ascii-never-dies.tiles
  (:require [ascii-never-dies.screen :as s]
            [ascii-never-dies.player :as player])
  (:require-macros [ascii-never-dies.filereader :refer [load-maps]]))

(enable-console-print!)

(def width 25)
(def height 15)
(def board (s/new-screen width height))
(def maps (load-maps))

(defn get-map
  "Returns a map. n is what order it was loaded in alphabetically."
  [n]
  (nth maps (dec n)))

(defn print-board
  "Creates a string out of the current board."
  []
  (s/stringify (s/insert (player/get-pos) "@" (s/replace-screen board (get-map 1)))))

; testing .screen namespace
(def scr (s/new-screen width height))
(defn test-print []
  (s/stringify (s/insert [10 2] "t" (s/clear-screen "#" scr))))
