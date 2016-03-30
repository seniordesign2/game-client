(ns ascii-never-dies.screen
  (:require [clojure.string :as string]
            [clojure.java.io :as io]))

(defrecord Screen [width height cells])
(defrecord Cell [glyph color])

(enable-console-print!)

(defn init-cells
  "Generates a nested vector of new Cells."
  [width height]
  (vec (for [row (range height)]
         (->> (new Cell "." nil)
              (repeat width)
              (vec)))))

(defn new-screen
  "Generates a new Screen."
  [width height]
  (new Screen width height (init-cells width height)))

(defn clear-screen
  "Reverts every Cell in a given Screen to the supplied glyph, or <space>"
  ([screen]
   (clear-screen " " screen))
  ([glyph screen]
   (assoc screen :cells (vec (for [row (range (:height screen))]
                               (mapv #(assoc % :glyph glyph) (nth (:cells screen) row)))))))

(defn insert
  "Replaces the glyph of the Cell at the given xy position."
  [[x y] glyph screen]
  (assoc-in screen [:cells y x :glyph] glyph))

(defn stringify
  "Generates a string for the given Screen."
  [screen]
  (let [width (:width screen)
        height (:height screen)]
    (loop [row 0
           s ""]
      (if (>= row height)
        s
        (recur (inc row)
               (str s
                    (apply str (map :glyph (nth (:cells screen) row)))
                    "\n"))))))

; Maps will be linked together by simply appending the correct int to each filename for each map
; Example: map1.txt, map2.txt, map23.txt, map14.txt, so on...

(def map-file "/assets/map1.txt")

; Reads in a map#.txt file and updates the game map
(defn replace-screen
  (with-open [rdr (io/reader map-file)]
    (doseq [line (line-seq rdr)]
            (println line))))
