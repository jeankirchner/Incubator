
    rev([], []).
    rev([Head|Tail], Rev) :- rev(Tail, Rev2), append(Rev2, [Head], Rev).

