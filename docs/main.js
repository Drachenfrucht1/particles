const WIDTH = 400;
const HEIGHT = 400;
const FPS = 30;

var sim;

function setup() {
  console.log("Starting...");
  sim = new Simulation(WIDTH, HEIGHT, FPS);
  sim.createObjects(2);
  createCanvas(WIDTH, HEIGHT);
  frameRate(FPS);
  noStroke();
  fill(15, 7, 3);
  rect(0, 0, WIDTH, HEIGHT);
  noLoop();
  console.log("Setup finished");
  //sim.calculate();
}

function draw() {
  sleep(2000);
  sim.calculate();
  clear();
  fill(15, 7, 3);
  rect(0, 0, WIDTH, HEIGHT);
  fill(211, 90, 29);
  var objects = sim.objects;
  for(var i = 0; i < sim.objects.length; i++) {
    rect(sim.objects[i].location.x, sim.objects[i].location.y, 10, 10);
  }
}
