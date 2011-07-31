
    fib(0, 1).
    fib(1, 1).
    fib(2, 1).
    fib(N, Fib) :- N_1 is N-1, N_2 is N-2, fib(N_1, Rec1), fib(N_2, Rec2), Fib is Rec1 + Rec2.
