    function rad(degrees) {
        var oneDegree = Math.PI / 180;
        return oneDegree * degrees;
    }

    function deg(radians) {
        var oneRad = 180;
        return 180 * radians / Math.PI;
    }

    function Engine2d(canvas2d) {
        this._canvas2d = canvas2d;
        this._precision = 30;
    }

    Engine2d.prototype.drawCircle = function(x, y, radius, color) {
        this._canvas2d.fillStyle = color;
        this._canvas2d.beginPath();
        this._canvas2d.arc(x, y, radius, 0, rad(360), true);
        this._canvas2d.closePath();
        this._canvas2d.fill();
    };

    Engine2d.prototype.drawRect = function(x, y, width, height, color) {
        this._canvas2d.fillStyle = color;
        this._canvas2d.beginPath();
        this._canvas2d.rect(x, y, width, height);
        this._canvas2d.closePath();
        this._canvas2d.stroke();
    };

    Engine2d.prototype.drawLine = function(x0, y0, x1, y1, color) {
        this._canvas2d.fillStyle = color;
        this._canvas2d.lineWidth = 5;
        this._canvas2d.beginPath();
        this._canvas2d.moveTo(x0, y0);
        this._canvas2d.lineTo(x1, y1);
        this._canvas2d.closePath();
        this._canvas2d.stroke();
    };

    Engine2d.prototype.clear = function() {
        var canvasEl = $(this._canvas2d.canvas);
        this._canvas2d.beginPath();
        this._canvas2d.save();
        this._canvas2d.setTransform(1, 0, 0, 1, 0, 0);
        this._canvas2d.clearRect(0, 0, canvasEl.width(), canvasEl.height());
        this._canvas2d.restore();
    };
