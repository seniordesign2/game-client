(ns ascii-never-dies.game
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]])
  (:require
   [ascii-never-dies.player :as player]
   [ascii-never-dies.tiles :as tiles]
   [cljs.core.async :refer [chan put! <! >! timeout]]
   [dommy.core :as dommy]))

(def initial-world {:status nil
                    :screens [:play]})

(defn plan-tick!
  "Schedule a game tick."
  [speed commands]
  (go
    (<! (timeout speed)) ; TODO: why is this necessary, even when speed is 0?
    (put! commands [:tick])))

(defn update-world
  "Applies the game constraints to the world and returns the new version."
  [{:keys [status] :as world}]
  )

(defn handle-move
  "Moves the character based on the given key press."
  [key]
  (case key
    (:left :h) (player/move-player-left)
    (:right :l) (player/move-player-right)
    (:up :k) (player/move-player-up tiles/width)
    (:down :j) (player/move-player-down tiles/width)))

(defn game!
  "Game internal loop that processes commands and updates state applying functions."
  [initial-world commands]
  (go-loop [{:keys [status] :as world} initial-world]
    (let [[cmd value] (<! commands)]
      (if (and (= status :game-over) (not= cmd :reset))
        (recur world)
        (case cmd
          :init (do
                  (plan-tick! 0 commands)
                  (recur world))
          
          :tick (let [new-world (update-world world)
                      status (:status new-world)]
                  (if (= status :game-over)
                    (recur new-world)
                    (do
                      (plan-tick! 0 commands)
                      (-> (dommy/sel1 :#board)
                          #_(dommy/set-text! (tiles/print-board))
                          (dommy/set-text! (tiles/print-board)))
                      (recur new-world))))

          :move (do
                  (println "Key pressed: " value)
                  (handle-move value)
                  (recur world))

          :damage (do
                    (swap! player/cur-health dec)
                    (do
                      (-> (dommy/sel1 :#health)
                          (dommy/set-text! (str "HP: " (player/print-health))))
                      (recur world)))

          (throw (js/Error. (str "Unrecognized game command: " cmd))))))))

(defn init
  "Initialize the game loop."
  [commands]
  (println "Start game loop")
  (game! initial-world commands))
