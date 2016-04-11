(ns ascii-never-dies.player)

(enable-console-print!)

(def player
  "The player"
  (atom {:x nil :y nil
         :cur-health nil :max-health nil}))

(defn set-pos
  "Sets the player's position."
  [[x y]]
  (swap! player assoc :x x)
  (swap! player assoc :y y))

(defn get-pos
  "Returns a vector of the player's position."
  []
  [(:x @player) (:y @player)])

;;
;; TODO: Make this not hardcoded
;;
(defn enter-room
  "Moves the player to the appropriate position for enter a room
  from a given direction."
  [from-dir]
  (case from-dir
    :n (set-pos [12 1])
    :e (set-pos [23 7])
    :w (set-pos [1 7])
    :s (set-pos [12 13])))

(defn init
  "Initializes player attributes."
  [width height health]
  (swap! player assoc :cur-health health)
  (swap! player assoc :max-health health)
  (let [x (int (/ width 2))
        y (int (/ height 2))]
    (set-pos [x y])))

(defn is-dead?
  "Checks if the player is currently dead."
  []
  (<= (:cur-health @player) 0))

(defn damage-health
  "Decreases the player's health by a given value,
  potentially negative."
  [damage]
  (let [p @player]
    (if (>= (- (:cur-health p) damage) (:max-health p))
      (swap! player assoc :cur-health (:max-health p))
      (swap! player update :cur-health #(- % damage)))))

;; player movement methods
;; width and height refer to game board, as board is a 1D vector
;; TODO: we WILL need player logic to check for walls / objects before moving
(defn move-player-left []
  (swap! player update :x dec))

(defn move-player-right []
  (swap! player update :x inc))

(defn move-player-up []
  (swap! player update :y dec))

(defn move-player-down []
  (swap! player update :y inc))

(defn print-health
  "Prints the player's health."
  []
  (str (:cur-health @player) "/" (:max-health @player)))
