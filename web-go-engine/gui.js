function _2d(x, y) {
    return {
        x: x,
        y: y
    };
}

function Shape2D(x, y) {
    this.pt = _2d(x, y);
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

function Goishi() {}

Goishi.prototype = new Shape2D();

Goishi.prototype.render = function(canvas) {}

function Bowl(player, pieceCls) {
    this.pieceCls = pieceCls;
    this.player = player;
    this.id = IdManager.get().genId();

    this.inside = [];
    this.outside = [];
}

Bowl.prototype = {

    constructor: Bowl,

    get: function() { //lazy get
        if (this.inside.length == 0) {
            this.inside.push(_piece());
        }
        var piece = this.inside.pop();
        this.outside.push(piece);
        return piece;
    },

    put: function(piece) {
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

function GuiBoard(
boardRowColumns,
rectangleProportion,
totalWidth,
totalHeight,
boardModel) {

    this.dimension = boardRowColumns;
    this.rectangleProportion = rectangleProportion;

    this.height = totalHeight;
    this.width = this.height * this.rectangleProportion;

    this.cellWidth = this.width / (this.dimension - 1);
    this.cellHeight = this.height / (this.dimension - 1);

    this.model = boardModel;

}

GuiBoard.prototype = {
    constructor: GuiBoard,

    renderBoard: function(canvasCtx) {
        canvasCtx.moveTo(0, 0);
        canvasCtx.lineTo(0, this.height);
        canvasCtx.lineTo(this.width, this.height);
        canvasCtx.lineTo(this.width, 0);
        canvasCtx.lineTo(0, 0);

        for (var i = 0; i < this.dimension; i++) {
            var x = i * this.cellWidth;
            canvasCtx.moveTo(x, 0);
            canvasCtx.lineTo(x, this.height);

            var y = i * this.cellHeight;
            canvasCtx.moveTo(0, y);
            canvasCtx.lineTo(this.width, y);
        }
        canvasCtx.stroke();

        this.renderCircle(canvasCtx, 3, 3, 5, "black");
        this.renderCircle(canvasCtx, 19 - 4, 19 - 4, 5, "black");
        this.renderCircle(canvasCtx, 3, 19 - 4, 5, "black");
        this.renderCircle(canvasCtx, 19 - 4, 3, 5, "black");
        this.renderCircle(canvasCtx, 9, 9, 5, "black");
        this.renderCircle(canvasCtx, 9, 3, 5, "black");
        this.renderCircle(canvasCtx, 3, 9, 5, "black");
        this.renderCircle(canvasCtx, 19 - 10, 19 - 4, 5, "black");
        this.renderCircle(canvasCtx, 19 - 4, 19 - 10, 5, "black");
    },

    getPieceColor: function(piece) {
        return piece == 0 ? "black" : "white";
    },

    renderCircle: function(canvasCtx, x, y, size, color) {
        canvasCtx.beginPath();
        canvasCtx.arc(x * this.cellWidth, y * this.cellHeight, size, 0, Math.PI * 2);
        canvasCtx.stroke();
        canvasCtx.fillStyle = color;
        canvasCtx.fill();
        canvasCtx.closePath();
    },

    render: function(canvasCtx) {
        this.renderBoard(canvasCtx);

        for (var i = 0; i < this.dimension; i++) {
            for (var j = 0; j < this.dimension; j++) {
                var piece = this.model.board[i][j];
                if (piece == -1) continue;

                var size = this.cellHeight / 2.2;
                this.renderCircle(canvasCtx, i, j, size, this.getPieceColor(piece));
            }
        }

    },

    install: function(el) {
        this.elements.push(el);
    },
}
