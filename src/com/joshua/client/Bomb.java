package com.joshua.client;

import java.util.List;

import com.google.gwt.canvas.dom.client.Context2d;

public class Bomb {
    private double x;
    private double y;
    private FACE face;
    private double speed;

    private int radius;

    private String color;

    private long lastTime;

    public Bomb(double x, double y, FACE face) {
        this.x = x;
        this.y = y;
        this.face = face;

        speed = 0.1;

        radius = 5;
        color = "#FF0606";
        lastTime = System.currentTimeMillis();
    }
    
    

    public void setSpeed(double speed) {
        this.speed = speed;
    }



    public boolean isOutOfRange() {
        if (x < 0) {
            return true;
        } else if (y < 0) {
            return true;
        } else if (x >= MyCanvas.canvasWidth) {
            return true;
        } else if (y >= MyCanvas.canvasHeight) {
            return true;
        }
        return false;
    }

    private double getMoveLength() {
        long time = System.currentTimeMillis() - lastTime;
        return time * speed;
    }

    public void drawBomb(Context2d context) {
        if (isOutOfRange()) {
            return;
        }
        context.setFillStyle(color);
        context.beginPath();
        context.arc(x, y, radius, 0, Math.PI * 2);
        context.closePath();
        context.fill();

        if (this.face == FACE.NORTH) {
            y -= getMoveLength();

        } else if (this.face == FACE.EAST) {
            x += getMoveLength();

        } else if (this.face == FACE.SOUTH) {
            y += getMoveLength();

        } else if (this.face == FACE.WEST) {
            x -= getMoveLength();

        }
        lastTime = System.currentTimeMillis();
    }

    public void tryHit(Tank tank) {

        if (x >= tank.getX() && x <= tank.getRightX() && y >= tank.getY() && y <= tank.getDownY()) {
            tank.setHit(true);

        }
    }

    public void tryHit(List<Tank> tanks) {
        for (Tank t : tanks) {
            tryHit(t);
        }
    }

}
