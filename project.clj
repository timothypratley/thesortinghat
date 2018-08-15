(defproject thesortinghat "0.1.0-SNAPSHOT"
  :description "Reads records about people and displays list of people sorted by attribute"
  :url "http://github.com/timothypratley/thesortinghat"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main thesortinghat.cli
  :ring {:handler thesortinghat.server/handler}
  :plugins [[com.jakemccrary/lein-test-refresh "0.23.0"]
            [lein-cloverage "1.0.11"]
            [lein-ring "0.12.4"]]
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/data.csv "0.1.4"]
                 [clojure.java-time "0.3.2"]
                 [ring "1.6.3"]
                 [ring/ring-mock "0.3.2"]
                 [ring/ring-json "0.4.0"]
                 [compojure "1.6.1"]])
