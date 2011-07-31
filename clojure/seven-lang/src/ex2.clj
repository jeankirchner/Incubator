    
    (ns ex2
        (:gen-class))

    ;macro unless with else
    (defmacro unless [condition body bodyElse]
        (list 'if(list 'not condition) body bodyElse))


    ;here shows the generated code from calling a macro
    ;(println (macroexpand '(unless false (ok)(nok))))

    ;here is a simple test of unless with else
    ;(def going false)
    ;(unless going (println "stay at home") (println "you really went didn't ya?"))

    (defprotocol Behavior1
        (bev1 [this])
        (bev2 [this])
        (bev3 [this])
        (bev4 [this]))

    (defrecord Yeah [yourYeah]
        Behavior1
        (bev1 [this] (println "bev1"))
        (bev2 [this] (println "bev2"))
        (bev3 [this] (println "bev3"))
        (bev4 [this] (println "bev4")))

    (def obj (Yeah. "ohhhhhhhhhhhhhh yeahhhh"))
    (println (.bev4 obj))

