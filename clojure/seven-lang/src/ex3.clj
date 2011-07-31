
    (ns ex3 
        (:gen-class))

    (defn newMap 
        []
        (atom {}))

    (defn get2
        [cache key]
        (@cache key))

    (defn put2
        ([cache value-map]
            (swap! cache merge value-map))
        ([cache key value]
            (swap! cache assoc key value)))

    (defprotocol Acc 
        (credit [this value])
        (debit [this value]))

    (defrecord Account [balance]
        Acc
        (credit [this value] 
            (reset! balance (+ @balance value)))
        (debit [this value] 
            (reset! balance (- @balance value))))

    (def t (Account. (atom 0)))
    (println t)
    (credit t 5)
    (println t)
    (credit t 5)
    (credit t 5)
    (credit t 5)
    (println t)
    (debit t 10)
    (println t)

