(ns ascii-never-dies.enemies)

(def Enemy {:glyph nil :type nil :cur-health nil :max-health nil :x nil :y nil})

;; List of all enemies
(def enemies [(assoc Enemy
                     :glyph "r"
                     :type "rat"
                     :max-health 2)
              (assoc Enemy
                     :glyph "g"
                     :type "goblin"
                     :max-health 5)])

(defn get-random-enemy
  "Returns a random enemy type."
  [tile]
  (let [enemy (merge tile (rand-nth enemies))]
    (assoc enemy :cur-health (:max-health enemy))))
