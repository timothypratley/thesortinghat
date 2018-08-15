(ns thesortinghat.cli
  (:require
    [thesortinghat.core :as hat]
    [clojure.java.io :as io]
    [clojure.string :as string]))

(def usage-message
  "Usage: lein run <filename>\n")

(def not-found-message
  "File not found\n")

(def no-separator-message
  (str "No separator found. Expected lines of records like '"
       (string/join ", " (map name hat/fields)) "'.\n"))

(defn print-records [records]
  (doseq [record records]
    (println (hat/format-record record))))

(defn -main [& args]
  (if (= 1 (count args))
    (let [file (io/file (first args))]
      (if (.exists file)
        (do
          (if-let [separator (hat/detect-separator (slurp file))]
            (let [records (hat/read-records file separator)]
              (println "== By gender (female first) then last name:")
              (print-records (hat/by-gender-then-last-name records))
              (println)
              (println "== By birth date oldest to youngest:")
              (print-records (hat/oldest-to-youngest records))
              (println)
              (println "== By last-name descending:")
              (print-records (hat/by-last-name-descending records))
              (println)
              :done)
            (print no-separator-message)))
        (print not-found-message)))
    (print usage-message)))
