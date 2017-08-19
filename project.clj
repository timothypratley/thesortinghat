(defproject thesortinghat "0.1.0-SNAPSHOT"
  :description "The Sorting Hat; reads records about people and displays list of people sorted by attribute"
  :url "http://github.com/timothypratley/thesortinghat"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main thesortinghat.core
  :plugins [[com.jakemccrary/lein-test-refresh "0.20.0"]
            [lein-cloverage "1.0.9"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.csv "0.1.4"]])
