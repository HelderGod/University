function mensangem(text){
    let terminal = document.getElementById("terminal");
    terminal.innerHTML=text;
}
function range(n) {
    return [...Array(n).keys()];
}

function render(model){
  
    if(model.lose){
        mensangem(`YOU LOSE!! <br> Pontuation: ${Math.round(model.time * 0.001)}s `)
    }else{

        mensangem(`Time : ${Math.round(model.time * 0.001)}s`)
    }
    let ari = model.ariete
    this.fillStyle="#C2B280"
    this.fillRect(0,0,this.canvas.width, this.canvas.height);
    this.save();
    this.translate(ari.x,ari.y);
    this.scale(ari.scale,ari.scale);
    this.ariete();
    this.restore()
    for(d of model.porta){
        this.fillStyle = "#966F33";
        if(d.pos<0.2){
            this.fillRect(d.x,0,100,d.y);
        }
        else{
            this.fillRect(d.x,400,100,-d.y);
        }
    }
}
    

function update(){
    if(this.lose){  
        this.start=performance.now();
        return;
    }
    let ari = this.ariete
    switch(ari.action){
        case 1:
            ari.y -= ari.vel
            break;
        case 2:
            ari.y += ari.vel
            break;
        default:
            break;
    }
    if(ari.y<-5) ari.y=-5;
    if(ari.y+ari.sy>400) ari.y=400-ari.sy;
    ari.action=0;
    for(p of this.porta){
        p.x -= p.vel;
        if(p.x < 0 ){
           this.lose=true;
        }
        if(p.x < 90) {
            if(p.pos<0.5){
               if(p.y-ari.y>0){
                   p.eliminate=true;
               }
            }else{
                if(ari.y+ari.sy>400-p.y){
                    p.eliminate=true;
                }
            }
        }
        if(p.eliminate){
            p.x=Math.ceil(Math.random()*450)+100
            p.y=Math.random()*150+100
            p.pos= Math.random();
            p.eliminate= false
        }
    }
}
function newDoor(){
    let porta={
        x:Math.ceil(Math.random()*450)+100,
        y:Math.random()*100+100,
        pos: Math.random(),
        vel:Math.random()*0.7+0.009,
        eliminate: false,
    }
    return porta;
    
}
function newModel() {
    let model = {
        time:0,
        start:0,
        lose: false,
        min_vel: 1,
        min_height: 150,
        ariete: {
            x: 0,
            y: 0,
            sx: 0,
            sy: 74,
            vel: 10,
            scale: 0.3,
            action: 0,
        },
        porta: range(3).map(i => newDoor()),
    };
    model.update = update;
    document.addEventListener("keypress", (e) => {
        if(e.key==="w"||e.key==="W") model.ariete.action=1; 
        if(e.key==="s"||e.key==="S") model.ariete.action=2; 
    });
    return model;
}

function newContext() {
    let gc = document
        .getElementById("canvas")
        .getContext("2d");

    gc.render = render;
    gc.ariete = ariete;
    gc.canvas.width = 700;
    gc.canvas.height = 400;

    return gc;
}

function main() {
    mensangem("Ram Ram")
    let gc = newContext();
    let model = new newModel();
    let step = (ts) => {
        model.update();
        if(!model.lose){
            let progress = ts - model.start;
            model.time=progress;
        }
        gc.render(model);
        requestAnimationFrame(step);
    }
    requestAnimationFrame(step);
}