(ns ascii-never-dies.world
  (:require [ascii-never-dies.screen :as screen]
            [ascii-never-dies.tiles :as tiles]
            [ascii-never-dies.player :as player]
            [ascii-never-dies.enemies :as enemies])
  (:require-macros [ascii-never-dies.filereader :as files]))

(enable-console-print!)

(def Room {:scr nil :x nil :y nil :enemies nil})

(def width 25)
(def height 15)
(def empty-screen (screen/new-screen width height))
(def maps (files/load-maps))
(def room-idx
  "The index of which room the player is in."
  (atom {:x nil :y nil}))
(def rooms
  "A set of every room in the game world."
  (atom #{}))

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

(defn set-room-idx
  "Resets the current room's world coords."
  [idx]
  (reset! room-idx idx))

(defn set-rooms
  "Resets the current vector of rooms."
  [rs]
  (reset! rooms (set rs)))

(defn init
  "Loads a random map for the starting room."
  []
  (let [new-room (assoc Room
                        :scr (get-random-map)
                        :x 0 :y 0)]
    (reset! room-idx {:x 0 :y 0})
    (reset! rooms #{new-room})))

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

(defn door-check
  "Returns whether the specified spot is a door or in front of a door."
  [room tile]
  (let [{:keys [x y]} tile]
    (loop [xx -1
           yy -1]
      (if (= "door" (:name (screen/get-tile [(+ x xx) (+ y yy)] (:scr room))))
        true
        (if (< xx 1)
          (recur (inc xx) yy)
          (if (< yy 1)
            (recur -1 (inc yy))
            false))))))

(defn empty-tiles
  "Returns all empty spots in the given room.
  Empty defined as 'is-solid' being false, not a door, and not in front of a door."
  [room]
  (set (filter #(not (or (:is-solid %)
                         (door-check room %))) (-> room
                                                   (:scr)
                                                   (:cells)
                                                   (flatten)))))

(defn generate-enemies
  "Creates a random list of enemies."
  [room]
  (let [empties (atom (empty-tiles room))]
    (set (for [n (range (rand-int 5))
               :while (seq @empties)
               :let [spot (first (shuffle @empties))]]
           (do
             (swap! empties disj spot)
             (enemies/get-random-enemy tiles/enemy spot))))))

(defn create-room
  "Creates a new room."
  [new-coords]
  (let [x (:x new-coords)
        y (:y new-coords)
        room (assoc Room
                    :scr (get-random-map)
                    :x x :y y)
        room (assoc room :enemies (generate-enemies room))]
    (swap! rooms conj room)))

(defn enter-room
  "Tries to enter a room - if it doesn't exist, creates it first."
  [dir]
  (let [new-coords (get-new-coords dir)]
    (reset! room-idx new-coords)
    (if-not (get-room new-coords)
      (create-room new-coords))))

(defn add-player
  "Adds the player to the screen."
  [scr hide-player]
  (if-not hide-player
    (screen/insert @player/player scr)
    scr))

(defn add-enemies
  "Adds all enemies to the screen."
  [scr enemies]
  (loop [s scr
         e enemies]
    (if (empty? e)
      s
      (recur (screen/insert (first e) s)
             (rest e)))))

(defn to-screen
  "Gathers all the elements of the game world (player, enemies, etc)
  and creates a new screen out of the base board."
  [& hide-player]
  (let [{:keys [scr enemies]} (get-current-room)]
    (-> scr
        (add-player hide-player)
        (add-enemies enemies))))

(defn print-board
  "Creates a string out of the current board."
  []
  (screen/stringify-html (to-screen)))

(defn collision?
  "Decides if a given coordinate in the map will result in a collision."
  [[x y]]
  (if (or (< x 0) (< y 0) (>= x width) (>= y height))
    true
    (:is-solid (screen/get-tile [x y] (to-screen)))))

(defn damage-enemy
  "Damages the enemy in the current room with the specified coords."
  [[x y] damage]
  (let [room (get-current-room)
        enemies (set (:enemies room))
        target (some #(if (and (= x (:x %))
                               (= y (:y %)))
                        %)
                     enemies)]
    (swap! rooms disj room)
    (swap! rooms conj
           (if (<= (- (:cur-health target) damage) 0)
             (assoc room :enemies (disj enemies target))
             (let [enemies (disj enemies target)
                   target (update target :cur-health - damage)
                   enemies (conj enemies target)]
               (player/damage-health 5)
               (assoc room :enemies enemies))))))
