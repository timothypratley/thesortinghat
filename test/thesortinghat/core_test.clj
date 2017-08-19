(ns thesortinghat.core-test
  (:require [clojure.test :refer :all]
            [thesortinghat.core :refer :all]))

(deftest command-line-interface-test
  (is (= usage-message
         (with-out-str (-main)))))
