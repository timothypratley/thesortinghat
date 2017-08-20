(ns thesortinghat.server-test
  (:require
    [clojure.test :refer :all]
    [thesortinghat.server :as server]
    [ring.mock.request :as mock]))

(deftest handler-test
  (is (= 200 (:status (server/handler (mock/request :get "/" nil)))))
  (is (= 200 (:status (server/handler (mock/request :post "/records" {:body "test,record"})))))
  (is (= 200 (:status (server/handler (mock/request :get "/records/gender" nil)))))
  (is (= 200 (:status (server/handler (mock/request :get "/records/birthdate" nil)))))
  (is (= 200 (:status (server/handler (mock/request :get "/records/name" nil)))))
  (is (= 404 (:status (server/handler (mock/request :post "/invalid-url" nil)))))
  (is (= 404 (:status (server/handler (mock/request :get "/invalid-url" nil))))))
