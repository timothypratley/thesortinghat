(ns thesortinghat.server-test
  (:require
    [clojure.test :refer :all]
    [thesortinghat.server :as server]
    [clojure.string :as string]
    [ring.mock.request :as mock]
    [ring.util.io :as io]))

(defn post [last-name first-name gender favorite-color birth-date]
  (server/handler (assoc (mock/request :post "/records")
                    :body (io/string-input-stream
                            (string/join "," [last-name first-name gender favorite-color birth-date])))))

(deftest handler-test
  (is (= 200 (:status (server/handler (mock/request :get "/" nil)))))
  (with-out-str
    (is (= 200 (:status (server/handler (assoc (mock/request :post "/records") :body (io/string-input-stream "Potter,Harry,male,green,1/1/1985")))))))
  (is (= 400 (:status (server/handler (assoc (mock/request :post "/records") :body (io/string-input-stream "bad,test,record"))))))
  (is (= 200 (:status (server/handler (mock/request :get "/records/gender")))))
  (is (= 200 (:status (server/handler (mock/request :get "/records/birthdate")))))
  (is (= 200 (:status (server/handler (mock/request :get "/records/name")))))
  (is (= 404 (:status (server/handler (mock/request :post "/invalid-url")))))
  (is (= 404 (:status (server/handler (mock/request :get "/invalid-url"))))))

(deftest stateful-test
  (testing "Received two records about Harry, and one about Hermione..."
    (with-out-str
      (post "Potter" "Harry" "male" "blue" "1/1/1985")
      (post "Potter" "Harry" "male" "green" "1/1/1985")
      (post "Granger" "Hermione" "female" "red" "3/1/1985"))
    (is (= "[{\"last-name\":\"Granger\",\"first-name\":\"Hermione\",\"date-of-birth\":\"3/1/1985\",\"gender\":\"female\",\"favorite-color\":\"red\"},{\"last-name\":\"Potter\",\"first-name\":\"Harry\",\"date-of-birth\":\"1/1/1985\",\"gender\":\"male\",\"favorite-color\":\"green\"}]"
           (:body (server/handler (mock/request :get "/records/name"))))
        "There is only two records (one for Harry, one for Hermione) returned in JSON in the correct order")))
