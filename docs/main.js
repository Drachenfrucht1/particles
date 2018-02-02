const WIDTH = 400;
const HEIGHT = 400;
const FPS = 30;

var socket;
var objects = [];

function setup() {
  console.log("Starting...");
  socket = io("localhost:3000");
  createCanvas(WIDTH, HEIGHT);
  frameRate(FPS);
  noStroke();
  fill(15, 7, 3);
  rect(0, 0, WIDTH, HEIGHT);

  socket.on("frame", function(msg) {
    objects = JSON.parse();
  });

  console.log("Setup finished");
}

function draw() {
  sleep(2000);
  clear();
  fill(15, 7, 3);
  rect(0, 0, WIDTH, HEIGHT);
  fill(211, 90, 29);
  for(var i = 0; i < objects.length; i++) {
    rect(objects[i].x, objects[i].y, 10, 10);
  }
}
