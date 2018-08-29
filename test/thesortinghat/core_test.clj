(ns thesortinghat.core-test
  (:require [clojure.test :refer :all]
            [thesortinghat.core :as hat]
            [thesortinghat.cli :as cli]
            [clojure.java.io :as io]
            [java-time :as time]))

(deftest detect-separator-test
  (testing "The correct separator is detected"
    (is (= \, (hat/detect-separator (slurp (io/resource "people.csv")))))
    (is (= \| (hat/detect-separator (slurp (io/resource "people.psv")))))
    (is (= \space (hat/detect-separator (slurp (io/resource "people.ssv")))))))

(deftest parse-date-test
  (testing "Expected date formats are read correctly"
    (is (= (time/local-date 1990 12 30)
           (hat/parse-date "12/30/1990")))
    (is (= (time/local-date 1990 12 30)
           (hat/parse-date "12-30-1990")))
    (is (= (time/local-date 1990 12 30)
           (hat/parse-date "1990/12/30")))
    (is (= (time/local-date 1990 12 30)
           (hat/parse-date "1990-12-30")))
    (is (= "Unrecognized date of birth format: Not-a-date\n"
           (with-out-str (hat/parse-date "Not-a-date"))))))

(deftest format-record-test
  (is (string? (hat/format-record {}))
      "Missing fields should not cause an exception"))

(defn six-rows-of-five? [xs]
  (and
    (= 6 (count xs))
    (every? #(= 5 (count %)) xs)))

(deftest parse-file-test
  (testing "The correct number of records, of the correct size are read"
    (is (six-rows-of-five?
          (hat/read-records (io/resource "people.csv") \,)))

    (is (six-rows-of-five?
          (hat/read-records (io/resource "people.psv") \|)))

    (is (six-rows-of-five?
          (hat/read-records (io/resource "people.ssv") \space)))))

(deftest by-birth-date-test
  (is (= ["1/1/1985"
          "2/1/1985"
          "3/1/1985"
          "11/30/1985"
          "12/22/1985"
          "4/4/1986"]
         (->> (hat/read-records (io/resource "people.csv") \,)
              (hat/oldest-to-youngest)
              (map :date-of-birth)
              (map hat/format-date)))
      "Dates should be sorted temporally, not by their string values"))

(defn reverse-compare
  "Instead of reversing the sort,
  we can invert the comparator"
  [a b]
  (- (compare a b)))

(defn by-last-name-descending2
  [records]
  (sort-by (juxt :last-name :first-name)
           reverse-compare
           records))

(deftest alternative-sorting-approach-test
  (is (= [["Pratley" "Timothy"]
          ["Pratley" "Adam"]
          ["Bernshteyn" "Ilya"]
          ["Bernshteyn" "Alex"]]
         (map (juxt :last-name :first-name)
              (by-last-name-descending2
                [{:last-name "Bernshteyn"
                  :first-name "Ilya"}
                 {:last-name "Pratley"
                  :first-name "Timothy"}
                 {:last-name "Pratley"
                  :first-name "Adam"}
                 {:last-name "Bernshteyn"
                  :first-name "Alex"}])))))
