(ns ascii-never-dies.tiles
  (:require [clojure.string :as str]))

;; vectors to be updated with game info
(def vRowA (vector "#" "#" "#" "#" "#" "#" "#" "#" "#" "#" "#"))
(def vRowB (vector "#" "." "." "." "." "." "." "." "B" "." "#"))
(def vRowC (vector "#" "." "." "." "." "@" "." "." "." "." "#"))
(def vRowD (vector "#" "." "B" "." "." "." "." "." "." "." "#"))
(def vRowE (vector "#" "#" "#" "#" "#" "#" "#" "#" "#" "#" "#"))

;; string sequences of each vector to be printed by index.cljs.hl
(def strRowA (clojure.string/join vRowA))
(def strRowB (clojure.string/join vRowB))
(def strRowC (clojure.string/join vRowC))
(def strRowD (clojure.string/join vRowD))
(def strRowE (clojure.string/join vRowE))
