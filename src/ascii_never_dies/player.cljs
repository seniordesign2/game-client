(ns ascii-never-dies.player)

(enable-console-print!)

;; player vars
(def pos (atom 0))

;; initializes player position to center board
(defn init-player-pos [height width]
  (swap! pos + (* width (int(/ height 2))) (int(/ width 2)))
  (println "start: " pos))

;; player movement methods
;; width and height refer to game board, as board is a 1D vector
;; TODO: we WILL need player logic to check for walls / objects before moving
(defn move-player-left []
  (swap! pos - 1)
  (println "left! " (deref pos)))

(defn move-player-right []
  (swap! pos + 1))

(defn move-player-up [width]
  (swap! pos - width))

(defn move-player-down [width]
  (swap! pos + width))
