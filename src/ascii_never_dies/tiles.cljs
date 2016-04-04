(ns ascii-never-dies.tiles
  (:require [ascii-never-dies.screen :as s]
            [ascii-never-dies.player :as player]))

(enable-console-print!)

;; empty hash-map for game board
(def width 25)
(def height 15)
(def board (s/new-screen width height))

;; creates a gameboard string seperated by newlines based on width and height
(defn print-board []
  (let [[x y] @player/pos]
    (s/stringify (s/insert @player/pos "@" (s/replace-map board)))))

; testing .screen namespace
(def scr (s/new-screen width height))
(defn test-print []
  (s/stringify (s/insert [10 2] "t" (s/clear-screen "#" scr))))

; testing file reading in screen.cljs
#_(defn test-readfile []
  (println (s/replace-map)))
