(ns crtb.test-async
  (:require
   [http.async.client :as ac]
   [clojure.data.json :as json]))

(defn test []
  (with-open [client (ac/create-client)] ; Create client
    (let [response (ac/GET client "http://stream.twitter.com/1/statuses/sample.json")] ; request http resource
      (println response)
      (ac/await response)
      (println response)
      (println "done: " @(:done response))
      (println "error: " @(:error response))
      (println @(:status response))
      (println (ac/string response)) ; read body of response as string
      )))

(def u "rhliza")
(def p "thisisme")

(defn print-user-and-text [tw]
  (let [twit (json/read-json tw)
        user (:screen_name (:user twit))
        text (:text twit)]
    (println user "=>" text)))

(defn test2 []
  (with-open [client (ac/create-client)]
    (doseq [twit-str (ac/string
                      (ac/stream-seq client
                                     :get "http://stream.twitter.com/1/statuses/sample.json"
                                     :auth {:user u :password p}))]
      (print-user-and-text twit-str))))

