
    (ns cache
        (:gen-class))

    (defn create
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

    (def ac (create))
    (put2 ac :quote "Yeah I am your father dude")
    (println (str "cached item: " (get2 ac :quote)))

