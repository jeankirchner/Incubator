var defineProperties = function(object, map) {
    Object.keys(map).forEach(function(name) {
        var method = map[name];
        if (!object[name]) {
            Object.defineProperty(object, name, {
                configurable: true,
                enumerable: false,
                writable: true,
                value: method
            });
        }
    });
};

defineProperties(Math, {
    dst: function(x0, y0, x1, y1) {
        return Math.hypot(x0-x1, y0-y1);
    },
    hypot: function(x, y, z) {
        if (!z) { z = 0; }
        return Math.sqrt(Math.hypot2(x, y, z));
    },
    hypot2: function (x, y, z) {
        if (!x) { x = 0; }
        if (!y) { y = 0; }
        if (!z) { z = 0; }
        var xSq = x * x, ySq = y * y, zSq = z * z, inf = Infinity;
        if (xSq === inf || ySq === inf || zSq === inf) { return inf; }
        if (isNaN(xSq) || isNaN(ySq) || isNaN(zSq)) { return NaN; }
        return x * x + y * y + z * z || 0;
    }
});

defineProperties(Array.prototype, {
    remove: function(object) {
        var index = this.indexOf(object);
        if (index === -1) {
            return false;
        }
        this.splice(index, 1);
        return true;
    }, 
    each: function(fn) {
        for(var i = 0; i < this.length; i++) {
            fn(this[i]);
        }
    },
    map: function(fn) {
        for(var i = 0; i < this.length; i++) {
            this[i] = fn(this[i]);
        }
    }
});

