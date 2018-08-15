(ns thesortinghat.core
  (:require
    [clojure.data.csv :as csv]
    [clojure.java.io :as io]
    [clojure.string :as string]
    [java-time :as time]))

(def fields
  [:last-name :first-name :gender :favorite-color :date-of-birth])

(def field-separators
  [\, \| \space])

(def output-date-pattern
  "M/d/yyyy")

(def input-date-patterns
  ["M/d/yyyy"
   "M-d-yyyy"
   "yyyy/MM/dd"
   "yyyy-MM-dd"])

(defn field-count-match? [csv separator]
  (= (count (first
              (csv/read-csv csv :separator separator)))
     (count fields)))

(defn detect-separator [csv]
  (first
    (filter #(field-count-match? csv %) field-separators)))

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
  (when d
    (time/format output-date-pattern d)))

(defn read-records [readable separator]
  (with-open [reader (io/reader readable)]
    (->> (csv/read-csv reader :separator separator)
         (map #(mapv string/trim %))
         (map #(zipmap fields %))
         (map #(update % :date-of-birth parse-date))
         (doall))))

(defn by-gender-then-last-name [records]
  (sort-by (juxt :gender
                 (comp string/lower-case :last-name)
                 (comp string/lower-case :first-name))
           records))

(defn oldest-to-youngest [records]
  (sort-by :date-of-birth records))

(defn by-last-name-descending [records]
  (reverse (sort-by (juxt (comp string/lower-case :last-name)
                          (comp string/lower-case :first-name))
                    records)))

(defn format-record [record]
  (as-> record x
        (update x :date-of-birth format-date)
        (map x fields)
        (string/join ", " x)))
