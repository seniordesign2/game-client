(ns ascii-never-dies.tiles
  (:require [clojure.string]))

(enable-console-print!)

;; empty hash-map for game board
(def board (atom []))
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

;; TODO: create a function to split string into new lines based on board width and height
(def strTest (do (initBoard) (clojure.string/join (deref board))))
