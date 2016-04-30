(ns ascii-never-dies.enemies)

(def Enemy {:glyph nil :type nil :color nil :cur-health nil :max-health nil :x nil :y nil})

;; List of all enemies
(def enemies [(assoc Enemy
                     :glyph "r"
                     :type "rat"
                     :color "red"
                     :max-health 2)
              (assoc Enemy
                     :glyph "g"
                     :type "goblin"
                     :color "blue"
                     :max-health 5)

              (assoc Enemy
                     :glyph "s"
                     :type "spoopy spider"
                     :color "green"
                     :max-health 10)

              (assoc Enemy 
                     :glyph "d"
                     :type "demon"
                     :color "pink"
                     :max-health 66.6)

              (assoc Enemy
                     :glyph "p"
                     :type "poltergeist"
                     :color "white"
                     :max-health 100)

              (assoc Enemy
                     :glyph "m"
                     :type "money bag"
                     :color "brown"
                     :max-health 777)

              (assoc Enemy
                     :glyph "n"
                     :type "Ninja Brian"
                     :color "black"
                     :max-health 69)

              (assoc Enemy
                     :glyph "b"
                     :type "Mr. Bones"
                     :color "chucknorris"
                     :max-health 1)])

(defn get-random-enemy
  "Returns a random enemy type."
  [tile coords]
  (let [enemy (merge tile (rand-nth enemies))
        {:keys [x y]} coords]
    (assoc enemy
           :cur-health (:max-health enemy)
           :x x
           :y y)))
