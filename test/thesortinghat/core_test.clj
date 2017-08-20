(ns thesortinghat.core-test
  (:require [clojure.test :refer :all]
            [thesortinghat.core :refer :all]
            [clojure.java.io :as io]
            [java-time :as time]))

(defn six-rows-of-five? [xs]
  (and
    (= 6 (count xs))
    (every? #(= 5 (count %)) xs)))

(deftest command-line-interface-test
  (is (= usage-message
         (with-out-str (-main))))

  (is (= not-found-message
         (with-out-str (-main "nosuchfile"))))

  (with-out-str
    (is (= :done
           (-main "resources/people.csv")))))

(deftest parse-file-test
  (is (six-rows-of-five?
        (read-file (io/resource "people.csv") \,)))

  (is (six-rows-of-five?
         (read-file (io/resource "people.psv") \|)))

  (is (six-rows-of-five?
         (read-file (io/resource "people.ssv") \space))))

(deftest detect-separator-test
  (is (= \, (detect-separator (io/resource "people.csv"))))
  (is (= \| (detect-separator (io/resource "people.psv"))))
  (is (= \space (detect-separator (io/resource "people.ssv")))))

(deftest parse-date-test
  (is (= (time/local-date 1990 12 30)
         (parse-date "12/30/1990")))
  (is (= (time/local-date 1990 12 30)
         (parse-date "12-30-1990")))
  (is (= (time/local-date 1990 12 30)
         (parse-date "1990/12/30")))
  (is (= (time/local-date 1990 12 30)
         (parse-date "1990-12-30")))
  (is (= "Unrecognized date of birth format: Not-a-date\n"
         (with-out-str (parse-date "Not-a-date")))))

(deftest by-birth-date-test
  (is (= ["4/4/1986"
          "12/22/1985"
          "11/30/1985"
          "3/1/1985"
          "2/1/1985"
          "1/1/1985"]
         (->> (read-file (io/resource "people.csv") \,)
              (by-birth-date)
              (map :date-of-birth)
              (map format-date)))
      "Dates are sorted temporally, not by their string values"))
