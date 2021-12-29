"use strict";

var BoardTests = {
    isSuite: true,
    test001: function() {
        var board = new BoardModel(10);
        var piece = 1;

        for (var i = 0; i < 10; i++) {
            for (var j = 0; j < 10; j++) {
                board.putPiece(piece, i, j);
            }
        }

        for (var i = 0; i < board.board.length; i++) {
            for (var j = 0; j < board.board[i].length; j++) {
                assertEquals(board.board[i][j], 1);
            }
        }

    },
    test002: function() {
        var board = new BoardModel(10);
        var piece = 1;

        var error = false;
        try {
            for (var i = 0; i < 11; i++) {
                for (var j = 0; j < 11; j++) {
                    board.putPiece(piece, i, j);
                }
            }

            for (var i = 0; i < board.board.length; i++) {
                for (var j = 0; j < board.board[i].length; j++) {
                    assertEquals(board.board[i][j], 1);
                }
            }
        } catch (e) {
            error = true;
        }
        if (!error) {
            throw new Error("Should throw an exception of boundaries check, but none was thrown");
        }
    },
    test003: function() {
        var board = new BoardModel(10);
        var piece = 1;

        for (var i = 0; i < 10; i++) {
            for (var j = 0; j < 10; j++) {
                board.putPiece(piece, i, j);
            }
        }

        board.clear();

        for (var i = 0; i < board.board.length; i++) {
            for (var j = 0; j < board.board[i].length; j++) {
                assertEquals(board.board[i][j], -1);
            }
        }
    },


    test004: function() {
        var board = new BoardModel(10);
        var piece = 1;

        for (var i = 0; i < 10; i++) {
            for (var j = 0; j < 1; j++) {
                board.putPiece(piece, i, j);
            }
        }

        for (var i = 0; i < board.board.length; i++) {
            for (var j = 0; j < 1; j++) {
                assertEquals(board.board[i][j], piece);
            }
        }

        for (var i = 0; i < board.board.length; i++) {
            for (var j = 0; j < 1; j++) {
                board.removePiece(i, j);
            }
        }

        for (var i = 0; i < board.board.length; i++) {
            for (var j = 0; j < board.board[i].length; j++) {
                assertEquals(board.board[i][j], -1);
            }
        }
    }
};

//registra os testes
(function() {
    TestControl.get().registerSuite(BoardTests);
})();
