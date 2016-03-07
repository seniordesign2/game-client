(ns ascii-never-dies.tiles)

(enable-console-print!)

;; empty hash-map for game board
(def board (atom []))
(def height 10)
(def width 20)

;; initializes game board with empty spaces (periods)
(defn initBoard [] 
  (dotimes [n (* width height)]
    (swap! board conj ".")))

;; TODO: printBoard should create a gameboard string seperated by newlines based on width and height
(def printBoard
  (do
    (initBoard)
    (loop [n 0
           bStr ""]
      (if (>= n height)
        bStr
        (let [row (* n width)]
          (recur (inc n)
                 (str bStr (apply str (subvec (deref board) row (+ row width))) "\n")))))))

;; passes a string made from the initBoard func
(def strTest (do
                (initBoard)
                (apply str (deref board))))
