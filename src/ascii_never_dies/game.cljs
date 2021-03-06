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
  "Checks the tile the player is trying to be moved into."
  [key commands]
  (let [[x y] (player/get-pos)
        [x y] (case key
                (:left :h)  [(dec x) y]
                (:right :l) [(inc x) y]
                (:up :k)    [x (dec y)]
                (:down :j)  [x (inc y)])
        tile (screen/get-tile [x y] (w/to-screen false))]
    (if-not (w/collision? [x y])
      (do
        (player/set-pos [x y])
        (case (:name tile)
          "trap" (do
                   (println (str "Trap: " (traps/get-effect tile)))
                   (put! commands (traps/get-effect tile)))
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
          nil))
      (if (= "enemy" (:name tile))
        (w/damage-enemy [x y] (player/get-attack))))))

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
                  (-> (dommy/sel1 :#board)
                      (dommy/set-html! (w/print-board)))
                  (-> (dommy/sel1 :#health)
                      (dommy/set-text! (str "HP: " (player/print-health))))
                  (if (= status :game-over)
                    (do
                      (js/alert "GAME OVER!")
                      (player/init w/width w/height 100)
                      (w/init)
                      (plan-tick! 0 commands)
                      (recur initial-world))
                    (do
                      (plan-tick! 0 commands)
                      (recur new-world))))

          :move (do
                  (println "Key pressed: " value)
                  (handle-move value commands)
                  (recur world))

          :damage (do
                    (player/damage-health value)
                    (recur world))

          :teleport (recur world)

          (throw (js/Error. (str "Unrecognized game command: " cmd))))))))

(defn init
  "Initialize the game loop."
  [commands]
  (println "Start game loop")
  (game! initial-world commands))
