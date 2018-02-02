import "simulation.js";
var app = require("express")();
var http = require("http").Server(app);
var io = require("socket.io")(http);
var simulation;

io.on("connection", function(socket) {
  console.log("User connected");
});

http.listen(3000, function() {
  console.log("Listening on localhost:3000");
});

simulation = new Simulation(1000, 1000, 60);

while(true) {
  simulation.calculate();
  var objects = simulation.createSendObjects();
  io.emit("frame", JSON.stringify(objects));
}
