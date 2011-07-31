var requestAnimationFrame =
    requestAnimationFrame ||
    webkitRequestAnimationFrame ||
    mozRequestAnimationFrame ||
    msRequestAnimationFrame ||
    oRequestAnimationFrame;

function Game(engine2d, inputEngine, stats) {
    this._engine2d = engine2d;
    this._inputEngine = inputEngine;
    this._stats = stats;
    this._entities = [];

    var player = new Player(50, 50);

    this._entities.push(player);
    this._entities.push(new Minion(200, 200));
    this._entities.push(new Minion(270, 270));
    this._entities.push(new Minion(300, 300));

    this._setup();

    this._lastTick = new Date().getTime();
    this._tick = this._lastTick;
}

Game.prototype._setup = function() {
    this._entities.forEach(function(o) { o.bindActions(this._inputEngine)}.bind(this));

    this._physicsEngine = new PhysicsEngine(new CollisionDetector(), new CollisionSolver(), new PhysicsWorld({
        squareSize: 20,
        width: 1000,
        height: 500
    }));

    for (var i = 0, l = this._entities.length; i < l; i ++) {
      var e = this._entities[i];
      this._physicsEngine.addEntity(e.physicsEntity);
    }
};

Game.prototype.play = function() {
    var that = this;
    requestAnimationFrame(function() {
        that._profileStep();
        that.play();
    });
};

Game.prototype._profileStep = function () {
    this._stats.begin();
    this._lastTick = this._tick;
    this._tick = new Date().getTime();
    this._step(this._tick - this._lastTick);
    this._stats.end();
};

Game.prototype._processActions = function() {
    var a = null;
    while (a = this._inputEngine.consume()) {
        for(var i = 0; i < this._entities.length; i++) {
            this._entities[i].handleAction(a);
        }
    }
};

Game.prototype._step = function (elapsed) {
    this._engine2d.clear();
    this._processActions();

    this._physicsEngine.step(elapsed);
    this._entities.forEach(function(o){ o.draw(this._engine2d); }.bind(this));
};
