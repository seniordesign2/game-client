(ns ascii-never-dies.world
  (:require [ascii-never-dies.screen :as screen]
            [ascii-never-dies.tiles :as tiles]
            [ascii-never-dies.player :as player])
  (:require-macros [ascii-never-dies.filereader :as files]))

(enable-console-print!)

(defrecord Room [screen n e w s])

(def width 25)
(def height 15)
(def empty-screen (screen/new-screen width height))
(def maps (files/load-maps))
(def room-idx
  "The index of which room the player is in."
  (atom nil))
(def rooms
  "A vector of every room in the game world."
  (atom []))

(defn get-map
  "Returns a map as a screen. n is what order it was loaded in alphabetically."
  [n]
  (screen/replace-screen empty-screen (nth maps (dec n))))

(defn get-random-map
  "Decides a random map to load."
  []
  (get-map (inc (rand-int (count maps)))))

(defn get-current-room
  "Returns the room the player is in."
  []
  (nth @rooms @room-idx))

(defn init
  "Loads a random map for the starting room."
  []
  (let [new-room (new Room (get-random-map) nil nil nil nil)]
    (reset! room-idx 0)
    (swap! rooms conj new-room)))

(defn enter-room
  "Tries to enter a room - if it doesn't exist, creates it first."
  [to-dir from-dir]
  (let [to [@room-idx to-dir]]
    (if (get-in @rooms to)
      (reset! room-idx (get-in @rooms to))
      (let [from-room (assoc (new Room (get-random-map) nil nil nil nil) from-dir @room-idx)]
        (reset! room-idx (count @rooms))
        (swap! rooms assoc-in to (count @rooms))
        (swap! rooms conj from-room)))))

(defn to-screen
  "Gathers all the elements of the game world (player, enemies, etc)
  and creates a new screen out of the base board."
  [& hide-player]
  (let [s (:screen (get-current-room))]
    (if-not hide-player
      (screen/insert (player/get-pos) "@" s)
      s)))

(defn print-board
  "Creates a string out of the current board."
  []
  (screen/stringify (to-screen)))

(defn collision?
  "Decides if a given coordinate in the map will result in a collision."
  [[x y]]
  (if (or (< x 0) (< y 0) (>= x width) (>= y height))
    true
    (-> (screen/get-tile [x y] (to-screen))
        :attributes
        :is-solid?)))
