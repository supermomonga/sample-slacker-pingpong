(ns sample-slacker-pingpong.core
  (:require [clojure.string :refer [lower-case]]
            [slacker.client :refer [emit! handle with-stacktrace-log]]
            [clojure.tools.logging :as log]))

(defn ping-pong
  "ping-pong"
  [{:keys [channel text]}]
  (when (= text "ping")
    (emit! :slacker.client/send-message channel "pong")))

(defn -main
  [& args]
  (handle :message ping-pong)
  (if-let [api-token (System/getenv "SLACK_API_TOKEN")]
    (emit! :slacker.client/connect-bot )
    (log/error "You need to set environment variable `SLACK_API_TOKEN`.")))
