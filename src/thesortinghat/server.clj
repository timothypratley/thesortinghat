(ns thesortinghat.server
  (:require
    [thesortinghat.core :as hat]
    [compojure.core :refer [GET POST] :as compojure]
    [compojure.handler :as handler]
    [compojure.route :as route]
    [hiccup.core :as hiccup]
    [ring.util.io :as ring-io]
    [ring.util.response :as response]
    [ring.middleware.json :as json]
    [clojure.string :as string]
    [clojure.java.io :as io]))

(defonce
  ^{:docstring
    "The state of the server is a map of records, emulating a database.
    The keys in the map identify a person by name and date of birth,
    similar to a primary key in a database.
    The values are the attributes associated with the person; favorite color and gender."}
  records
  (atom {}))

(def identifiers
  [:last-name :first-name :date-of-birth])

(defn welcome []
  (hiccup/html
    [:div
     [:h2 "Welcome to The Sorting Hat"]
     [:h3 "Load from a file"]
     [:form {:action "load" :method "post"}
      [:input {:type "submit" :value "LOAD people.csv"}]]
     [:form {:action "records" :method "post"}
      [:h3 "Add a new record"]
      [:div "Hint: " (string/join ", " (map name hat/fields)) ""]
      [:input {:name "record" :size "50"}]
      [:input {:type "submit" :value "POST"}]]
     [:h3 "Query by"]
     [:ul
      [:li [:a {:href "records/gender"} "gender (female first) then last name ascending"]]
      [:li [:a {:href "records/birthdate"} "birth date oldest to youngest"]]
      [:li [:a {:href "records/name"} "last name descending"]]]]))

(defn post-record [{:keys [body form-params]}]
  (let [record-string (if-let [record (get form-params "record")]
                        record
                        (slurp body))
        separator (hat/detect-separator record-string)]
    (if separator
      (let [[record] (hat/read-records
                       (ring-io/string-input-stream record-string)
                       separator)]
        (println "Received a record:" record)
        (swap! records assoc
               (select-keys record identifiers)
               (apply dissoc record identifiers))
        {:status 200
         :body "ok"})
      {:status 400
       :body (str "No separator found. Expected something like '"
                  (string/join ", " (map name hat/fields)) "'.\n")})))

(defn current-records []
  (for [[identifiers attributes] @records]
    (merge identifiers attributes)))

(defn query [sort-fn]
  (->> (current-records)
       (sort-fn)
       (map #(update % :date-of-birth hat/format-date))
       (response/response)))

(defn load-example-records []
  (into {}
        (for [record (hat/read-records (io/resource "people.csv") \,)]
          [(select-keys record identifiers)
           (apply dissoc record identifiers)])))

(def routes
  (compojure/routes
    (GET "/" []
      (welcome))
    (GET "/status" []
      "Running")
    (POST "/load" []
      (swap! records merge (load-example-records))
      "ok")
    (compojure/context "/records" []
      (POST "/" request
        (post-record request))
      (GET "/gender" []
        (query hat/by-gender-then-last-name))
      (GET "/birthdate" []
        (query hat/oldest-to-youngest))
      (GET "/name" []
        (query hat/by-last-name-descending)))
    (route/not-found "Not found")))

(def handler
  (-> #'routes
      (json/wrap-json-response {:pretty true})
      (handler/api)))
