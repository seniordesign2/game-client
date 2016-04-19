(ns ascii-never-dies.game
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]])
  (:require
   [ascii-never-dies.player :as player]
   [cljs.core.async :refer [chan put! <! >! timeout]]
   [dommy.core :as dommy]
   [ascii-never-dies.world :as w]
   [ascii-never-dies.screen :as screen]
   [ascii-never-dies.traps :as traps]))

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
  (if (player/is-dead?)
    (assoc world :status :game-over)))

(defn handle-move
  "Moves the character based on the given key press."
  [key commands]
  (let [[x y] (player/get-pos)]
    (case key
      (:left :h) (if-not (w/collision? [(dec x) y])
                   (player/move-player-left))
      (:right :l) (if-not (w/collision? [(inc x) y])
                    (player/move-player-right))
      (:up :k) (if-not (w/collision? [x (dec y)])
                 (player/move-player-up))
      (:down :j) (if-not (w/collision? [x (inc y)])
                   (player/move-player-down))))

  (let [[x y] (player/get-pos)
        tile (screen/get-tile [x y] (w/to-screen false))]
    (case (-> tile
              :attr
              :name)
      "trap" (put! commands (:effect tile))
      "door" (do
               (println "DOOR!\nfrom room: " @w/room-idx)
               (cond
                 (= y 0) (do (w/enter-room :n)
                             (player/enter-room w/width w/height :s))
                 (= x (dec w/width)) (do (w/enter-room :e)
                                         (player/enter-room w/width w/height :w))
                 (= x 0) (do (w/enter-room :w)
                             (player/enter-room w/width w/height :e))
                 (= y (dec w/height)) (do (w/enter-room :s)
                                          (player/enter-room w/width w/height :n)))
               (println "to room: " @w/room-idx))
      nil)))

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
                    (println "GAME OVER!")
                    (do
                      (plan-tick! 0 commands)
                      (-> (dommy/sel1 :#board)
                          (dommy/set-text! (w/print-board)))
                      (recur new-world))))

          :move (do
                  (println "Key pressed: " value)
                  (handle-move value commands)
                  (recur world))

          :damage (do
                    (player/damage-health value)
                    (-> (dommy/sel1 :#health)
                        (dommy/set-text! (str "HP: " (player/print-health))))
                    (recur world))

          :teleport (do
                      (println "Teleported " value " away")
                      (recur world))

          (throw (js/Error. (str "Unrecognized game command: " cmd))))))))

(defn init
  "Initialize the game loop."
  [commands]
  (println "Start game loop")
  (game! initial-world commands))
