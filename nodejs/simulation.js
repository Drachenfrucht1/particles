var GRAVITATION_CONSTANT = 6.674 * Math.pow(10, -11);
var FRAMES_PER_SECOND = 60;
var Simulation = /** @class */ (function () {
    function Simulation(width, height, frameRate) {
        this.objects = [];
        this.width = width;
        this.height = height;
        FRAMES_PER_SECOND = frameRate;
    }
    Simulation.prototype.calculate = function () {
        console.log("1");
        for (var i = 0; i < this.objects.length; i++) {
            this.objects[i].calculateForces(this.objects);
        }
        console.log("2");
        for (var i = 0; i < this.objects.length; i++) {
            this.objects[i].calculateAcceleration();
        }
        console.log("3");
        this.calculateCrash();
        console.log("4");
        for (var i = 0; i < this.objects.length; i++) {
            if (this.objects[i].dead) {
                this.objects = getNewArray(this.objects[i], this.objects);
            }
            else {
                this.objects[i].move();
            }
        }
        console.log("5");
    };
    Simulation.prototype.calculateCrash = function () {
        var lObjects = this.objects;
        while (this.objects.length > 1) {
            var obj1 = lObjects[0];
            for (var i = 1; i < lObjects.length; i++) {
                var obj2 = lObjects[i];
                var obj1Match = false;
                var obj2Match = false;
                var m1 = obj1.speed.y / obj1.speed.x;
                var c1 = obj1.location.y - m1 * obj1.location.x;
                var m2 = obj2.speed.y / obj2.speed.x;
                var c2 = obj2.location.y - m2 * obj2.location.x;
                var x = 0;
                try {
                    x = (c2 - c1) / (m1 - m2);
                }
                catch (e) {
                    console.log(e);
                    continue;
                }
                var y = m1 * x + c1;
                //TODO
                if (obj1Match && obj2Match) {
                    obj2.dead = true;
                    this.objects = getNewArray(obj2, this.objects);
                    lObjects = getNewArray(obj2, lObjects);
                    obj1.mass += obj2.mass;
                    var valueF1 = obj1.acceleration * obj1.mass;
                    var valueF2 = obj2.acceleration * obj2.mass;
                    var f1 = Force.getForceFromVectorAndOrigin(x, y, new Vector2D(obj1.speed.x, obj1.speed.y), valueF1);
                    var f2 = Force.getForceFromVectorAndOrigin(x, y, new Vector2D(obj2.speed.x, obj2.speed.y), valueF2);
                    var forces = Force[2](f1, f2);
                    obj1.forces.push(Force.getResulting(x, y, forces));
                }
            }
            lObjects = getNewArray(obj1, lObjects);
        }
    };
    Simulation.prototype.createObjects = function (count) {
        for (var i = 0; i < count; i++) {
            var obj = new PhysicsObject(getRandomNumber(this.width), getRandomNumber(this.height), getRandomNumber(1000) + 10);
            this.objects.push(obj);
        }
    };
    Simulation.prototype.createSendObjects = function () {
        var array = [];
        for (var i = 0; i < this.objects.length; i++) {
            var obj = this.objects[i];
            array.push(new SendObject(obj.location.x, obj.location.y));
        }
        return array;
    };
    return Simulation;
}());
var PhysicsObject = /** @class */ (function () {
    function PhysicsObject(x, y, mass) {
        this.forces = [];
        this.speed = new Vector2D(0, 0);
        this.location = new Vector2D(x, y);
        this.dead = false;
        this.mass = mass * 10000;
        this.acceleration = 0;
    }
    PhysicsObject.prototype.calculateForces = function (objects) {
        for (var i = 0; i < objects.length; i++) {
            var obj = objects[i];
            if (obj != this) {
                var distance = new Vector2D(obj.location.x - this.location.x, obj.location.y - this.location.y);
                var value = (GRAVITATION_CONSTANT * this.mass * obj.mass) / Math.pow(distance.getValue(), 2);
                var f = Force.getForceFromVectorAndOrigin(this.location.x, this.location.y, distance, value);
                this.forces.push(f);
            }
        }
    };
    PhysicsObject.prototype.calculateAcceleration = function () {
        var resulting = Force.getResulting(this.location.x, this.location.y, this.forces);
        var acceleration = resulting.vector.getValue() / this.mass;
        var direction = this.speed;
        direction.add(resulting.vector);
        this.acceleration = acceleration / FRAMES_PER_SECOND;
        var speedValue = this.speed.getValue() + this.acceleration;
        this.speed = Vector2D.getVectorFromValue(direction, speedValue);
        this.forces = [];
    };
    PhysicsObject.prototype.move = function () {
        this.location.x += this.speed.x;
        this.location.y += this.speed.y;
    };
    return PhysicsObject;
}());
var Force = /** @class */ (function () {
    function Force(originX, originY, vector) {
        this.originX = originX;
        this.originY = originY;
        this.vector = vector;
    }
    Force.getForceFromVectorAndOrigin = function (originX, originY, vector, value) {
        var nVector = Vector2D.getVectorFromValue(vector, value);
        return new Force(originX, originY, nVector);
    };
    Force.getResulting = function (originX, originY, forces) {
        var resulting = new Force(originX, originY, new Vector2D(0, 0));
        for (var i = 0; i < forces.length; i++) {
            var f = forces[i];
            if (f.originX == originX && f.originY == originY) {
                resulting.vector.add(f.vector);
            }
        }
        return resulting;
    };
    return Force;
}());
var Vector2D = /** @class */ (function () {
    function Vector2D(x, y) {
        this.x = x;
        this.y = y;
    }
    Vector2D.prototype.add = function (vector) {
        this.x += vector.x;
        this.y += vector.y;
    };
    Vector2D.prototype.getValue = function () {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    };
    Vector2D.getVectorFromValue = function (vector, value) {
        var n = Math.sqrt(Math.pow(value, 2) / (Math.pow(vector.x, 2) + Math.pow(vector.y, 2)));
        return new Vector2D(vector.x * n, vector.y * n);
    };
    return Vector2D;
}());
var SendObject = /** @class */ (function () {
    function SendObject(x, y) {
        this.x = x;
        this.y = y;
    }
    return SendObject;
}());
function getRandomNumber(max) {
    return Math.floor(Math.random() * Math.floor(max));
}
function getNewArray(remove, array) {
    var index = 0;
    var newArray = [];
    for (var i = 0; i < array.length; i++) {
        if (array[i] != remove) {
            newArray.push(array[i]);
        }
    }
    return newArray;
}
function sleep(millis) {
    var currentTime = new Date().getTime();
    while (currentTime + millis >= new Date().getTime()) { }
}
