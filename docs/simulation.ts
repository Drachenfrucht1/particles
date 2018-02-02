const GRAVITATION_CONSTANT: number = 6.674 * Math.pow(10, -11);
var FRAMES_PER_SECOND: number = 60;

class Simulation {
	height: number;
	width: number;
	objects: any;
  constructor(width:number, height: number, frameRate: number) {
    this.objects = [];
    this.width = width;
    this.height = height;
    FRAMES_PER_SECOND = frameRate;
  }

  calculate(): void {
		console.log("1");
    for(var i = 0; i < this.objects.length; i++) {
      this.objects[i].calculateForces(this.objects);
    }
		console.log("2");
    for(var i = 0; i < this.objects.length; i++) {
      this.objects[i].calculateAcceleration();
    }
		console.log("3");

	  this.calculateCrash();
		console.log("4");
    for(var i = 0; i < this.objects.length; i++) {
      if(this.objects[i].dead) {
        this.objects = getNewArray(this.objects[i], this.objects);
      } else {
        this.objects[i].move();
      }
    }
		console.log("5");
  }

  calculateCrash() {
    var lObjects: PhysicsObject[] = this.objects;

    while(this.objects.length > 1) {
      var obj1: PhysicsObject = lObjects[0];
      for(var i = 1; i < lObjects.length; i++) {
        var obj2: PhysicsObject = lObjects[i];
        var obj1Match: boolean = false;
        var obj2Match: boolean = false;

        var m1 = obj1.speed.y / obj1.speed.x;
        var c1 = obj1.location.y - m1 * obj1.location.x;

        var m2 = obj2.speed.y / obj2.speed.x;
        var c2 = obj2.location.y - m2 * obj2.location.x;

        var x: number = 0;

        try {
          x = (c2-c1) / (m1-m2)
        } catch(e) {
          console.log(e);
          continue;
        }

        var y: number = m1 * x + c1;

        //TODO

        if(obj1Match && obj2Match) {
          obj2.dead = true;
          this.objects = getNewArray(obj2, this.objects);
          lObjects = getNewArray(obj2, lObjects);

          obj1.mass += obj2.mass;

          var valueF1 = obj1.acceleration * obj1.mass;
          var valueF2 = obj2.acceleration * obj2.mass;

          var f1: Force = Force.getForceFromVectorAndOrigin(x, y,
            new Vector2D(obj1.speed.x, obj1.speed.y), valueF1);

          var f2: Force = Force.getForceFromVectorAndOrigin(x, y,
            new Vector2D(obj2.speed.x, obj2.speed.y), valueF2);

          var forces: Force[] = Force[2](f1, f2);
          obj1.forces.push(Force.getResulting(x, y, forces));
        }
      }
      lObjects = getNewArray(obj1, lObjects);
    }
  }

  createObjects(count): void {
    for(var i = 0; i < count; i++) {
      var obj = new PhysicsObject(getRandomNumber(this.width),
        getRandomNumber(this.height),
        getRandomNumber(1000) + 10);
      this.objects.push(obj);
    }
  }
}

class PhysicsObject {
	dead: boolean;
	location: Vector2D;
	speed: Vector2D;
	forces: Force[];
  mass: number;
  acceleration: number;
  constructor(x: number, y: number, mass: number) {
    this.forces = [];
    this.speed = new Vector2D(0, 0);
    this.location = new Vector2D(x, y);
    this.dead = false;
    this.mass = mass * 10000;
    this.acceleration = 0;
  }

  calculateForces(objects: PhysicsObject[]): void {
    for(var i = 0; i < objects.length; i++) {
      var obj = objects[i];
      if(obj != this) {
        var distance: Vector2D= new Vector2D(obj.location.x - this.location.x,
          obj.location.y - this.location.y);

        var value:number = (GRAVITATION_CONSTANT * this.mass * obj.mass) / Math.pow(distance.getValue(), 2);

        var f: Force = Force.getForceFromVectorAndOrigin(this.location.x, this.location.y, distance, value);

        this.forces.push(f);
      }
    }
  }

  calculateAcceleration(): void {
    var resulting: Force = Force.getResulting(this.location.x, this.location.y, this.forces);
    var acceleration = resulting.vector.getValue() / this.mass;
    var direction: Vector2D = this.speed;
    direction.add(resulting.vector);

    this.acceleration = acceleration/FRAMES_PER_SECOND;

    var speedValue: number = this.speed.getValue() + this.acceleration;

    this.speed = Vector2D.getVectorFromValue(direction, speedValue);

    this.forces = [];
  }

  move(): void {
    this.location.x += this.speed.x;
    this.location.y += this.speed.y;
  }
}

class Force {
	originX: number;
	originY: number;
	vector: Vector2D;
  constructor(originX, originY, vector: Vector2D) {
    this.originX = originX;
    this.originY = originY;

    this.vector = vector;
  }

  static getForceFromVectorAndOrigin(originX: number, originY: number, vector: Vector2D, value: number): Force {
    var nVector = Vector2D.getVectorFromValue(vector, value);

    return new Force(originX, originY, nVector);
  }

  static getResulting(originX, originY, forces: Force[]): Force {
    var resulting = new Force(originX, originY, new Vector2D(0, 0));
    for(var i = 0; i < forces.length; i++) {
      var f = forces[i];
      if(f.originX == originX && f.originY == originY) {
        resulting.vector.add(f.vector);
      }
    }
    return resulting;
  }
}

class Vector2D {
	x: number;
	y: number;
  constructor(x, y) {
    this.x = x;
    this.y = y;
  }

  add(vector: Vector2D): void {
    this.x += vector.x;
    this.y += vector.y;
  }

  getValue(): number {
    return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
  }

  static getVectorFromValue(vector: Vector2D, value: number): Vector2D {
    var n = Math.sqrt(Math.pow(value, 2) / (Math.pow(vector.x, 2) + Math.pow(vector.y, 2)));

    return new Vector2D(vector.x * n, vector.y * n);
  }
}

function getRandomNumber(max: number): number {
  return Math.floor(Math.random() * Math.floor(max));
}

function getNewArray(remove, array) {
  var index: number = 0;
  var newArray = [];
  for(var i = 0; i < array.length; i++) {
    if(array[i] != remove) {
      newArray.push(array[i]);
    }
  }

  return newArray;
}

function sleep(millis:number) {
	var currentTime: any = new Date().getTime();
	while(currentTime + millis >= new Date().getTime()){}
}
