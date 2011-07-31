
    musician_inst(a, guitar).
    musician_inst(b, drum).
    musician_inst(c, piano).
    musician_inst(d, guitar).
    musician_inst(e, guitar).
    musician_inst(f, bass).
    musician_inst(g, violin).

    musician_style(a, rock).
    musician_style(b, rock).
    musician_style(c, classical).
    musician_style(d, pop).
    musician_style(e, rock).
    musician_style(f, rock).
    musician_style(g, classical).

    instrument_by_style(X, Y) :- musician_style(Z, Y), musician_inst(Z, X).
