(ns crtb.core
  (:require
   [twitter.api :as tw]
   [twitter.oauth :as oauth]
   [twitter.callbacks :as cb]
   [twitter.callbacks.handlers :as handlers]
   [twitter.api.restful :as restful]
   [twitter.api.streaming :as streaming]
   [clojure.data.json :as json]
   [http.async.client :as ac])
  (:import
   (twitter.callbacks.protocols SyncSingleCallback)
   (twitter.callbacks.protocols AsyncStreamingCallback)))

(def ^:dynamic *app-consumer-key*         (System/getenv "RHLIZA_CONSUMER_KEY"))
(def ^:dynamic *app-consumer-secret*      (System/getenv "RHLIZA_CONSUMER_SECRET"))
(def ^:dynamic *user-access-token*        (System/getenv "RHLIZA_ACCESS_TOKEN"))
(def ^:dynamic *user-access-token-secret* (System/getenv "RHLIZA_ACCESS_TOKEN_SECRET"))

(def my-creds (oauth/make-oauth-creds *app-consumer-key*
                                      *app-consumer-secret*
                                      *user-access-token*
                                      *user-access-token-secret*))

(def bad-creds (oauth/make-oauth-creds *app-consumer-key*
                                       *app-consumer-key*
                                       *user-access-token*
                                       *user-access-token-secret*))

(defn rest-tests []

  ;; simply retrieves the user, authenticating with the above credentials
  ;; note that anything in the :params map gets the -'s converted to _'s
  (println (restful/users-show :oauth-creds bad-creds :params {:screen-name "rhliza"}))
  
  ;; supplying a custom header
  ;; (users-show :oauth-creds my-creds :params {:screen-name "AdamJWynne"} :headers {:x-blah-blah "value"})
                                        ; shows the users friends
  
  (println (restful/friendships-show :oauth-creds my-creds 
                                     :params {:target-screen-name "rhliza"}))
  
  ;; use a custom callback function that only returns the body of the response
  #_(tw/friendships-show :oauth-creds my-creds
                         :callbacks (SyncSingleCallback. response-return-body 
                                                         response-throw-error
                                                         exception-rethrow)      
                         :params {:target-screen-name "rhliza"})
  
  ;; upload a picture tweet with a text status attached, using the default sync-single callback
  ;;(statuses-update-with-media :oauth-creds *creds*
  ;;                            :body [(file-body-part "/pics/test.jpg")
  ;;                                  (status-body-part "testing")])
  )

(defn custom-streaming-callback []
  (AsyncStreamingCallback.
   (comp println #(:text %) json/read-json #(str %2))
   (comp println handlers/response-return-everything)
   handlers/exception-print))

(defn counting-callback []
  (AsyncStreamingCallback.
   #(println "got some body")
   #(println "got an error")
   #(println "got an exception")))

(def ^:dynamic *strm* nil)

(defn stream-test1 []
  (binding [*strm* (streaming/user-stream
                    :oauth-creds nil ;my-creds
                                        ;:callbacks (custom-streaming-callback)
                    :params {:with "user" :replies "all"})]
    (println (meta *strm*))
    (Thread/sleep (* 20 1000))
    ((:cancel (meta *strm*)))))

(defn stream-test2 []
  (binding [*strm* (streaming/statuses-sample :oauth-creds my-creds
                                              ;:callbacks
;(custom-streaming-callback)
                                              :callbacks (counting-callback)
                                              )
            ]
    (println (meta *strm*))
    (Thread/sleep (* 3 60 1000))
    ((:cancel (meta *strm*)))))

