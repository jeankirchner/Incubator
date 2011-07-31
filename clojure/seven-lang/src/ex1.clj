
    (ns ex1
        (:gen-class))

    (defn isStrBigger [str n] 
            (> (count str) n))

    (defn getStructureType [in]
        (cond 
            (set? in) :set
            (map? in) :map
            (list? in) :list
            (vector? in) :vector))

    (println (getStructureType [1 2]))
    (println (getStructureType {1 2}))
    (println (getStructureType #{1 2}))
    (println (getStructureType '(1 2)))
    
