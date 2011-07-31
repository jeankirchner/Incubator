
    function Board(dimension){
        this.empty = -1;
        this.board = []; 
        for (var i = 0; i < dimension; i++) {
            var line = [];
            for (var j = 0; j < dimension; j++) {
                line.push(this.empty);
            }
            this.board.push(line);
        }
    }

    Board.prototype = {

        constructor: Board,
         
        //do not verify dimensions, let array do his job doing this
        putPiece: function (piece, x, y) {
            this.board[x][y] = piece;
        },

        removePiece: function(x, y) {
            this.board[x][y] = this.empty;
        },

        clear: function() {
            for (var i = 0; i < this.board.length; i++) {
                for (var j = 0; j < this.board.length; j++) {
                    this.board[i][j] = this.empty;
                }
            }
        }

    };

    function BoardTree(board) {
        this.boardState = board;

        this.className = "BoardTree";
        this.id = IdManager.get().genId(this.className);

        this.root = {
            parentId: this.id
        };
        this.activeNode = this.root;    
    }

    BoardTree.prototype = {
        constructor: BoardTree,
         
        play: function(x, y, piece) {
            var mv = this.move(x,y);
            /* 
             * TODO:
             * if already exists, and piece is different, should throw an exception?
             * there is a way of piece be different?
             */
            mv.piece = piece;
            mv.parent = this.activeNode;
            this.activeNode = mv;
            this.board.putPiece(x, y, piece);
        },

        move: function(x,y) {
            var k = this.key(x,y);
            var mv = this.activeNode[k];
            if (!mv) {
                mv = {};
                this.activeNode[k] = mv;
            }
            return mv;
        },

        key: function(x, y) {
            return x + '-' + y;
        },

        activateMove: function(move) {
            //TODO: Validations like these should be just in development?
            if (!move) {
                throw new Error("Move can't be null");
            }
            if (move === this.activeNode) {
                return;
            } 
            var path = this.buildPath(this.activeNode, move);
            
            for (var i = 0; i < path.rollback.length; i++) {
                var mv = path.rollback[i];
                this.board.removePiece(mv.x, mv.y);
            }

            for (var i = 0; i < path.movements.length; i++) {
                var mv = path.movements[i];
                this.board.putPiece(mv.x, mv.y, mv.piece);
            }
            
        },

        buildPath: function(node1, node2) {
            this.validateNode(node1);
            this.validateNode(node2);

            var buildNodePath = function(nd) {
                var path = [];
                while (nd) {
                    path.push(nd);
                    nd = nd.parent;
                }
                return path;
            };

            var path1 = buildNodePath(node1);
            var path2 = buildNodePath(node2);

            var last = null;

            while (path1[i] == path2[i]) {
                last = path1[i]; 
            }

            var result = {
                rollback: [],   
                movements: []
            };
            
            
            return result;
        },

        //TODO: This verification is necessary?
        validateNode: function(nd) {
            if (nd.parentId != this.id) {
                throw new Error("This movement doesn't belong to this board");
            }
        }

    };

