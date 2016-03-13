(ns ascii-never-dies.tiles
  (:require [ascii-never-dies.screen :as s]))

(enable-console-print!)

;; empty hash-map for game board
(def board (atom []))
(def height 5)
(def width 15)

;; initializes game board with empty spaces (periods)
(defn init-board [] 
  (dotimes [n (* width height)]
    (swap! board conj ".")))

;; TODO: printBoard should create a gameboard string seperated by newlines based on width and height
(def print-board
  (do
    (init-board)
    (loop [n 0
           b-str ""]
      (if (>= n height)
        b-str
        (let [row (* n width)]
          (recur (inc n)
                 (str b-str (apply str (subvec (deref board) row (+ row width))) "\n")))))))

;; passes a string made from the initBoard func
(def str-test (do
                (init-board)
                (apply str (deref board))))

; testing .screen namespace
(def scr (s/new-screen width height))
;(defn test-print [] (:cells scr))
;(defn test-print [] (apply vector (:cells (s/clear-screen scr "#"))))
(defn test-print [] (s/stringify (s/clear-screen scr "#")))
