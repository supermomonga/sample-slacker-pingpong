(ns sample-slacker-pingpong.core
  (:require [clojure.string :refer [lower-case]]
            [slacker.client :refer [emit! await! handle with-stacktrace-log]]
            [clojure.tools.logging :as log]
            [environ.core :refer [env]]))

(defn print-args
  "echo argument"
  [type & args]
  (println (str type
                " args: "
                (if (nil? args) "nil" args))))

(defn -main
  [& args]
  (handle :hello (partial print-args "hello"))
  (handle :message (partial print-args "message"))
  (handle :user-typng (partial print-args "user-typing"))
  (handle :pin-added (partial print-args "pin-added"))
  (handle :pin-removed (partial print-args "pin-removed"))
  (if-let [api-token (env :slack-api-token)]
    (do
      (log/info "API token found.")
      (emit! :slacker.client/connect-bot api-token)
      (await! :slacker.client/bot-disconnected))
    (log/error "You need to set environment variable `SLACK_API_TOKEN`.")))
