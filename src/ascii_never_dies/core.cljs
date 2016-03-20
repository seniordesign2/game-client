(ns ascii-never-dies.core
  (:require
   [castra.core :as cas]
   [javelin.core :as jav :include-macros true]
   [ascii-never-dies.tiles :as tiles]
   [ascii-never-dies.player :as player]))

;; enable cljs to print to JS console or browser
(enable-console-print!)

;; RPC update cells
(jav/defc record nil)
(jav/defc error nil)
(jav/defc loading nil)
(jav/defc xy nil)

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

;; print to the console
(println "Hello, World!")
