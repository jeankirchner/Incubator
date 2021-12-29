function InputEngine(mouseElement) {
    this._mouseEvElement = mouseElement;

    this._keyboardBindings = {};
    this._mouseBindings = {};
    this._boundActions = {};

    this._mouse = {
        x : 0,
        y : 0
    };

    this._actionQueue = [];
    this._setup();
}

InputEngine.prototype._setup = function() {
    var that = this;
    var fnEvent = function(fn) {
        return function(e) {
            if (e.preventDefault) {
                e.preventDefault();
            }
            if (e.stopPropagation) {
                e.stopPropagation();
            }
            fn.call(that, e);
            return false;
        };
    };

    jQuery(this._mouseEvElement).on('contextmenu', function() { return false; });
    jQuery(this._mouseEvElement).on('mousemove', fnEvent(this._onMouseMove));
    jQuery(this._mouseEvElement).on('mousedown', fnEvent(this._onMouseDown));
    jQuery(this._mouseEvElement).on('mouseup', fnEvent(this._onMouseUp));
    jQuery(window).on('keydown', fnEvent(this._onKeyDown));
    jQuery(window).on('keyup', fnEvent(this._onKeyUp));
};

InputEngine.prototype._gatherMouseData = function(e) {
    var offset = jQuery(this._mouseEvElement).offset();
    this._mouse.x = e.pageX - offset.left;
    this._mouse.y = e.pageY - offset.top;
};

InputEngine.prototype._captureMouseDown = function(e) {
    var bindKey = this._getBindingKey(e, e.which);
    var pos = this.getMousePos();
    var action = this._mouseBindings[bindKey];
    if (action) {
        this._actionQueue.push({
            handlerId : action.handlerId,
            name : action.name,
            x : pos.x,
            y : pos.y
        });
    }
};

InputEngine.prototype._captureKeyDown = function(e) {
    var bindKey = this._getBindingKey(e, e.which);
    var action = this._keyboardBindings[bindKey];
    if (action) {
        this._actionQueue.push({
            handlerId : action.handlerId,
            name : action.name
        });
    }
};

InputEngine.prototype._onMouseDown = function(e) {
    this._gatherMouseData(e);
    this._captureMouseDown(e);
    this._capturingMouseDown = true;
};

InputEngine.prototype._onMouseUp = function(e) {
    this._gatherMouseData(e);
    this._capturingMouseDown = false;
};

InputEngine.prototype._onMouseMove = function(e) {
    this._gatherMouseData(e);
    if (this._capturingMouseDown) {
        this._captureMouseDown(e);
    }
};

InputEngine.prototype._onKeyDown = function(e) {
    this._captureKeyDown(e);
};

InputEngine.prototype._onKeyUp = function(e) {
    //Algo a se fazer no keyUp?
};

InputEngine.prototype._getBindingKey = function(modifiers, code) {
    return (!!modifiers.ctrlKey) + "_" +
           (!!modifiers.altKey) + "_" + 
           (!!modifiers.shiftKey) + "_" + 
           code;
};

InputEngine.prototype.bindKey = function(id, key, action, modifiers) {
    if (!this._boundActions[action]) {
        this._boundActions[action] = true;
        var bindKey = this._getBindingKey(modifiers, key);
        this._keyboardBindings[bindKey] = {
            handlerId : id,
            name : action
        };
    }
};

InputEngine.prototype.bindMouse = function(id, button, action, modifiers) {
    if (!this._boundActions[action]) {
        this._boundActions[action] = true;
        var bindKey = this._getBindingKey(modifiers, button);
        this._mouseBindings[bindKey] = {
            handlerId : id,
            name : action
        };
    }
};

InputEngine.prototype.consume = function() {
    return this._actionQueue.shift();
};

InputEngine.prototype.getMousePos = function() {
    return {
        x: this._mouse.x,
        y: this._mouse.y
    };
};

