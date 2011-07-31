function PhysicsEntity(config) {
    this.x = config.x;
    this.y = config.y;

    this.isStatic = config.isStatic;

    this.lastX = this.x;
    this.lastY = this.y;

    this.width = config.width;
    this.height = config.height;

    this.speed = config.speed || 0;
    this.direction = config.direction || 0;

    this.collisionClasses = [];

    this.targetLocation = {
        x: this.x,
        y: this.y
    };

    this.alive = true;
}

PhysicsEntity.prototype.getMidX = function() {
    return (this.width / 2) + this.x;
};

PhysicsEntity.prototype.getMidY = function() {
    return (this.height / 2) + this.y;
};

PhysicsEntity.prototype.getTop = function() {
    return this.y;
};

PhysicsEntity.prototype.getLeft = function() {
    return this.x;
};

PhysicsEntity.prototype.getRight = function() {
    return this.x + this.width;
};

PhysicsEntity.prototype.getBottom = function() {
    return this.y + this.height;
};

PhysicsEntity.prototype.addCollisionClass = function(className) {
    this.collisionClasses.push(className);
};

function CollisionDetector() {}

CollisionDetector.prototype.collideRect = function(collider, collidee) {
    var l1 = collider.getLeft();
    var t1 = collider.getTop();
    var r1 = collider.getRight();
    var b1 = collider.getBottom();

    var l2 = collidee.getLeft();
    var t2 = collidee.getTop();
    var r2 = collidee.getRight();
    var b2 = collidee.getBottom();

    if (b1 < t2 || t1 > b2 || r1 < l2 || l1 > r2) {
        return false;
    }
    return true;
};

CollisionDetector.prototype.hasConflictingClass = function(a, b) {
    for (var i = 0; i < a.collisionClasses.length; i++) {
        for (var j = 0; j < b.collisionClasses.length; j++) {
            if (a.collisionClasses[i] == b.collisionClasses[j]) {
                return true;
            }
        }
    }
    return false;
};

CollisionDetector.prototype.detect = function(entity, entities) {
    var collisions = [];
    for (var i = 0, l = entities.length; i < l; i++) {
        var e = entities[i];
        if (entity === e) continue;
        if (!this.hasConflictingClass(entity, e)) continue;

        if (this.collideRect(entity, e)) {
            collisions.push({
                collider: entity,
                collidee: e
            });
        }
    }
    return collisions;
};

function CollisionSolver() {

}

CollisionSolver.prototype.solve = function(collider, collidee) {
    collider.x = collider.lastX;
    collider.y = collider.lastY;
};

CollisionSolver.prototype.solveCollisions = function(collisions) {
    for (var i = 0, l = collisions.length; i < l; i++) {
        var c = collisions[i];
        this.solve(c.collider, c.collidee);
    }
};

function PhysicsWorld(config) {
    this.squareSize = config.squareSize;

    this.widthInSquares = Math.floor(config.width / this.squareSize);
    this.heightInSquares = Math.floor(config.height / this.squareSize);

    this.world = new Array(this.widthInSquares);
    for (var i = 0, l = this.world.length; i < l; i++) {
        this.world[i] = new Array(this.heightInSquares);
    }
}

PhysicsWorld.prototype.update = function(entity) {
    entity.worldCells = entity.worldCells || [];
    entity.worldCells.each(function(cell) {
        cell.remove(entity);
    });

    var startIndexX = Math.floor(entity.x / this.squareSize);
    var restX = entity.x % this.squareSize;
    var howManyX = Math.ceil((entity.width + restX) / this.squareSize);

    var startIndexY = Math.floor(entity.y / this.squareSize);
    var restY = entity.y % this.squareSize;
    var howManyY = Math.ceil((entity.height + restY) / this.squareSize);

    for (var i = startIndexX; i < Math.min(startIndexX + howManyX, this.widthInSquares); i++) {
        for (var j = startIndexY; j < Math.min(startIndexY + howManyY, this.heightInSquares); j++) {
            var cell = this.world[i][j] = this.world[i][j] || [];
            cell.push(entity);
            entity.worldCells.push(cell);
        }
    }
};

PhysicsWorld.prototype.findPath = function(fromLocation, toLocation) {

    
};

function PhysicsEngine(collisionDetector, collisionSolver, physicsWorld) {
    this.entities = [];
    this.dynamicEntities = [];
    this.collisionDetector = collisionDetector;
    this.collisionSolver = collisionSolver;
    this.physicsWorld = physicsWorld;
}

PhysicsEngine.prototype.addEntity = function(entity) {
    this.entities.push(entity);
    if (!entity.isStatic) {
        this.dynamicEntities.push(entity);
    }
    this.physicsWorld.update(entity);
};

PhysicsEngine.prototype.move = function(elapsed) {
    for (var i = 0, l = this.dynamicEntities.length; i < l; i++) {
        var dEntity = this.dynamicEntities[i];
        var pos = dEntity.targetLocation;

        if (Math.dst(pos.x, pos.y, dEntity.x, dEntity.y) <= 3) {
            dEntity._lastMoveAction = null;
            return;
        }

        var x = pos.x - dEntity.x;
        var y = pos.y - dEntity.y;

        dEntity.direction = Math.atan(Math.abs(y / x));

        if (x < 0) {
            dEntity.direction = rad(180) - dEntity.direction;
        }
        if (y < 0) {
            dEntity.direction = rad(360) - dEntity.direction;
        }

        dEntity.lastX = dEntity.x;
        dEntity.lastY = dEntity.y;
        dEntity.x += Math.cos(dEntity.direction) * (dEntity.speed * elapsed);
        dEntity.y += Math.sin(dEntity.direction) * (dEntity.speed * elapsed);
    }
};

PhysicsEngine.prototype.removeEntity = function(entity) {
    this.entities.remove(entity);
    this.dynamicEntities.remove(entity);

};

PhysicsEngine.prototype.step = function(elapsed) {
    this.move(elapsed);

    for (var i = 0, l = this.dynamicEntities.length; i < l; i++) {
        var dEntity = this.dynamicEntities[i];
        if (dEntity.alive) {
            var collisions = this.collisionDetector.detect(dEntity, this.entities);
            if (collisions.length > 0) {
                this.collisionSolver.solveCollisions(collisions);
            }
            this.physicsWorld.update(dEntity);
        } else {
            this.removeEntity(dEntity);
        }
    }
};
