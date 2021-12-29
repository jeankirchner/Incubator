function main() {
    var stats = setupStats();
    var container = createContainer();
    var canvas2d = createCanvas2d(container);
    var inputEngine = new InputEngine(canvas2d.canvas);
    var engine2d = new Engine2d(canvas2d);
    var game = new Game(engine2d, inputEngine, stats);
    game.play();
}

function setupStats() {
    var stats = new Stats();
    stats.setMode(0);
    document.body.appendChild(stats.domElement);
    return stats;
}

function createContainer() {
    var div = document.createElement("div");
    div.style.position = "relative";
    div.style.top = "0px";
    div.style.left = "0px";
    div.style.height = jQuery(window).height();
    div.style.width = jQuery(window).width();
    document.body.appendChild(div);
    return div;
}

function createCanvas2d(container) {
    var canvas = document.createElement("canvas");
    canvas.style.border = "1px solid black";
    canvas.width = 1000;
    canvas.height = 500;
    container.appendChild(canvas);
    return canvas.getContext("2d");
}
