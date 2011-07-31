
    smt(0).
    grt(1).

    assign(X, X).
    assign(X, _) :- assign(X, X).

    append_if(N, List, How, Than, R) :- 
        smt(How), 
        (N < Than -> append( [N], List, R); assign(List, R)).

    append_if(N, List, How, Than, R) :- 
        grt(How),
        (N > Than -> append( [N], List, R); assign(List, R)).

    filter([], _, _, []).
    filter([Head|Tail], How, Than, R) :- 
        filter(Tail, How, Than, R2), append_if(Head, R2, How, Than, R).

    custom_sort([], Ordered) :- assign([], Ordered).
    custom_sort([Head|Tail], Ordered) :- 
        filter(Tail, 0, Head, Smt), 
        filter(Tail, 1, Head, Grt), 
        custom_sort(Smt, Osmt), 
        custom_sort(Grt, Ogrt), 
        append(Osmt, [Head], Buff), 
        append(Buff, Ogrt, Ordered).

