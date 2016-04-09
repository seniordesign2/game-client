(ns ascii-never-dies.core
  (:require
   [castra.core :as cas]
   [javelin.core :as jav :include-macros true]
   [cljs.core.async :refer [chan]]
   [ascii-never-dies.game :as game]
   [ascii-never-dies.input :as input]
   [ascii-never-dies.world :as w]
   [ascii-never-dies.player :as player]))

;; enable cljs to print to JS console or browser
(enable-console-print!)

;; RPC update cells
(jav/defc record nil)
(jav/defc error nil)
(jav/defc loading nil)
(jav/defc xy nil)

(defn get-xy
  "Returns the contents of the xy cell as a vector."
  []
  (let [{x :x y :y} @xy]
    [x y]))

(def url "https://ascii-never-dies.herokuapp.com/")
(def get-record
  (cas/mkremote 'ascii-never-dies.handler/get-record
                record error loading
                {:url url}))
(def update-record
  (cas/mkremote 'ascii-never-dies.handler/update-record
                record error loading
                {:url url}))
(def save-coords
  (cas/mkremote 'ascii-never-dies.handler/save-coords
                record error loading
                {:url url}))
(def load-coords
  (cas/mkremote 'ascii-never-dies.handler/load-coords
                xy error loading
                {:url url}))

(defn init
  "Initialize the game loop and the input loop."
  []
  (player/init w/width w/height 100)
  (let [commands (chan)]
    (game/init commands)
    (input/init commands)))
