(ns build
  (:require
             [shadow.cljs.devtools.api :as shadow]
             [hoplon  :refer hoplon prerender]))


(defn hop []
  (hoplon))
