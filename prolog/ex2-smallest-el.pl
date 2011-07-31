
    assign(X, X).
    assign(X, _) :- assign(X, X).

    min2(X, _, T, Z) :- T < 0, assign(X, Z).
    min2(_, Y, T, Z) :- T > 0, assign(Y, Z).
    min2(X, _, T, Z) :- T = 0, assign(X, Z).
    
    min(X, Y, Z) :- T is X - Y, min2(X, Y, T, Z).
    max(X, Y, Z) :- T is Y - X, min2(X, Y, T, Z).

    empty([]).

    smallest_el([], false).
    smallest_el([Head|Tail], S) :- empty(Tail), S is Head.
    smallest_el([Head|Tail], S) :- \+empty(Tail), smallest_el(Tail, Rec), min(Rec, Head, S).

