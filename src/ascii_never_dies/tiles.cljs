(ns ascii-never-dies.tiles
  (:require [clojure.string]))

;; empty hash-map for game board
(def board {})

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

;; TODO: initialize game board with empty spaces (periods)
;;       find way to pass as a string for index.cljs.hl

(defn initBoard [v] 
  (loop [i 0]
    (when (< i 10)
    (loop [j 0 vec v]
      (when (< j 10)
      (recur (inc j) (conj vec "."))))
    (recur (inc i))))
    v
)

(def strTest (clojure.string/join (initBoard [])))
