(ns thesortinghat.core-test
  (:require [clojure.test :refer :all]
            [thesortinghat.core :refer :all]
            [clojure.java.io :as io]))

(defn six-rows-of-five? [xs]
  (and
    (= 6 (count xs))
    (every? #(= 5 (count %)) xs)))

(deftest command-line-interface-test
  (is (= usage-message
         (with-out-str (-main))))

  (is (= not-found-message
         (with-out-str (-main "nosuchfile"))))

  (is (= :done (-main "resources/people.csv"))))

(deftest parse-file-test
  (is (six-rows-of-five?
        (read-file (io/resource "people.csv") \, identity)))

  (is (six-rows-of-five?
         (read-file (io/resource "people.psv") \| identity)))

  (is (six-rows-of-five?
         (read-file (io/resource "people.ssv") \space identity))))

(deftest detect-separator-test
  (is (= \, (detect-separator (io/resource "people.csv"))))
  (is (= \| (detect-separator (io/resource "people.psv"))))
  (is (= \space (detect-separator (io/resource "people.ssv")))))