(ns ascii-never-dies.core
  (:require-macros
   [cljs.core.async.macros :refer [go go-loop]])
  (:require
   [cljs.core.async :refer [chan put! <! >! timeout]]
   [castra.core :as cas]
   [javelin.core :as jav :include-macros true]))

(jav/defc record nil)
(jav/defc error nil)
(jav/defc test-err nil)
(jav/defc loading nil)

(def url "https://ascii-never-dies.herokuapp.com/")
(def rpc-test
  (cas/mkremote 'ascii-never-dies.handler/rpc-test
                record test-err loading
                {:url url}))
(def get-record
  (cas/mkremote 'ascii-never-dies.handler/get-record
                record error loading
                {:url url}))
(def update-record
  (cas/mkremote 'ascii-never-dies.handler/update-record
                record error loading
                {:url url}))

;; enable cljs to print to JS console or browser
(enable-console-print!)

;; print to the console
(println "Hello, World!")

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
  ;(println "world update")
  )


(defn game!
  "Game internal loop that processes commands and updates state applying functions"
  [initial-world cmds]
  (go-loop [{:keys [status] :as world} initial-world]
    (let [[cmd v] (<! cmds)]
                                        ;(println "Received: " cmd)
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

          :turn (do (println "Key pressed: " v) (recur world))

          (throw (js/Error. (str "Unrecognized game command: " cmd))))))))

(defn init [commands]
  (let [notifos (chan)]
    (game! initial-world commands)
    notifos))
