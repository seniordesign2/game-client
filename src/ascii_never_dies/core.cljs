(ns ascii-never-dies.core
  (:require
   [castra.core :as cas]
   [javelin.core :as jav :include-macros true]
   [cljs.core.async :refer [chan]]
   [ascii-never-dies.game :as game]
   [ascii-never-dies.input :as input]
   [ascii-never-dies.world :as w]
   [ascii-never-dies.player :as player]))

(enable-console-print!)

;; ---------------------------------------------------------------------------
;; RPC update cells

(jav/defc record nil)
(jav/defc error nil)
(jav/defc loading nil)
(jav/defc state nil)

;; ---------------------------------------------------------------------------
;; Data unloading functions

(defn get-xy
  "Returns the player's xy coords in the current room."
  []
  (let [{x :x y :y} @state]
    [x y]))

(defn get-health
  "Returns the player's current health."
  []
  (:cur_health @state))

(defn get-room-idx
  "Returns the coords of the room the player is currently in."
  []
  (:room_idx @state))

(defn get-rooms
  "Returns every room visited so far."
  []
  (:rooms @state))

;; ---------------------------------------------------------------------------
;; RPC functions

(def url "https://ascii-never-dies.herokuapp.com/")
(def get-user
  (cas/mkremote 'ascii-never-dies.handler/get-user
                record error loading
                {:url url}))
(def add-user
  (cas/mkremote 'ascii-never-dies.handler/add-user
                record error loading
                {:url url}))
(def save
  (cas/mkremote 'ascii-never-dies.handler/save
                record error loading
                {:url url}))
(def load
  (cas/mkremote 'ascii-never-dies.handler/load
                state error loading
                {:url url}))

;; ---------------------------------------------------------------------------
;; Initialization

(defn init
  "Initialize the game loop and the input loop."
  []
  (player/init w/width w/height 100)
  (w/init)
  (let [commands (chan)]
    (game/init commands)
    (input/init commands)))
