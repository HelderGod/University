function porta(x,y) {
    this.fillStyle = "#966F33";
    this.beginPath();
    this.moveTo(0, 0);
    this.lineTo(0, y);
    this.lineTo(x, y);
    this.lineTo(x, 0);
    this.fill();
    //maçaneta
    this.fillStyle = "#D4AF37";
    this.beginPath();
    this.moveTo(x-20, y/2);
    this.arcTo(x-30, y/2, x-30, y/2+10, 10);
    this.arcTo(x-30, y/2+20, x-20, y/2+20, 10);
    this.arcTo(x-10, y/2+20, x-10, y/2+10, 10);
    this.arcTo(x-10, y/2, x-20, y/2, 10);
    this.fill();
}

