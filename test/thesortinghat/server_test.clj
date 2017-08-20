(ns thesortinghat.server-test
  (:require
    [clojure.test :refer :all]
    [thesortinghat.server :as server]
    [ring.mock.request :as mock]
    [ring.util.io :as io]))

(deftest handler-test
  (is (= 200 (:status (server/handler (mock/request :get "/" nil)))))
  (with-out-str
    (is (= 200 (:status (server/handler (assoc (mock/request :post "/records") :body (io/string-input-stream "Potter,Harry,male,green,1/1/1990")))))))
  (is (= 400 (:status (server/handler (assoc (mock/request :post "/records") :body (io/string-input-stream "bad,test,record"))))))
  (is (= 200 (:status (server/handler (mock/request :get "/records/gender" nil)))))
  (is (= 200 (:status (server/handler (mock/request :get "/records/birthdate" nil)))))
  (is (= 200 (:status (server/handler (mock/request :get "/records/name" nil)))))
  (is (= 404 (:status (server/handler (mock/request :post "/invalid-url" nil)))))
  (is (= 404 (:status (server/handler (mock/request :get "/invalid-url" nil))))))
