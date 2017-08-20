(defproject thesortinghat "0.1.0-SNAPSHOT"
  :description "The Sorting Hat; reads records about people and displays list of people sorted by attribute"
  :url "http://github.com/timothypratley/thesortinghat"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main thesortinghat.core
  :ring {:handler thesortinghat.server/handler}
  :plugins [[com.jakemccrary/lein-test-refresh "0.20.0"]
            [lein-cloverage "1.0.9"]
            [lein-ring "0.12.0"]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.csv "0.1.4"]
                 [clojure.java-time "0.3.0"]
                 [ring "1.6.2"]
                 [ring/ring-mock "0.3.1"]
                 [ring/ring-json "0.4.0"]
                 [compojure "1.6.0"]])
