(ns ascii-never-dies.traps)

(def Trap {:attr nil :effect nil})

;; List of all traps
(def traps [(assoc Trap :effect [:damage 5])
            (assoc Trap :effect [:teleport 2])])

(defn get-random-trap
  "Returns a random trap."
  [attr]
  (assoc (rand-nth traps) :attr attr))
