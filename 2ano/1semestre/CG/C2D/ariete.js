function ariete(){
    this.fillStyle = "#FFFF00";
    //cara
    this.beginPath();
    this.moveTo(200, 80);
    this.arcTo(120, 80, 120, 160, 80);
    this.arcTo(120, 240, 200, 240, 80);
    this.arcTo(280, 240, 280, 160, 80);
    this.arcTo(280, 80, 200, 80, 80);
    this.fill();
    //olhos
    this.fillStyle = "#000000";
    this.beginPath();
    this.moveTo(170, 130);
    this.arcTo(165, 130, 165, 135, 5);
    this.arcTo(165, 140, 170, 140, 5);
    this.arcTo(175, 140, 175, 135, 5);
    this.arcTo(175, 130, 170, 130, 5);
    this.fill();
    this.beginPath();
    this.moveTo(230, 130);
    this.arcTo(225, 130, 225, 135, 5);
    this.arcTo(225, 140, 230, 140, 5);
    this.arcTo(235, 140, 235, 135, 5);
    this.arcTo(235, 130, 230, 130, 5);
    this.fill();
    //boca
    this.fillStyle = "#654321";
    this.strokeStyle = "#FF00FF"
    this.beginPath();
    this.moveTo(180, 210);
    this.quadraticCurveTo(200, 225, 220, 210);
    this.quadraticCurveTo(200, 215, 180, 210);
    this.stroke();
    this.fill();
    //sobrancelhas
    this.beginPath();
    this.moveTo(180, 125);
    this.quadraticCurveTo(170, 115, 150, 125);
    this.quadraticCurveTo(170, 120, 170, 125);
    this.fill();
    this.beginPath();
    this.moveTo(210, 125);
    this.quadraticCurveTo(230, 115, 240, 125);
    this.quadraticCurveTo(230, 120, 220, 125);
    this.fill();
    //nariz
    this.strokeStyle="black";
    this.moveTo(200, 150)
    this.quadraticCurveTo(190, 185, 205, 180);
    this.stroke()
    //chapéu
    this.fillStyle = "#0000A0";
    this.beginPath();
    this.moveTo(120, 100);
    this.quadraticCurveTo(200, 140, 280, 100);
    this.quadraticCurveTo(200, 60, 120, 100);
    this.fill();
    this.beginPath();
    this.moveTo(150, 100);
    this.quadraticCurveTo(160, 30, 200, 30);
    this.quadraticCurveTo(240, 30, 250, 100);
    this.strokeStyle="black";
    this.stroke();
    this.fill();
    this.fillStyle="#FFFF33";
    this.fillRect(155, 70, 90, 10);
    this.fillStyle="#F6BE00";
    this.beginPath();
    this.moveTo(205, 60);
    this.lineTo(210, 75);
    this.lineTo(223, 78);
    this.lineTo(210, 83);
    this.lineTo(220, 95);
    this.lineTo(205, 88);
    this.lineTo(190, 95);
    this.lineTo(198, 83);
    this.lineTo(185, 78);
    this.lineTo(200, 75);
    this.fill();
}
