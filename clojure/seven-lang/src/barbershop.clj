
    (ns barbershop
        (:gen-class))

    ;generates random miliseconds between 10-30

    (def barbershop-capacity 3)

    (defrecord Barbershop [waitingChairs, servedCustomers])

    (defn newBarbershop [] (Barbershop. (ref 0) (ref 0)))

    (defn newCustomer [barbershop] 
        (do 
            (println "notifying")
            (.notify barbershop)
            (dosync (alter (fn [a] a + 1) (:waitingChairs barbershop) )) ))

    (defn customerDone [barbershop] 
        (dosync (alter (fn [a] a + 1) (:servedCustomers barbershop))))
    
    (defn getCustomer [barbershop] 
        (if (> @(:waitingChairs barbershop) 0) 
            (dosync 
                (alter (fn [a] a - 1) (:waitingChairs barbershop) ))
            (do 
                (println "waiting")
                (locking barbershop (.wait barbershop))
                (Thread/sleep 1)
                (getCustomer barbershop))))

    (defn runBarber [barbershop]
        (do 
            (getCustomer barbershop) 
            (Thread/sleep 20)))

    (defn waitingTime [] 
        (Math/round (+ (mod (* (Math/random) 100) 20) 10)))

    (defn customerGen [barbershop] 
        (do 
            (println waitingTime)
            (Thread/sleep (waitingTime)) 
            (newCustomer barbershop)))

    (println (waitingTime))
    (def a (agent (newBarbershop)))
    (println a)
    (send a runBarber)
    (send a customerGen)
    (Thread/sleep 10000)
    (println a)

