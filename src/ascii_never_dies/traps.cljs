(ns ascii-never-dies.traps)

(def Trap {:attr nil :effect nil})

;; List of all traps
(def traps [(assoc Trap :effect [:damage 5])
            (assoc Trap :effect [:teleport 2])])

(defn get-random-trap
  "Returns a random trap."
  [attr]
  (assoc (rand-nth traps) :attr attr))

(defn get-effect
  "Returns the effect of the trap, formatted as a keyword and value."
  [trap]
  (let [e (:effect trap)]
    [(keyword (first e)) (second e)]))
