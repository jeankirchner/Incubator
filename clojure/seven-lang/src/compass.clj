
    (ns compass
        (:gen-class))

    (def directions [:north :east :south :west])

    (defprotocol Compass
        (direction [c])
        (left [c])
        (right [c]))

    (defn turn 
        [base amount]
        (rem (+ base amount) (count directions)))

    (defrecord SimpleCompass [bearing]
        Compass
        (direction [_] (directions bearing))
        (left [_] (SimpleCompass. (turn bearing 3)))
        (right [_] (SimpleCompass. (turn bearing 1)))
        (toString [this] (str "[" (direction this) "]" )))

    (println (def c (SimpleCompass. 0)))
    (println (left c))
    (println (right c))
    (println c)

