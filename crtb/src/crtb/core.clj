(ns crtb.core)
  (:use
   [twitter.oauth]
   [twitter.callbacks]
   [twitter.callbacks.handlers]
   [twitter.api.restful])
  (:import
   (twitter.callbacks.protocols SyncSingleCallback))

(def *app-consumer-key*         (System/getenv "RHLIZA_CONSUMER_KEY"))
(def *app-consumer-secret*      (System/getenv "RHLIZA_CONSUMER_SECRET"))
(def *user-access-token*        (System/getenv "RHLIZA_ACCESS_TOKEN"))
(def *user-access-token-secret* (System/getenv "RHLIZA_ACCESS_TOKEN_SECRET"))

(def my-creds (make-oauth-creds *app-consumer-key*
                                *app-consumer-secret*
                                *user-access-token*
                                *user-access-token-secret*))

; simply retrieves the user, authenticating with the above credentials
; note that anything in the :params map gets the -'s converted to _'s
(users-show :oauth-creds my-creds :params {:screen-name "rhliza"})

; supplying a custom header
; (users-show :oauth-creds my-creds :params {:screen-name "AdamJWynne"} :headers {:x-blah-blah "value"})

; shows the users friends
(friendships-show :oauth-creds my-creds 
                  :params {:target-screen-name "rhliza"})

; use a custom callback function that only returns the body of the response
(friendships-show :oauth-creds my-creds
                  :callbacks (SyncSingleCallback. response-return-body 
                                                  response-throw-error
                                                  exception-rethrow)      
          :params {:target-screen-name "rhliza"})

; upload a picture tweet with a text status attached, using the default sync-single callback
;(statuses-update-with-media :oauth-creds *creds*
;                            :body [(file-body-part "/pics/test.jpg")
;                                  (status-body-part "testing")])
