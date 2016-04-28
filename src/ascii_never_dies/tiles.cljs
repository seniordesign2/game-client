(ns ascii-never-dies.tiles
  (:require [ascii-never-dies.traps :as traps]
            [ascii-never-dies.enemies :as enemies]))

(enable-console-print!)

(def Tile {:name nil :glyph nil :color nil :is-solid nil :x nil :y nil})

(def error (assoc Tile
                  :name "ERROR"
                  :glyph "ERROR"
                  :color "#FF0000"
                  :is-solid false))
(def floor (assoc Tile
                  :name "floor"
                  :glyph "."
                  :color "#303030"
                  :is-solid false))
(def wall  (assoc Tile
                  :name "wall"
                  :glyph "#"
                  :color "#808080"
                  :is-solid true))
(def trap  (assoc Tile
                  :name "trap"
                  :glyph "^"
                  :color "#888800"
                  :is-solid false))
(def door  (assoc Tile
                  :name "door"
                  :glyph "-"
                  :color "#880000"
                  :is-solid false))
(def enemy (assoc Tile
                  :name "enemy"
                  :is-solid true))
(def player (assoc Tile
                   :name "player"
                   :glyph "@"
                   :color nil
                   :is-solid true))

(defn new-tile-from-map
  "Returns a new tile based on the symbol passed to it."
  [symbol x y]
  (assoc (case symbol
           "." floor
           "#" wall
           "^" (traps/get-random-trap trap)
           "+" door
           error)
         :x x
         :y y))
