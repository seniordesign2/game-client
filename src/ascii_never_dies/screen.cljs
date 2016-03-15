(ns ascii-never-dies.screen)

(defrecord Screen [width height cells])
(defrecord Cell [glyph color])

;; Generates a vector of empty Cells for use in a new Screen
(defn init-cells [width height]
  (loop [n 0
         cells []]
    (if (>= n (* width height))
      cells
      (recur (inc n)
             (conj cells (new Cell "." nil))))))

;; Generates a new Screen
(defn new-screen [width height]
  (new Screen width height (init-cells width height)))

;; Reverts every Cell in a given Screen to the supplied glyph, or <space>
(defn clear-screen
  ([screen]
   (clear-screen " " screen))
  ([glyph screen]
   (assoc screen :cells (apply vector (map #(assoc % :glyph glyph) (:cells screen))))))

;; Replaces the glyph of the Cell at the given xy position
(defn insert [x y glyph screen]
  (let [n (+ x (* y (:width screen)))]
    (assoc-in screen [:cells n :glyph] glyph)))

;; Sequentially replaces the glyphs of all cells
;; from the starting point to the end of the given string
(defn insert-string [x y glyphs screen]
  (loop [n 0
         s screen]
    (if (>= n (count glyphs))
      s
      (let [xx (+ x n)
            glyph (nth glyphs n)]
        (recur (inc n)
               (insert xx y glyph s))))))

;; Generates a string of a given Screen
(defn stringify [screen]
  (let [width (:width screen)
        height (:height screen)]
    (loop [n 0
           s ""]
      (if (>= n height)
        s
        (let [row (* n width)]
          (recur (inc n)
                 (str s
                      (apply str (map :glyph (subvec (:cells screen) row (+ row width))))
                      "\n")))))))
