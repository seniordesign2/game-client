(ns ascii-never-dies.input
  (:require [ascii-never-dies.game :as game]
            [goog.dom :as dom]
            [goog.events :as events]
            [cljs.core.async :as async :refer [chan put! pipe unique merge map< filter< alts!]]))


;; enable cljs to print to JS console or browser
(enable-console-print!)

;; define keycodes that interest us
(def keycodes
  "Keycodes that interest us. Taken from
  http://docs.closure-library.googlecode.com/git/closure_goog_events_keynames.js.source.html#line33"
  {37 :left
   38 :up
   39 :right
   40 :down
   32 :space
   13 :enter})

(defn event->key
  "Transform an js event object into the key name"
  [ev] (get keycodes (.-keyCode ev) :key-not-found))

(defn event-chan
  "Creates a channel with the events of type event-type and optionally applies the function parse-event to each event."
  ([event-type] (event-chan event-type identity false))
  ([event-type parse-event]
   (event-chan event-type parse-event false))
  ([event-type parse-event prevent-default]
   (let [ev-chan (chan)]
     (events/listen (.-body js/document)
                    event-type
                    #(do
                       (if prevent-default (.preventDefault %))
                       (put! ev-chan (parse-event %))))
     ev-chan)))

(defn keys-chan
  "Returns a channel with the key events of event-type parsed and filtered by the allowed keys."
  [event-type allowed-keys]
  (let [evs (event-chan event-type event->key)]
    (filter< allowed-keys evs)))

(def move-keys "Keys that trigger movement" #{:left :up :right :down :space :enter})

(def valid-keys-down
  "Keys we want to listen on key down"
  move-keys)

(defn keys-down-chan
  "Create a channel of keys pressed down restricted by the valid keys"
  [] (keys-chan (.-KEYDOWN events/EventType) valid-keys-down))

(defn key-down->command
  "Transform a key pressed down to the command we will send to the game"
  [k]
  (cond
    (move-keys k) [:turn k]))

(defn init-events!
  "Initialize event processing. It takes all the key presses and transforms them into command and passes them to the game commands channel"
  [game-commands]
  (let [keys-pressed (keys-down-chan)
        commands (unique (merge [(map< key-down->command keys-pressed)]))]
    (pipe commands game-commands)))

(defn init
  "Initialize the UI by initializing the user input, adapting the canvas
  and starting the render loop."
  []
  (println "Start init")
  (let [commands (chan)
        notifos (game/init commands)]
    (init-events! commands)
    (put! commands [:init])
    ))
