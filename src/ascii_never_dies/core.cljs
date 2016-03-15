(ns ascii-never-dies.core
  (:require
   [castra.core :as cas]
   [javelin.core :as jav :include-macros true]))

(jav/defc record nil)
(jav/defc error nil)
(jav/defc loading nil)

(def url "https://ascii-never-dies.herokuapp.com/")
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
