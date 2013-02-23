package com.joshua.client;

import java.util.List;

import com.google.gwt.canvas.dom.client.Context2d;

public class Tank {
    private String color;
    private int x;
    private int y;

    private int xSize;
    private int ySize;

    private int gunXSize;
    private int gunYSize;

    private int speed;

    private FACE face;

    private boolean hit;

    public Tank() {
        color = "#0B6905";
        xSize = 20;
        ySize = 36;

        gunXSize = 4;
        gunYSize = 10;

        speed = 4;
        face = FACE.NORTH;

        x = (MyCanvas.canvasWidth - xSize) / 2;
        y = MyCanvas.canvasHeight - ySize;

        hit = false;

    }

    public void setFace(FACE face) {
        this.face = face;
    }

    public FACE getFace() {
        return face;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getxSize() {
        return xSize;
    }

    public void setxSize(int xSize) {
        this.xSize = xSize;
    }

    public int getySize() {
        return ySize;
    }

    public void setySize(int ySize) {
        this.ySize = ySize;
    }

    public int getGunXSize() {
        if (this.face == FACE.NORTH) {
            return gunXSize;

        } else if (this.face == FACE.EAST) {
            return gunYSize;

        } else if (this.face == FACE.SOUTH) {
            return gunXSize;

        } else if (this.face == FACE.WEST) {
            return gunYSize;
        }
        return 0;
    }

    public void setGunXSize(int gunXSize) {
        this.gunXSize = gunXSize;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public boolean isHit() {
        return hit;
    }

    public int getGunYSize() {
        if (this.face == FACE.NORTH) {
            return gunYSize;

        } else if (this.face == FACE.EAST) {
            return gunXSize;

        } else if (this.face == FACE.SOUTH) {
            return gunYSize;

        } else if (this.face == FACE.WEST) {
            return gunXSize;
        }
        return 0;
    }

    public void setGunYSize(int gunYSize) {
        this.gunYSize = gunYSize;
    }

    public int getBodyX() {
        if (this.face == FACE.NORTH) {
            return x;

        } else if (this.face == FACE.EAST) {
            return x;

        } else if (this.face == FACE.SOUTH) {
            return x;

        } else if (this.face == FACE.WEST) {
            return x + gunYSize;
        }
        return 0;
    }

    public int getBodyY() {
        if (this.face == FACE.NORTH) {
            return y + gunYSize;

        } else if (this.face == FACE.EAST) {
            return y;

        } else if (this.face == FACE.SOUTH) {
            return y;

        } else if (this.face == FACE.WEST) {
            return y;
        }

        return 0;
    }

    public int getBodyXSize() {
        if (this.face == FACE.NORTH) {
            return xSize;

        } else if (this.face == FACE.EAST) {
            return ySize - gunYSize;

        } else if (this.face == FACE.SOUTH) {
            return xSize;

        } else if (this.face == FACE.WEST) {
            return ySize - gunYSize;
        }

        return 0;
    }

    public int getBodyYSize() {
        if (this.face == FACE.NORTH) {
            return ySize - gunYSize;

        } else if (this.face == FACE.EAST) {
            return xSize;

        } else if (this.face == FACE.SOUTH) {
            return ySize - gunYSize;

        } else if (this.face == FACE.WEST) {
            return xSize;
        }

        return 0;
    }

    public int getGunX() {
        if (this.face == FACE.NORTH || this.face == FACE.SOUTH) {
            return x + (xSize - gunXSize) / 2;

        } else if (this.face == FACE.EAST) {
            return x + ySize - gunYSize;

        } else if (this.face == FACE.WEST) {
            return x;
        }
        return 0;
    }

    public int getGunY() {
        if (this.face == FACE.NORTH) {
            return y;

        } else if (this.face == FACE.EAST) {
            return y + (xSize - gunXSize) / 2;

        } else if (this.face == FACE.SOUTH) {
            return y + ySize - gunYSize;

        } else if (this.face == FACE.WEST) {
            return y + (xSize - gunXSize) / 2;
        }
        return 0;
    }

    public int getRightX() {
        if (this.face == FACE.NORTH) {
            return x + xSize;

        } else if (this.face == FACE.EAST) {
            return x + ySize;

        } else if (this.face == FACE.SOUTH) {
            return x + xSize;

        } else if (this.face == FACE.WEST) {
            return x + ySize;
        }
        return 0;
    }

    public int getDownY() {
        if (this.face == FACE.NORTH) {
            return y + ySize;

        } else if (this.face == FACE.EAST) {
            return y + xSize;

        } else if (this.face == FACE.SOUTH) {
            return y + ySize;

        } else if (this.face == FACE.WEST) {
            return y + xSize;
        }
        return 0;
    }
    
    protected boolean isOverlapWithOtherTank(Tank otherTank) {
        return this.getDownY() > otherTank.getY() && y < otherTank.getDownY() && x < otherTank.getRightX() && this.getRightX() > otherTank.getX();
    }

    public void moveUp(List<Tank> otherTanks) {
        face = FACE.NORTH;
        y -= speed;
        limitInBorder();

        for (Tank otherTank : otherTanks) {
            if (isOverlapWithOtherTank(otherTank)) {
                y += speed;
                break;
            }
        }
    }

    

    public void moveDown(List<Tank> otherTanks) {
        face = FACE.SOUTH;
        y += speed;
        limitInBorder();
        
        for (Tank otherTank : otherTanks) {
            if (isOverlapWithOtherTank(otherTank)) {
                y -= speed;
                break;
            }
        }
    }

    public void moveLeft(List<Tank> otherTanks) {
        face = FACE.WEST;
        x -= speed;
        limitInBorder();
        
        for (Tank otherTank : otherTanks) {
            if (isOverlapWithOtherTank(otherTank)) {
                x += speed;
                break;
            }
        }
    }

    public void moveRight(List<Tank> otherTanks) {
        face = FACE.EAST;
        x += speed;
        limitInBorder();
        
        for (Tank otherTank : otherTanks) {
            if (isOverlapWithOtherTank(otherTank)) {
                x -= speed;
                break;
            }
        }
    }

    protected void limitInBorder() {
        if (x < 0) {
            x = 0;
        } else if (y < 0) {
            y = 0;
        } else if (x >= MyCanvas.canvasWidth - this.getxSize()) {
            x = MyCanvas.canvasWidth - this.getxSize() - 1;
        } else if (y >= MyCanvas.canvasHeight - this.getySize()) {
            y = MyCanvas.canvasHeight - this.getySize() - 1;
        }
    }

    public void drawTank(Context2d context) {
        //context.setFillStyle(tank.getColor());
        //        int rndRedColor = Random.nextInt(255);
        //        int rndGreenColor = Random.nextInt(255);
        //        int rndBlueColor = Random.nextInt(255);
        //        double rndAlpha = Random.nextDouble();
        //        CssColor randomColor = CssColor.make("rgba(" + rndRedColor + ", " + rndGreenColor + "," + rndBlueColor + ", " + rndAlpha + ")");
        //        context.setFillStyle(randomColor);

        if (!hit) {
            context.setFillStyle(getColor());
            context.beginPath();
            context.fillRect(getBodyX(), getBodyY(), getBodyXSize(), getBodyYSize());
            context.fillRect(getGunX(), getGunY(), getGunXSize(), getGunYSize());
            context.closePath();
            context.fill();
        }

    }

    public double getGunBombX() {
        if (this.face == FACE.NORTH) {
            return this.getGunX() + gunXSize / 2;

        } else if (this.face == FACE.EAST) {
            return this.x + ySize + gunXSize / 2;

        } else if (this.face == FACE.SOUTH) {
            return this.getGunX() + gunXSize / 2;

        } else if (this.face == FACE.WEST) {
            return this.getGunX() - gunXSize / 2;
        }
        return 0d;
    }

    public double getGunBombY() {
        if (this.face == FACE.NORTH) {
            return this.getGunY() - gunXSize / 2;

        } else if (this.face == FACE.EAST) {
            return this.getGunY() + gunXSize / 2;

        } else if (this.face == FACE.SOUTH) {
            return this.y + ySize + gunXSize / 2;

        } else if (this.face == FACE.WEST) {
            return this.getGunY() + gunXSize / 2;
        }
        return 0d;
    }

}
