(set-env!
;;  :repositories [["clojars" {:url "https://clojars.org/repo"}]]
  :dependencies '[[adzerk/boot-cljs          "2.1.4"]
                  [adzerk/boot-cljs-repl   "0.4.0-SNAPSHOT"  ] ;; latest release
                  [adzerk/boot-reload        "0.6.0"]
                  [hoplon/hoplon             "7.3.0-SNAPSHOT"]
                  [org.clojure/clojure       "1.9.0"]
                  [org.clojure/clojurescript "1.10.339"]
                  [cider/piggieback "0.3.9"  :scope "test"]
;                 [com.cemerick/piggieback "0.2.1"  :scope "test"]
                  [weasel                  "0.7.0"  :scope "test"]
                  [nrepl "0.4.5" :scope "test"]
                  [tailrecursion/boot-jetty  "0.1.3"]]
  :source-paths #{"src"}
  :asset-paths  #{"assets"})

(task-options! push {:repo "clojars"})

(require
  '[adzerk.boot-cljs         :refer [cljs]]
  '[adzerk.boot-reload       :refer [reload]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl cljs-repl-env]]
  '[hoplon.boot-hoplon       :refer [hoplon prerender]]
  '[tailrecursion.boot-jetty :refer [serve]])

(deftask dev
  "Build address-book for local development."
  []
  (comp
    (watch)
    (speak)
    (hoplon)
    (reload)
    (cljs-repl-env)
    (cljs)
    (serve :port 8000)))

(deftask prod
  "Build address-book for production deployment."
  []
  (comp
    (hoplon)
    (cljs :optimizations :advanced)
    (target :dir #{"target"})))
