(ns ascii-never-dies.screen
  (:require [clojure.string :as string]
            [ascii-never-dies.tiles :as tiles]))

(enable-console-print!)

(defrecord Screen [width height cells])
(defrecord Cell [glyph color])

(defn init-cells
  "Generates a nested vector of new Cells."
  [width height]
  (vec (for [row (range height)]
         (->> tiles/floor
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

(defn get-tile
  "Returns a tile at a specific position."
  [[x y] screen]
  (get-in screen [:cells y x]))

(defn stringify
  "Generates a string for the given Screen."
  [screen]
  (let [height (:height screen)]
    (loop [row 0
           s ""]
      (if (>= row height)
        s
        (recur (inc row)
               (str s
                    (apply str (map :glyph (nth (:cells screen) row)))
                    "\n"))))))

(defn replace-screen
  "Replaces the given screen with a string representing the new screen."
  [screen replacement]
  (let [s (string/split replacement #"\n" (:height screen))]
    (assoc screen :cells (vec (for [r (range (:height screen))
                                    :let [row (nth (:cells screen) r)
                                          new-row (nth s r)]]
                                (mapv #(assoc %1 :glyph %2) row new-row))))))
