(ns whimrepl.version
  (:require
    [clojure.string :as str]))

(def major 1)
(def minor 0)
(def patch 0)
(def snapshot false)

(def string
  (str
    (str/join "." (filter identity [major minor patch]))
    (if snapshot "-SNAPSHOT" "")))
