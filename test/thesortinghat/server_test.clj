(ns thesortinghat.server-test
  (:require
    [clojure.test :refer :all]
    [thesortinghat.server :as server]
    [clojure.string :as string]
    [ring.mock.request :as mock]
    [cheshire.core :as json]))

(deftest handler-test
  (is (= 200 (:status (server/handler (mock/request :get "/" nil)))))
  (with-out-str
    (is (= 200 (:status (server/handler (mock/request :post "/records"
                                                      "Potter,Harry,male,green,1/1/1985"))))
        "should accept records in the body")
    (is (= 200 (:status (server/handler (mock/request :post "/records"
                                                      {:record "Potter,Harry,male,green,1/1/1985"}))))
        "should accept form parameter 'record' as well for HTML form convenience"))
  (is (= 200 (:status (server/handler (mock/request :post "/load")))))
  (is (= 400 (:status (server/handler (mock/request :post "/records"
                                                    "bad,test,record")))))
  (is (= 200 (:status (server/handler (mock/request :get "/records/gender")))))
  (is (= 200 (:status (server/handler (mock/request :get "/records/birthdate")))))
  (is (= 200 (:status (server/handler (mock/request :get "/records/name")))))
  (is (= 404 (:status (server/handler (mock/request :post "/invalid-url")))))
  (is (= 404 (:status (server/handler (mock/request :get "/invalid-url"))))))

(defn handle-mock-record [& args]
  (server/handler
    (mock/request :post "/records"
                  (string/join "," args))))

(deftest stateful-test
  (testing "Received two records about Harry, and one about Hermione..."
    (with-out-str
      (handle-mock-record "Potter" "Harry" "male" "blue" "1/1/1985")
      (handle-mock-record "Potter" "Harry" "male" "green" "1/1/1985")
      (handle-mock-record "Granger" "Hermione" "female" "red" "3/1/1985"))
    (is (= [{"last-name" "Potter",
            "first-name" "Harry",
            "date-of-birth" "1/1/1985",
            "gender" "male",
            "favorite-color" "green"}
           {"last-name" "Granger",
            "first-name" "Hermione",
            "date-of-birth" "3/1/1985",
            "gender" "female",
            "favorite-color" "red"}]
          (json/parse-string
             (:body (server/handler (mock/request :get "/records/name")))))
        "should be only two records (one for Harry, one for Hermione) returned in JSON in the correct order")))
