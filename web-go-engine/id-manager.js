"use strict";

function IdManager() {
    this.nullCategory = {};
    this.idMap = {};
}

IdManager.prototype = {
    constructor: IdManager,

    genId: function(catName) {
        var catMap = this.category(catName);
        if (!catMap.last) {
            catMap.last = Number.MIN_VALUE;
        } else {
            catMap.last++;
        }
        return catMap.last;
    },
    genStrId: function(catName) {
        return String(this.genId(catName));
    },

    category: function(cat) {
        if (!cat) {
            return this.nullCategory;
        }
        var c = this.idMap[cat];
        if (!c) {
            c = {};
            this.idMap[cat] = c;
        }
        return c;
    }
};
IdManager.get = function() {
    if (!IdManager.instance) {
        IdManager.instance = new IdManager();
    }
    return IdManager.instance;
};
