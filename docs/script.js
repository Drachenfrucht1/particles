var objectsColor = "#fc6e3f";
var grey = "#5b5655";
var url = "ws://drachenfruch1.de/gravitation/ws";

var sketch = function(p) {
    p.setup = () => {
        p.createCanvas(1000, 410);
        p.fill(grey);
        p.noStroke();
        p.noLoop();

        p.rect(0, 0, 1000, 410);
        p.fill(objectsColor);

        var connection = new WebSocket("ws://localhost:4000/ws");

        connection.onopen = () =>  {
            console.log("Connected...");
        };

        connection.onerror = (error) => {
            console.log("Websocket error: " + error);
        };

        connection.onmessage = (e) => {
            var simulation = JSON.parse(e.data);

            //reset canvas
            p.clear();
            p.fill(grey);
            p.rect(0, 0, 1000, 410);
            p.fill(objectsColor);

            for(var i = 0; i < simulation.length; i++) {
                var obj = simulation[i];
                p.rect(obj.x/4, obj.y/4, 2, 2);
            }
        };

    }
}

new p5(sketch, "container");