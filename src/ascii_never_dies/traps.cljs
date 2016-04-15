(ns ascii-never-dies.traps)

(defrecord Trap [attributes effect])

;; List of all traps
(def traps [(new Trap nil [:damage 5])
            (new Trap nil [:teleport 2])])

(defn get-random-trap
  "Returns a random trap."
  [attrib]
  (assoc (rand-nth traps) :attributes attrib))
