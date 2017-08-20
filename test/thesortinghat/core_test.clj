(ns thesortinghat.core-test
  (:require [clojure.test :refer :all]
            [thesortinghat.core :as hat]
            [thesortinghat.cli :as cli]
            [clojure.java.io :as io]
            [java-time :as time]))

(deftest detect-separator-test
  (is (= \, (hat/detect-separator (slurp (io/resource "people.csv")))))
  (is (= \| (hat/detect-separator (slurp (io/resource "people.psv")))))
  (is (= \space (hat/detect-separator (slurp (io/resource "people.ssv"))))))

(deftest parse-date-test
  (is (= (time/local-date 1990 12 30)
         (hat/parse-date "12/30/1990")))
  (is (= (time/local-date 1990 12 30)
         (hat/parse-date "12-30-1990")))
  (is (= (time/local-date 1990 12 30)
         (hat/parse-date "1990/12/30")))
  (is (= (time/local-date 1990 12 30)
         (hat/parse-date "1990-12-30")))
  (is (= "Unrecognized date of birth format: Not-a-date\n"
         (with-out-str (hat/parse-date "Not-a-date")))))

(deftest format-record-test
  (is (string? (hat/format-record {}))
      "Missing fields should not cause an exception"))

(defn six-rows-of-five? [xs]
  (and
    (= 6 (count xs))
    (every? #(= 5 (count %)) xs)))

(deftest parse-file-test
  (is (six-rows-of-five?
        (hat/read-records (io/resource "people.csv") \,)))

  (is (six-rows-of-five?
        (hat/read-records (io/resource "people.psv") \|)))

  (is (six-rows-of-five?
        (hat/read-records (io/resource "people.ssv") \space))))

(deftest by-birth-date-test
  (is (= ["4/4/1986"
          "12/22/1985"
          "11/30/1985"
          "3/1/1985"
          "2/1/1985"
          "1/1/1985"]
         (->> (hat/read-records (io/resource "people.csv") \,)
              (hat/by-birth-date)
              (map :date-of-birth)
              (map hat/format-date)))
      "Dates are sorted temporally, not by their string values"))
