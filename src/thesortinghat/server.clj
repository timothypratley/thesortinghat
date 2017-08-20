(ns thesortinghat.server
  (:require
    [thesortinghat.core :as thesortinghat]
    [compojure.core :refer [GET POST] :as compojure]
    [compojure.route :as route]
    [compojure.handler :as handler]
    [hiccup.core :as hiccup]))

(defonce records
  (atom []))

(defn welcome []
  (hiccup/html
    [:div
     [:h2 "Welcome to The Sorting Hat."]
     [:p "Please send me some REST"]]))

(defn post-record [{:keys [body] :as request}]
  (println "Received a record")
  (swap! records conj (slurp body))
  {:status 200})

(def routes
  (compojure/routes
    (GET "/" [] (welcome))
    (compojure/context "/records" []
      (POST "/" request (post-record request))
      (GET "/gender" [] (pr-str @records))
      (GET "/birthdate" [] (pr-str @records))
      (GET "/name" [] (pr-str @records)))
    (route/not-found "Not found")))

(def handler
  (handler/api #'routes))
