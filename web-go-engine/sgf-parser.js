"use strict"

function emptyFn() {
}

/*
 * Stream class
 * Some stream utilities
 */
function Stream(input) {
    this.input = input;
    this.position= 0;
}

Stream.prototype = {

    constructor: Stream,

    reset: function(){
        this.position = 0;
    },

    savePoint: function() {
        return this.position;
    },

    rollback: function(savePoint) {
        this.position = savePoint;
    },

    ch: function(){
        if (this.eof()) throw new Error('End of File');
        return this.input[this.position++];
    },

    eof: function() {
        return this.position >= this.input.length;
    }

}


function GoBuilder(){
}

GoBuilder.prototype = {
    constructor: GoBuilder,

    build: emptyFn,

    buildBranch: emptyFn,

    buildNode: emptyFn,

    buildNodeProp: emptyFn
}

/*
 * Split text as sgf tokens
 */
function SGFReader(stream, builder) {
    this.stream = stream;
    //this.builder = builder;
}

SGFReader.prototype = {

    constructor: SGFReader,

    run: function() {

        var branches = [];

        while (this.hasBranch()) {
            branches.push(this.branch());
        }

        this.ignoreWhiteSpaces();

        if (!this.stream.eof()) {
            throw new Error("Since it ends, it should be in the end of stream");
        }

        //this.builder.build(branches);
    },

    branch: function() {

        this.ignoreWhiteSpaces();

        var c = this.stream.ch();
        if (c != '(') {
            throw new Error('Error at position: ' + this.stream.position + ' expected: \'(\'');
        }

        var nodes = [];
        nodes.push(this.node());

        while (this.hasNode()) {
            nodes.push(this.node());
        }

        this.ignoreWhiteSpaces();

        c = this.stream.ch();

        if (c != ')') {
            throw new Error('Error at position: ' + this.stream.position + ' expected: \')\'');
        }

        //this.builder.buildBranch(nodes);

        return nodes;

    },

    hasBranch: function() {

        if (this.stream.eof()) return false;

        this.ignoreWhiteSpaces();

        if (this.stream.eof()) return false;

        var svPt = this.stream.savePoint();
        var result = this.stream.ch() == '(';
        this.stream.rollback(svPt);
        return result;
    },

    node: function() {
        this.ignoreWhiteSpaces();

        if (this.stream.ch() != ';') {
            throw new Error('Error at position: ' + this.stream.position + ' expected: \';\'');
        };

        var props = [];

        while (this.hasNodeProp()) {
            props.push(this.nodeProp());
        }
        var subBranches = [];

        while (this.hasBranch()) {
            subBranches.push(this.branch());
        }

        //return this.builder.buildNode(props, subBranches);
    },

    hasNode: function() {
        this.ignoreWhiteSpaces();

        var svPt = this.stream.savePoint();

        var result = this.stream.ch() == ';';

        this.stream.rollback(svPt);
        return result;
    },

    nodeProp: function() {
        this.ignoreWhiteSpaces();

        var name = this.nodePropName();
        var value = this.nodePropValue();

        //return this.builder.buildNodeProp(name, value);
    },

    isUcChar: function(c) {
        var min = 'A'.charCodeAt(0);
        var max = 'Z'.charCodeAt(0);
        c = c.charCodeAt(0);

        return c >= min && c <= max;
    },

    hasNodeProp: function() {
        this.ignoreWhiteSpaces();

        var svPt = this.stream.savePoint();
        var result = this.isUcChar(this.stream.ch());
        this.stream.rollback(svPt);
        return result;
    },

    nodePropName: function() {
        this.ignoreWhiteSpaces();

        var name = [];
        var c = this.stream.ch();
        var svPt = this.stream.savePoint();

        while (this.isUcChar(c)) {
            name.push(c);
            svPt = this.stream.savePoint();
            c = this.stream.ch();
        }
        this.stream.rollback(svPt);

        return name.join('');
    },

    nodePropValue: function() {
        var values = [];
        values.push(this.propSingleValue());

        if (this.hasPropSingleValue()) {
            do {
                values.push(this.propSingleValue());
            } while (this.hasPropSingleValue());
        }
        return values;
    },

    propSingleValue: function() {
        this.ignoreWhiteSpaces();

        if (this.stream.ch() != '[')  {
            throw new Error('Error at position: ' + this.stream.position + ' expected: \'[\'');
        }

        var value = [];
        var c = this.stream.ch();
        var backSlash = false;

        function canContinue() {
            return backSlash || (c != ']');
        }

        while (canContinue()) {
            value.push(c);
            backSlash = c == '\\';
            c = this.stream.ch();
        }
        
        if (c != ']')  {
            throw new Error('Error at position: ' + this.stream.position + ' expected: \']\'');
        }

        //this.builder.nodePropValue(value.join(''));
    },

    hasPropSingleValue: function() {
        this.ignoreWhiteSpaces();
        var svPt = this.stream.savePoint();
        var result = this.stream.ch() == '[';
        this.stream.rollback(svPt);
        return result;
    },

    isWhiteSpace: function(c) {
        return /\s/.test(c);
    },

    ignoreWhiteSpaces: function() {
        if (this.stream.eof()) return;

        var svPt = this.stream.savePoint();
        var c = this.stream.ch();

        while (this.isWhiteSpace(c)) {
            svPt = this.stream.savePoint();

            if (this.stream.eof()) {
                break;
            }

            c = this.stream.ch();
        }

        this.stream.rollback(svPt);

    }
}

SGFCompiler = {
    compile: function(sgfText, builder) {
        var stream = new Stream(sgfText);
        var sgfReader = new SGFReader(stream, builder);
        sgfReader.run();
    }
}

function mainSGF(sgfText) {
    var time = new Date().getTime();
    SGFCompiler.compile(sgfText);
    time = new Date().getTime() - time;
    alert(time);
}

