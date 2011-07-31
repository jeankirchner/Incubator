    function _2d(x, y){
        return {x: x, y:y};
    }
    
    function Shape2D (x, y) {
        this.pt = _2d(x,y);
        this.width = 0;
        this.height = 0;
    }

    Shape2D.prototype = {
        constructor: Shape2D,
        setWidth: function(width) {
            this.width = width;
        },
        setHeight: function(height) {
            this.height = height;
        },
        render: function(canvas) {
            throw new Error("This method is abstract");
        }
    };
    
    function Goishi() {
    }
    
    Goishi.prototype = new Shape2D();

    Goishi.prototype.render = function(canvas) {
    }

    function Bowl(player, pieceCls){
        this.pieceCls = pieceCls;
        this.player  = player;
        this.id = IdManager.get().genId();

        this.inside = [];
        this.outside = [];
    }

    Bowl.prototype = {

        constructor: Bowl,

        get: function(){ //lazy get
            if (this.inside.length == 0) {
                this.inside.push(_piece());
            }
            var piece = this.inside.pop();
            this.outside.push(piece);
            return piece;
        },

        put: function(piece){
            //TODO: Validations just in development
            if (!(piece instanceof this.pieceCls)) {
                throw new Error("This kind of piece is not for this game"); 
            }
            if (piece.ownerId != this.id) {
                throw new Error("This piece doesn't belong to this Bowl"); 
            }
            this.outside.remove(piece);
            this.inside.push(piece);
        },

        _piece: function() {
            //create for the piece class
            var piece = new this.pieceCls();
            piece.ownerId = this.id;
            return piece;
        }

    };

    //Turns out that GuiBoard must be an shape anyway
    function GuiBoard(dimension) {
        this.dimension = dimension;

        this.widthSpace = 22;
        this.heightSpace = 23;

        this.width = dimension * this.widthSpace;
        this.height = dimension * this.heightSpace;

        this.id = IdManager.get().genId();
        this.containers = [];
        this.canvas = null;

        this.board = [];
        for (var i = 0; i < dimension; i++) {
            var pts = [];
            for (var j = 0; j < dimension; j++) {
                pts.push(_2d(this.widthSpace*i, this.heightSpace*j));
            }
            this.board.push(pts);
        }

    }

    GuiBoard.prototype = {
        constructor: GuiBoard,

        installEvents: function() {
        },

        render: function(canvasCtx){

            for (var x = 0; x < this.dimension; x++){

                var xPos = x*this.widthSpace;
                var yPos = x*this.heightSpace;

                canvasCtx.moveTo(xPos, 0);
                canvasCtx.lineTo(xPos, this.width);
                canvasCtx.moveTo(0, yPos);
                canvasCtx.lineTo(this.height, yPos);

            }
            canvasCtx.stroke();
        },

        install: function(el) {
            this.elements.push(el);
        },

        

    };
