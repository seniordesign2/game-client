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
                     :max-health 5)

              (assoc Enemy
                     :glyph "s"
                     :type "spoopy spider"
                     :max-health 10)

              (assoc Enemy 
                     :glyph "d"
                     :type "demon"
                     :max-health 666)

              (assoc Enemy
                     :glyph "p"
                     :type "poltergeist"
                     :max-health 1000)

              (assoc Enemy
                     :glyph "m"
                     :type "money bag"
                     :max-health 7777)

              (assoc Enemy
                     :glyph "n"
                     :type "Ninja Brian"
                     :max-health 69)

              (assoc Enemy
                     :glyph "b"
                     :type "Mr. Bones"
                     :max-health 1)])

(defn get-random-enemy
  "Returns a random enemy type."
  [tile]
  (let [enemy (merge tile (rand-nth enemies))]
    (assoc enemy :cur-health (:max-health enemy))))
