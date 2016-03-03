(ns ascii-never-dies.tiles
  (:require [clojure.string]))

(enable-console-print!)

;; empty hash-map for game board
(def board (atom []))
(def boardStr (atom ""))
(def height 100)
(def width 100)

;; vectors to be updated with game info
(def vRowA (vector "#" "#" "#" "#" "#" "#" "#" "#" "#" "#" "#"))
(def vRowB (vector "#" "." "." "." "." "." "." "." "B" "." "#"))
(def vRowC (vector "#" "." "." "." "." "@" "." "." "." "." "#"))
(def vRowD (vector "#" "." "B" "." "." "." "." "." "." "." "#"))
(def vRowE (vector "#" "#" "#" "#" "#" "#" "#" "#" "#" "#" "#"))

;; string sequences of each vector to be printed by index.cljs.hl
(def strRowA (clojure.string/join vRowA))
(def strRowB (clojure.string/join vRowB))
(def strRowC (clojure.string/join vRowC))
(def strRowD (clojure.string/join vRowD))
(def strRowE (clojure.string/join vRowE))

;; initializes game board with empty spaces (periods)
(defn initBoard [] 
  (dotimes [n (* width height)]
    (swap! board conj ".")
   )
)

;; TODO: printBoard should create a gameboard string seperated by newlines based on width and height
;(def printBoard
;  (do
;    (reset! boardStr "") ;;erase old board to avoid appending new string to old game board
;    (dotimes [n height]
;      (let [row (* n width)]
;        (swap! boardStr conj (clojure.string/join (subvec (deref board) row (+ row width)))))
;      )
;    (deref boardStr)
;  )
;)

;; passes a string made from the initBoard func
(def strTest (do (initBoard) (clojure.string/join (deref board))))
