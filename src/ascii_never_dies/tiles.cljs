(ns ascii-never-dies.tiles
  (:require [ascii-never-dies.traps :as traps]))

(enable-console-print!)

(def Tile {:attr nil})
(def Attr {:name nil :glyph nil :color nil :is-solid? nil})

(def error (assoc Attr
                  :name "ERROR"
                  :glyph "ERROR"
                  :color nil
                  :is-solid? false))
(def floor (assoc Attr
                  :name "floor"
                  :glyph "."
                  :color nil
                  :is-solid? false))
(def wall  (assoc Attr
                  :name "wall"
                  :glyph "#"
                  :color nil
                  :is-solid? true))
(def trap  (assoc Attr
                  :name "trap"
                  :glyph "^"
                  :color nil
                  :is-solid? false))
(def door  (assoc Attr
                  :name "door"
                  :glyph "-"
                  :color nil
                  :is-solid? false))

(defn new-tile-from-map
  "Returns a new tile based on the symbol passed to it."
  [symbol]
  (case symbol
    "." (assoc Tile :attr floor)
    "#" (assoc Tile :attr wall)
    "^" (traps/get-random-trap trap)
    "+" (assoc Tile :attr door)
    (assoc Tile :attr error)))
