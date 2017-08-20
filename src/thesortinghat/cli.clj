(ns thesortinghat.cli
  (:require
    [thesortinghat.core :as hat]
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [clojure.string :as string])
  (:import (java.io File)))

(def usage-message
  "Usage: lein run <filename>\n")

(def not-found-message
  "File not found\n")

(defn print-records [records]
  (doseq [record records]
    (println (hat/format-record record))))

(defn -main [& args]
  (if (= 1 (count args))
    (let [file (io/file (first args))]
      (if (.exists file)
        (do
          (let [records (hat/read-file file (hat/detect-separator file))]
            (println "*** By gender:")
            (print-records (hat/by-gender records))
            (println "*** By birth date:")
            (print-records (hat/by-birth-date records))
            (println "*** By last-name:")
            (print-records (hat/by-last-name records)))
          :done)
        (print not-found-message)))
    (print usage-message)))
