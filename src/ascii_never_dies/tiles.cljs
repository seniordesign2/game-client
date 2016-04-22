(ns ascii-never-dies.tiles
  (:require [ascii-never-dies.traps :as traps]
            [ascii-never-dies.enemies :as enemies]))

(enable-console-print!)

(def Tile {:name nil :glyph nil :color nil :is-solid nil})

(def error (assoc Tile
                  :name "ERROR"
                  :glyph "ERROR"
                  :color nil
                  :is-solid false))
(def floor (assoc Tile
                  :name "floor"
                  :glyph "."
                  :color nil
                  :is-solid false))
(def wall  (assoc Tile
                  :name "wall"
                  :glyph "#"
                  :color nil
                  :is-solid true))
(def trap  (assoc Tile
                  :name "trap"
                  :glyph "^"
                  :color nil
                  :is-solid false))
(def door  (assoc Tile
                  :name "door"
                  :glyph "-"
                  :color nil
                  :is-solid false))
(def enemy (assoc Tile
                  :name "enemy"
                  :is-solid true))

(defn new-tile-from-map
  "Returns a new tile based on the symbol passed to it."
  [symbol]
  (case symbol
    "." floor
    "#" wall
    "^" (traps/get-random-trap trap)
    "+" door
    "e" (enemies/get-random-enemy enemy)
    error))
