
#1

    fib := method(n, 
        if (n <= 2, 1, fib(n-1) + fib(n-2))
    )

    fib_ := method(n, 

        if (n <= 1, 1,
            x := 1;
            y := 0;
            z := 1;

            n = n-2;

            while (n > 0, 

                n = n-1;

                y = z;
                z = x + z;
                x = y;

            );

            z;
        )
    )


#2

    Number setSlot("div", 
        Number getSlot("/")
    )
    Number setSlot("/",
        method(denominator, 
            if (denominator == 0, 
                return (denominator),

                #else
                return (
                    self div denominator
                )
            )
        )
    )

#3

    List deepSum := method(
        flt := self flatten();
        sum := 0
        flt foreach(index, value,
            if (value type == "Number", 
                sum = sum + value
            )
        )
        return (sum)
    )

#4

    List myAverage := method(
        l := self size;

        if (l == 0, return (nil));

        avg := 0;

        self foreach(index, value, 

            if (value type != "Number, 
                Exception raise ("no number", 
                    ("this: " .. value .. " was encountered during the " .. (call message name) .. " operation)
                );
            )
            avg := avg + value;
        )
        return (avg / l);
    )

#5

    BiDim := List clone
    BiDim dim := method(x, y,
        bid := BiDim clone;
        while (bid size < y, 
            bid push (
                l := List clone;
                l setSize(x);
                l;
            )
        )
        return(bid)
    )

    BiDim get := method(x, y, 
        l := self at(x);
        return (l at (y));
    )

    BiDim set := method(x, y, value,
        l := self at(x);
        l putAt(y, value)
    )

#6

    TransposedBiDim := BiDim clone
    TransposedBiDim setSlot("_get", TransposedBiDim getSlot("get"))
    TransposedBiDim setSlot("_set, TransposedBiDim getSlot("set))

    TransposedBiDim get := method(x, y, 
        self _get(y, x);
    )

    TransposedBiDim set := method(x, y, 
        self _set(y, x);
    )

    BiDim transpose := method(
        self appendProto (TransposeBiDim clone);
    )

#7

    BiDim writeToFile := method(fileName, 
        data := File open(fileName);
        data write (self serialized);
        data close;
    )
    
    BiDim readFromFile := method(fileName,
        return (doFile(fileName));
    )

#8

    guessNumberGame := method (

        tries := 10;
        number := ((Random value * 99) + 1) round;
        "Try to guess this number from 1-100" println;

        guess := ReadLine readLine asNumber;

        lastGap := Number integerMax;
        gap := (number - guess) abs;

        while ((guess != number) && (tries > 0),
            if (gap < lastGap,
                "hotter" println,
                "colder" println
            )
            tries = tries - 1;
        )

        if (tries == 0, ":(" println, ":)" println);

        return (nil);

    );

