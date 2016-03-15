(ns ascii-never-dies.tiles
  (:require [ascii-never-dies.screen :as s]
            [ascii-never-dies.player :as player]))

(enable-console-print!)

;; empty hash-map for game board
(def height 5)
(def width 15)
(def board (s/new-screen width height))

;; convert values from 1D board vec to 2D coord representation and vice versa
;; TODO: to be used later for movement methods and game logic and general map building
;; TODO: change function names

(defn two-to-one-d [x y]
  (+ x (* y width)))

(defn one-to-two-d [pos]
  (let [x (int (mod pos width))
        y (int (/ pos width))]
    [x y]))
  
;; initializes game board with empty spaces (periods)
(defn init-board [] 
  (dotimes [n (* width height)]
    (swap! board conj ".")))

;; creates a gameboard string seperated by newlines based on width and height
(defn print-board []
  (let [[x y] (one-to-two-d (deref player/pos))]
    (s/stringify (s/insert x y "@" board))))

; testing .screen namespace
(def scr (s/new-screen width height))
(defn test-print [] (s/stringify (s/insert-string 5 3 "testing" (s/clear-screen "#" scr))))
