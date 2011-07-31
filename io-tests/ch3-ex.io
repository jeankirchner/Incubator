#1

    Builder := Object clone;

    Builder forward := method(
        i := self getSlot("indent");
        if (i == nil, self indent := "";);

        x := self doMessage(call argAt(0));
        x println;

        process := false;

        if (x type == "BuillderProps",
            write(self indent, "<" , call message name);
            x foreach(key, value,
                write(" ", key, " ", "=", " ", value, " ");
            );
            writeln(">");
            , 
            writeln(self indent, "<" , call message name, ">" );
            process = true;
        );

        b := self indent;
        self indent = self indent .. "  ";

        call message arguments foreach(
            arg, index,

            if (process || ((process == false) and index > 0),
                content := self doMessage(arg);
                if (content type == "Sequence", 
                    writeln(self indent, "  ", content);
                )
            );
        );

        self indent = b;
        writeln(self indent, "</" , call message name, ">" )
    );

#2

    Object squareBrackets := method(
        l := List clone;
        call message arguments foreach(arg,
            l push (doMessage(arg));
        );
    );

#3


    BuilderProps := Map clone;

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

    {
        "ad" : "adsf",
        1 : 2
    };

