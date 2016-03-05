(ns ascii-never-dies.tiles)

(enable-console-print!)

;; empty hash-map for game board
(def board (atom []))
(def boardStr (atom ""))
(def height 10)
(def width 20)

;; vectors to be updated with game info
(def vRowA (vector "#" "#" "#" "#" "#" "#" "#" "#" "#" "#" "#"))
(def vRowB (vector "#" "." "." "." "." "." "." "." "B" "." "#"))
(def vRowC (vector "#" "." "." "." "." "@" "." "." "." "." "#"))
(def vRowD (vector "#" "." "B" "." "." "." "." "." "." "." "#"))
(def vRowE (vector "#" "#" "#" "#" "#" "#" "#" "#" "#" "#" "#"))

;; string sequences of each vector to be printed by index.cljs.hl
(def strRowA (apply str vRowA))
(def strRowB (apply str vRowB))
(def strRowC (apply str vRowC))
(def strRowD (apply str vRowD))
(def strRowE (apply str vRowE))

;; initializes game board with empty spaces (periods)
(defn initBoard [] 
  (dotimes [n (* width height)]
    (swap! board conj ".")))

;; TODO: printBoard should create a gameboard string seperated by newlines based on width and height
(def printBoard
  (do
    (initBoard)
    (reset! boardStr "") ;;erase old board to avoid appending new string to old game board
    (dotimes [n height]
      (let [row (* n width)]
        (swap! boardStr str (apply str (subvec (deref board) row (+ row width))) "\n")))
    (deref boardStr)))

;; passes a string made from the initBoard func
(def strTest (do
                (initBoard)
                (apply str (deref board))))
