(ns ascii-never-dies.game
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]])
  (:require
   [ascii-never-dies.player :as player]
   [ascii-never-dies.tiles :as tiles]
   [cljs.core.async :refer [chan put! <! >! timeout]]))


  (def initial-world {:status nil})

  (defn plan-tick!
    "Tick the game after the elapsed speed time"
    ([speed cmds] (plan-tick! speed cmds (chan)))
    ([speed cmds shortcircuit]
     (go
       (alts! [(timeout speed) shortcircuit])
       (put! cmds [:tick]))))

(defn update-world
  "Applies the game constraints (eating, dying, ...) to the world and returns the new version."
  [{:keys [status] :as world}]
  (println "world update")
  )


(defn game!
  "Game internal loop that processes commands and updates state applying functions"
  [initial-world cmds]
  (go-loop [{:keys [status] :as world} initial-world]
    (let [[cmd v] (<! cmds)]

      (if (not (case cmd :tick))
        (println "Received: " cmd ", " v))
      (if (and (= status :game-over) (not= cmd :reset))
        (recur world)
        (case cmd
          :init (do (plan-tick! 0 cmds) (recur world))
          
          :tick (let [new-world (update-world world)
                      status (:status "new world")]
                  (if (= status :game-over)
                    (do
                                        ; (>! notify [:game-over])
                      (recur new-world))
                    (do
                      (plan-tick! 1 cmds)
                                        ; (>! notify [status])
                                        ; (>! notify [:world new-world])
                      (recur new-world))))

          :turn (do (println "Key pressed: " v) (player/move-player-left) (tiles/print-board) (recur world))

          (throw (js/Error. (str "Unrecognized game command: " cmd))))))))

(defn init [commands]
  (let [notifos (chan)]
    (game! initial-world commands)
    notifos))
