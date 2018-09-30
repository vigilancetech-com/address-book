(set-env!
;;  :repositories [["clojars" {:url "https://clojars.org/repo"}]]
  :dependencies '[[adzerk/boot-cljs          "2.1.4"]
                  [adzerk/bootlaces "0.1.13"]
;;                  [vigilancetech/boot-cljs-repl   "0.4.0-SNAPSHOT"  ] ;; latest release
                  [com.cemerick/pomegranate "1.0.0"]
                  [powerlaces/boot-figreload "0.5.14"]
                 [adzerk/boot-reload        "0.6.0"]
                  [hoplon/hoplon             "7.3.0-SNAPSHOT"]
                  [org.clojure/clojure       "1.9.0"]
                  [org.clojure/clojurescript "1.10.339"]
;;                  [cider/piggieback "0.3.9"  :scope "test"]
;                 [com.cemerick/piggieback "0.2.1"  :scope "test"]
;;                  [weasel                  "0.7.0"  :scope "test"]
                  [ajchemist/boot-figwheel "0.5.4-6"]
                  [nrepl "0.4.5" :scope "test"]
                  [tailrecursion/boot-jetty  "0.1.3"]]
  :source-paths #{"src"}
  :asset-paths  #{"assets"})

(def +version+ "0.0.1-SNAPSHOT")

(declare figwheel)
(task-options! push {:repo "clojars"}

               figwheel {:build-ids  ["dev"]
                         :all-builds [{:id "dev"
                                       :source-paths ["src"]   ; cljs(cljc) directories
                                       :compiler {:main 'app.core
                                                  :output-to "app.js"}
                                       :figwheel {:build-id  "dev"
                                                  :on-jsload 'app.core/main
                                                  :heads-up-display true
                                                  :autoload true
                                                  :debug false}}]
                         :figwheel-options {:open-file-command "emacsclient"
                                            :repl true}}

               pom {:project 'viglancetech/address-book
                    :scm {:url "http://github.com/vigilancetech-com/address-book"}
                    :url "http://github.com/vigilancetech-com/address-book"
                    :version +version+})

(require
  '[boot-figwheel :rename [cljs-repl fw-cljs-repl]]
  '[cemerick.pomegranate :only (add-dependencies)]
  '[adzerk.boot-cljs         :refer [cljs]]
  '[adzerk.boot-reload       :refer [reload]]
;;  '[adzerk.boot-cljs-repl :refer [cljs-repl start-repl cljs-repl-env]]
;;  '[powerlaces.boot-figreload :refer [reload]]
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
 ;;   (cljs-repl-env)
    (cljs)
    (serve :port 8000)))

(deftask prod
  "Build address-book for production deployment."
  []
  (comp
    (hoplon)
    (cljs :optimizations :advanced)
    (target :dir #{"target"})))
