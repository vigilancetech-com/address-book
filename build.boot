(set-env!
;;  :repositories [["clojars" {:url "https://clojars.org/repo"}]]
  :dependencies '[[adzerk/boot-cljs          "2.1.4"]
                  [adzerk/bootlaces "0.1.13"]
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

(def +version+ "0.0.1-SNAPSHOT")

(task-options! push {:repo "clojars"}
               pom {:project 'org.clojars.viglancetech/address-book
                   :version +version+})

(require
  '[adzerk.boot-cljs         :refer [cljs]]
  '[adzerk.boot-reload       :refer [reload]]
  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl cljs-repl-env]]
  '[hoplon.boot-hoplon       :refer [hoplon prerender]]
  '[tailrecursion.boot-jetty :refer [serve]]
  '[adzerk.bootlaces :refer :all])


(bootlaces! +version+)


#_(deftask check-conflicts
  "Verify there are no dependency conflicts."
  []
  (with-pass-thru fs
    (require '[boot.pedantic :as pedant])
    (let [dep-conflicts (resolve 'pedant/dep-conflicts)]
      (if-let [conflicts (not-empty (dep-conflicts pod/env))]
        (throw (ex-info (str "Unresolved dependency conflicts. "
                             "Use :exclusions to resolve them!")
                        conflicts))
        (println "\nVerified there are no dependency conflicts.")))))

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
