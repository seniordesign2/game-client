(ns ascii-never-dies.tiles
  (:require [ascii-never-dies.screen :as s]
            [ascii-never-dies.player :as player]))

(enable-console-print!)

;; empty hash-map for game board
(def board (atom []))
(def height 5)
(def width 15)

;; initializes game board with empty spaces (periods)
(defn init-board [] 
  (dotimes [n (* width height)]
    (swap! board conj ".")))

;; creates a gameboard string seperated by newlines based on width and height
(defn print-board []
  (do
    (init-board)
    ;; TODO: this will need removed when a main game loop initializes the player beforehand
    (let [temp-board (assoc (deref board) (deref player/pos) "@")]
      (loop [n 0
             b-str ""]
        (if (>= n height)
          b-str
          (let [row (* n width)]
            (recur (inc n)
                   (str b-str (apply str (subvec temp-board row (+ row width))) "\n"))))))))

;; passes a string made from the initBoard func
(def str-test (do
                (init-board)
                (apply str (deref board))))

; testing .screen namespace
(def scr (s/new-screen width height))
(defn test-print [] (s/stringify (s/insert-string 5 3 "testing" (s/clear-screen "#" scr))))

;; convert values from 1D board vec to 2D coord representation and vice versa
;; TODO: to be used later for movement methods and game logic and general map building
;; TODO: change function names

(defn two-to-one-d [x y]
  (+ x (* y width)))

(defn one-to-two-d [pos]
  (let [x (mod pos width )
        y (/ pos width)]
    [x y]))
