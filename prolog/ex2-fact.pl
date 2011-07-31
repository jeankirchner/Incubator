
    fact(0, 1).
    fact(1, 1).
    fact(N, Fact) :- N_1 is N-1, fact(N_1, Rec), Fact is N * Rec.
