(ns thesortinghat.server
  (:require
    [thesortinghat.core :as hat]
    [clojure.string :as string]
    [compojure.core :refer [GET POST] :as compojure]
    [compojure.route :as route]
    [compojure.handler :as handler]
    [hiccup.core :as hiccup]
    [ring.util.io :as io]))

(defonce
  ^{:docstring "The state of the server is a map of records
  where the key identifies a person (by name and date of birth)
  and the value are the person's attributes (favorite color and gender)"}
  records
  (atom {}))

(defn welcome []
  (hiccup/html
    [:div
     [:h2 "Welcome to The Sorting Hat"]
     [:p "Please send me some REST"]]))

(defn post-record [{:keys [body] :as request}]
  (let [body-string (slurp body)
        separator (hat/detect-separator (io/string-input-stream body-string))]
    (if separator
      (let [[record] (hat/read-records
                       (io/string-input-stream body-string)
                       separator)]
        (println "Received a record:" record)
        (swap! records assoc
               (select-keys record hat/identifiers)
               (apply dissoc record hat/identifiers))
        {:status 200})
      {:status 400
       :body hat/no-separator-message})))

(defn current-records []
  (for [[identifiers attributes] @records]
    (merge identifiers attributes)))

(defn query [sort-fn]
  (->> (current-records)
       (sort-fn)
       (map hat/format-record)
       (string/join \newline)))

(def routes
  (compojure/routes
    (GET "/" [] (welcome))
    (compojure/context "/records" []
      (POST "/" request (post-record request))
      (GET "/gender" [] (query hat/by-gender))
      (GET "/birthdate" [] (query hat/by-birth-date))
      (GET "/name" [] (query hat/by-last-name)))
    (route/not-found "Not found")))

(def handler
  (handler/api #'routes))
