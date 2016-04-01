(ns ascii-never-dies.player)

(enable-console-print!)

;; player vars
(def pos (atom []))
(def cur-health (atom 0))
(def max-health (atom 100))

;; Sets the players position
(defn set-player-pos [[x y]]
  (reset! pos [x y]))

;; initializes player position to center board
(defn init-player-pos [height width]
  (let [x (int (/ width 2))
        y (int (/ height 2))]
    (set-player-pos [x y])))

;; player movement methods
;; width and height refer to game board, as board is a 1D vector
;; TODO: we WILL need player logic to check for walls / objects before moving
(defn move-player-left []
  (swap! pos update 0 dec))

(defn move-player-right []
  (swap! pos update 0 inc))

(defn move-player-up [width]
  (swap! pos update 1 dec))

(defn move-player-down [width]
  (swap! pos update 1 inc))
