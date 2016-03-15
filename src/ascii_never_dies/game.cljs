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
  )

(defn handle-move
  "Moves the character based on the given key press"
  [key]
  (case key
    :left (player/move-player-left)
    :right (player/move-player-right)
    :up (player/move-player-up tiles/width)
    :down (player/move-player-down tiles/width)))


(defn game!
  "Game internal loop that processes commands and updates state applying functions"
  [initial-world cmds]
  (go-loop [{:keys [status] :as world} initial-world]
    (let [[cmd v] (<! cmds)]
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

          :turn (do (println "Key pressed: " v) (handle-move v) (recur world))

          (throw (js/Error. (str "Unrecognized game command: " cmd))))))))

(defn init [commands]
  (let [notifos (chan)]
    (game! initial-world commands)
    notifos))
