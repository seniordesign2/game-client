(ns ascii-never-dies.tiles)

(enable-console-print!)

(defrecord Tile [glyph color is-solid?])

(def floor (new Tile "." nil false))
(def wall (new Tile "#" nil true))
