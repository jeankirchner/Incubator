function MobileEntity(config) {
    config = config || {};
    config.width = config.width || config.radius || 20;
    config.height = config.height || config.radius || 20;

    this.physicsEntity = new PhysicsEntity({
        x : config.x || 0,
        y : config.y || 0,
        width : config.width,
        height : config.height,
        speed : config.speed/1000 || 0,
        isStatic : !!config.isStatic
    });
    this.color = config.color || "#999999";
}

MobileEntity.prototype.tick = function(elapsed) {
};

MobileEntity.prototype.draw = function (engine2d) {
    engine2d.drawCircle(this.physicsEntity.getMidX(), this.physicsEntity.getMidY(), this.physicsEntity.width, this.color);
    engine2d.drawRect(this.physicsEntity.getLeft(), this.physicsEntity.getTop(), this.physicsEntity.width, this.physicsEntity.height, this.color);

    var endX = Math.cos(this.physicsEntity.direction) * this.physicsEntity.width + this.physicsEntity.getMidX();
    var endY = Math.sin(this.physicsEntity.direction) * this.physicsEntity.height + this.physicsEntity.getMidY();

    engine2d.drawLine(this.physicsEntity.getMidX(), this.physicsEntity.getMidY(), endX, endY, "#000000");
};


function Player(x, y) {
    MobileEntity.call(this, {
        x: x, 
        y: y,
        color: "#FF0000",
        radius: 30,
        speed: 350,
        isStatic : false
    });
    this.physicsEntity.addCollisionClass("static");
    this.physicsEntity.addCollisionClass("minion");
    this.physicsEntity.addCollisionClass("player");
}

Player.prototype = new MobileEntity();

Player.prototype.bindActions = function(inputEngine) {
    inputEngine.bindMouse(this.id, MouseButtonID.BT_RIGHT, "move", {});
    inputEngine.bindKey(this.id, KeyID.VK_S, "stop", {});
};

Player.prototype.handleAction = function(action) {
    if (action.name === "move") {
        this.physicsEntity.targetLocation = action;
    } else if (action.name === "stop") {
        this.physicsEntity.targetLocation = {
            x: this.physicsEntity.x,
            y: this.physicsEntity.y
        };
    }
};

function Minion(x, y) {
    MobileEntity.call(this, {
        x: x, 
        y: y,
        speed: 200,
        radius: 20,
        isStatic: true
    });
    this.physicsEntity.addCollisionClass("static");
    this.physicsEntity.addCollisionClass("minion");
}

Minion.prototype = new MobileEntity();

Minion.prototype.bindActions = function(inputEngine) {
};

Minion.prototype.handleAction = function(action) {
};

