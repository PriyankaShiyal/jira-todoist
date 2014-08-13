(ns jira-todoist.updatedTicket
  (:require [jira-todoist.todoist :as todoist]))

(def interestingFields ["assignee" "description" "summary" "status"])

(defn dispatchField [ticket]
    (let [
          items (get-in ticket [:changelog :items])
          assignee (some (fn [item] (= (get item :field) "assignee")) items)
          ]

      (if assignee
        (todoist/assigneeChanged ticket)
        (todoist/contentChanged ticket)
      )))


(defn ticketValid? [ticket]
  (def items (get-in ticket [:changelog :items]))
  (def valid (some (fn [item] (contains? (set interestingFields) (get item :field))) items))
  valid)

(defn dispatchOnUpdate [ticket]
  "Dispatched on updated"
  (if (ticketValid? ticket)
    (dispatchField ticket)
    ("Invalid ticket")))
