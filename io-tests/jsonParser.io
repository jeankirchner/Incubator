
    OperatorTable addAssignOperator(":" , "xla");

    Map xla := method(
        self atPut(
            call evalArgAt(0),
            call evalArgAt(1)
        )
    );

    Object curlyBrackets := method (
        r := Map clone;
        call message arguments foreach(arg,
            r doMessage(arg);
        );
        return (r);
    );

    x := {
        "ad" : "adsf",
        1 : 2
    };
