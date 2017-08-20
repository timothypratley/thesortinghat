(ns thesortinghat.core
  (:require
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [clojure.string :as string]
    [java-time :as time]
    [java-time.format :as time-format])
  (:import (java.io File)))

(def usage-message
  "Usage: lein run <filename>\n")

(def not-found-message
  "File not found\n")

(def separators
  [\, \| \space])

(def output-date-pattern
  "M/d/yyyy")

(def input-date-patterns
  ["M/d/yyyy"
   "M-d-yyyy"
   "yyyy/MM/dd"
   "yyyy-MM-dd"])

(def fields
  [:last-name :first-name :gender :favorite-color :date-of-birth])

(defn field-count-match? [file separator]
  (with-open [reader (io/reader file)]
    (= (count (first
                (csv/read-csv reader :separator separator)))
       (count fields))))

(defn detect-separator [file]
  (first
    (filter #(field-count-match? file %) separators)))

(defn parse-date
  "Given a string, tries to parse it from a list of expected patterns.
  Returns the date or nil."
  [s]
  (or (first
        (for [pattern input-date-patterns
              :let [date (try
                           (time/local-date pattern s)
                           (catch Exception ex))]
              :when date]
          date))
      (println "Unrecognized date of birth format:" s)))

(defn format-date [d]
  (time/format output-date-pattern d))

(defn read-file [^File file separator]
  (with-open [reader (io/reader file)]
    (->> (csv/read-csv reader :separator separator)
         (map #(mapv string/trim %))
         (map #(zipmap fields %))
         (map #(update % :date-of-birth parse-date))
         (doall))))

(defn by-gender [records]
  (sort-by (juxt :gender :last-name) records))

(defn by-birth-date [records]
  (reverse (sort-by :date-of-birth records)))

(defn by-last-name [records]
  (sort-by :last-name records))

(defn format-record [record]
  (as-> record x
        (update x :date-of-birth format-date)
        (map x fields)
        (string/join ", " x)))

(defn print-records [records]
  (doseq [record records]
    (println (format-record record))))

(defn -main [& args]
  (if (= 1 (count args))
    (let [file (io/file (first args))]
      (if (.exists file)
        (do
          (let [records (read-file file (detect-separator file))]
            (println "*** By gender:")
            (print-records (by-gender records))
            (println "*** By birth date:")
            (print-records (by-birth-date records))
            (println "*** By last-name:")
            (print-records (by-last-name records)))
          :done)
        (print not-found-message)))
    (print usage-message)))
