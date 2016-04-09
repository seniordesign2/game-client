(ns ascii-never-dies.world
  (:require [ascii-never-dies.screen :as screen]
            [ascii-never-dies.tiles :as tiles]
            [ascii-never-dies.player :as player])
  (:require-macros [ascii-never-dies.filereader :as files]))

(enable-console-print!)

(def width 25)
(def height 15)
(def board (screen/new-screen width height))
(def maps (files/load-maps))
(def cur-room (atom nil))
(def rooms (atom []))

(defn get-map
  "Returns a map. n is what order it was loaded in alphabetically."
  [n]
  (nth maps (dec n)))

(defn get-random-map
  "Decides a random map to load."
  []
  (get-map (inc (rand-int (count maps)))))

(defn init
  "Loads a random map for the starting room."
  []
  (reset! cur-room (get-random-map))
  (swap! rooms conj @cur-room))

(defn to-screen
  "Gathers all the elements of the game world (player, enemies, etc)
  and creates a new screen out of the base board."
  []
  (let [s (screen/replace-screen board @cur-room)]
    (screen/insert (player/get-pos) "@" s)))

(defn print-board
  "Creates a string out of the current board."
  []
  (screen/stringify (to-screen)))

(defn collision?
  "Decides if a given coordinate in the map will result in a collision."
  [[x y]]
  (if (or (< x 0) (< y 0) (>= x width) (>= y height))
    true
    (:is-solid? (screen/get-tile [x y] (to-screen)))))
