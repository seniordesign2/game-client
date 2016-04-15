(ns ascii-never-dies.world
  (:require [ascii-never-dies.screen :as screen]
            [ascii-never-dies.tiles :as tiles]
            [ascii-never-dies.player :as player])
  (:require-macros [ascii-never-dies.filereader :as files]))

(enable-console-print!)

(defrecord Room [screen x y])

(def width 25)
(def height 15)
(def empty-screen (screen/new-screen width height))
(def maps (files/load-maps))
(def room-idx
  "The index of which room the player is in."
  (atom {:x nil :y nil}))
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

(defn get-room
  "Returns the room at the specified coords, or nil if it doesn't exist."
  [{x :x y :y}]
  (some #(if (and (= x (:x %))
                  (= y (:y %)))
           %)
        @rooms))

(defn get-current-room
  "Returns the room the player is in."
  []
  (get-room @room-idx))

(defn init
  "Loads a random map for the starting room."
  []
  (let [new-room (new Room (get-random-map) 0 0)]
    (reset! room-idx {:x 0 :y 0})
    (swap! rooms conj new-room)))

(defn get-new-coords
  "Based on the direction passed to it,
  returns the coords of the room the player is moving into."
  [dir]
  (let [x (:x @room-idx)
        y (:y @room-idx)]
    (case dir
      :n {:x x :y (dec y)}
      :e {:x (inc x) :y y}
      :w {:x (dec x) :y y}
      :s {:x x :y (inc y)})))

(defn enter-room
  "Tries to enter a room - if it doesn't exist, creates it first."
  [dir]
  (let [new-coords (get-new-coords dir)]
    (reset! room-idx new-coords)
    (if-not (get-room new-coords)
      (let [room (-> (new Room (get-random-map) nil nil)
                     (assoc :x (:x new-coords))
                     (assoc :y (:y new-coords)))]
        (swap! rooms conj room)))))

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
