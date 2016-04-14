(ns ascii-never-dies.tiles
  (:require [ascii-never-dies.traps :as traps]))

(enable-console-print!)

(defrecord Tile [attributes])
(defrecord Attributes [name glyph color is-solid?])

(def error (new Attributes "ERROR" "ERROR" nil false))
(def floor (new Attributes "floor" "." nil false))
(def wall  (new Attributes "wall" "#" nil true))
(def trap  (new Attributes "trap" "^" nil false))
(def door  (new Attributes "door" "-" nil false))

(defn new-tile-from-map
  "Returns a new tile based on the symbol passed to it."
  [symbol]
  (case symbol
    "." (new Tile floor)
    "#" (new Tile wall)
    "^" (traps/get-random-trap trap)
    "+" (new Tile door)
    error))
