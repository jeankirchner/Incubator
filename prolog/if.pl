
    assign(X, X).
    assign(X, _) :- assign(X, X).

    if(Cond, X, Y, R) :- Cond -> R is X; R is Y.

