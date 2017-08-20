(ns thesortinghat.cli-test
  (:require
    [clojure.test :refer :all]
    [thesortinghat.cli :as cli]
    [thesortinghat.core :as hat]
    [clojure.java.io :as io]))

(deftest -main-test
  (is (= cli/usage-message
         (with-out-str (cli/-main))))

  (is (= cli/not-found-message
         (with-out-str (cli/-main "nosuchfile"))))

  (is (= hat/no-separator-message
         (with-out-str (cli/-main "resources/bad.csv"))))

  (with-out-str
    (is (= :done
           (cli/-main "resources/people.csv")))))
