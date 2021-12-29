function GameStepNavigatorNode() {

    this.gameStepNode = gameStepNode;

    this.fromNode = null;
    this.activeVariation = null;

    this.variations = [];

}

GameStepNavigatorNode.prototype = {

    constructor: GameStepNavigatorNode,

    acceptCurrentNode: function(gameStepVisitor) {}

    acceptTree: function(gameStepVisitor) {}

}

function GameStepVisitor() {}

GameStepVisitor.prototype = {

    constructor: GameStepVisitor,

    //will need one for each tipe
    visit: function() {}

};

/*
 * Base class for state nodes on game
 */

function GameStepNode() {}

GameStepNode.prototype = {

    constructor: GameStepNode,

    accept: function(gameStepVisitor) {}

};

GoStepNode = extends(GameStepNode, {

    constructor: function() {},

});

/*
 * Decorator on StepNodes to add functionality on nodes
 * This way, each kind of StepNode can have just one responsability
 */
DecoratorStepNode = extends(GameStepNode, {

    //TODO: Not sure now, but constructor should be the base function for class?
    constructor: function() {},

});


//TODO: The classes bellow are not ok yet, just ideas of what an decorator could be
/*
 * Setup positions on the board
 * Go specific game?
 */
SetupStepNode = extends(DecoratorStepNode, {

    constructor: function() {},

});

/*
 * Dinamic and root configurations on game like:
 * board size
 * byo yomi
 * game time
 *
 * dinamic adding time for some player
 */
CfgStepNode = extends(DecoratorStepNode, {
    constructor: function() {}
});

/*
 * For graphic visualization of indications, signals on game.
 * Good for reviews (TODO: probably the only one use of this)
 */
MarkStepNode = extends(DecoratorStepNode, {
    constructor: function() {}
});

/*
 * Things like comments on game, etc (TODO: what is this etc?)
 */
MiscelaneuousStepNode = extends(DecoratorStepNode, {
    constructor: function() {}
});
