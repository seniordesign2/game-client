(ns ascii-never-dies.traps)

(def Trap {:effect nil})

;; List of all traps
(def traps [(assoc Trap :effect [:damage 5])
            (assoc Trap :effect [:teleport 2])])

(defn get-random-trap
  "Returns a random trap."
  [tile]
  (merge tile (rand-nth traps)))

(defn get-effect
  "Returns the effect of the trap, formatted as a keyword and value."
  [trap]
  (let [e (:effect trap)]
    [(keyword (first e)) (second e)]))
