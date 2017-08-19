(ns thesortinghat.core
  (:require
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [clojure.string :as string])
  (:import (java.io File)))

(def usage-message
  "Usage: lein run <filename>\n")

(def not-found-message
  "File not found\n")

(def separators
  [\, \| \space])

(def fields
  [:last-name :first-name :gender :favorite-color :date-of-birth])

(defn field-count-match? [file separator]
  (with-open [reader (io/reader file)]
    (= (count (first
                (csv/read-csv reader :separator separator)))
       (count fields))))

(defn detect-separator [file]
  (first (filter #(field-count-match? file %) separators)))

(defn read-file [^File file separator process-record]
  (with-open [reader (io/reader file)]
    (->> (csv/read-csv reader :separator separator)
         (map #(mapv string/trim %))
         (map #(zipmap fields %))
         (map #(process-record %))
         (doall))))

(defn -main [& args]
  (if (= 1 (count args))
    (let [file (io/file (first args))]
      (if (.exists file)
        (do (read-file file (detect-separator file) println)
            :done)
        (print not-found-message)))
    (print usage-message)))
