(set-env!
 :dependencies '[[adzerk/boot-cljs          "1.7.228-1"]
                 [adzerk/boot-reload        "0.4.5"]
                 [hoplon/boot-hoplon        "0.1.13"]
                 [hoplon/hoplon             "6.0.0-alpha13"]
                 [org.clojure/clojure       "1.7.0"]
                 [org.clojure/clojurescript "1.7.228"]
                 [org.clojure/core.async    "0.2.374"]
                 [tailrecursion/boot-jetty  "0.1.3"]
                 [prismatic/dommy           "1.1.0"]
                 [hoplon/castra             "3.0.0-alpha3"]]
 :source-paths #{"src"}
 :asset-paths  #{"assets"})

(require
  '[adzerk.boot-cljs         :refer [cljs]]
  '[adzerk.boot-reload       :refer [reload]]
  '[hoplon.boot-hoplon       :refer [hoplon prerender]]
  '[tailrecursion.boot-jetty :refer [serve]])

(deftask dev
  "Build game-client for local development."
  []
  (comp
   (watch)
   (speak)
   (hoplon)
   (reload)
   (cljs)
   (serve :port 8000)))

(deftask prod
  "Build game-client for production deployment."
  []
  (comp
    (hoplon)
    (cljs :optimizations :advanced)
    (prerender)))
