(ns ascii-never-dies.tiles)

(enable-console-print!)

(defrecord Tile [glyph color is-solid?])

(def error (new Tile "ERROR" nil false))
(def floor (new Tile "." nil false))
(def wall (new Tile "#" nil true))
(def trap (new Tile "^" nil false))
(def door (new Tile "-" nil false))

(defn new-tile-from-map
  "Returns a new tile based on the symbol passed to it."
  [symbol]
  (case symbol
    "." floor
    "#" wall
    "^" trap
    "+" door
    error))
