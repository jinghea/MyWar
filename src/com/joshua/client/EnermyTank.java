package com.joshua.client;

import java.util.List;

import com.google.gwt.user.client.Random;

public class EnermyTank extends Tank {
    private int speed = 3;

    private Tank myTank;

    private long fireLastTime;

    private long moveRandomLastTime;
    private MOVETYPE moveType;

    private static final int SIGHT_NAR = 30;

    private boolean knownMyTank;

    public enum MOVETYPE {
        UP, LEFT, DOWN, RIGHT
    }

    public EnermyTank(Tank myTank) {
        super();
        this.myTank = myTank;
        this.setColor("#606060");
        knownMyTank = false;

    }

    private void fireBomb(List<Bomb> bombs) {
        long timeDiff = System.currentTimeMillis() - fireLastTime;

        if (timeDiff / 1000 > 2) {
            Bomb b = new Bomb(this.getGunBombX(), this.getGunBombY(), this.getFace());
            bombs.add(b);

            fireLastTime = System.currentTimeMillis();
        }

    }

    private boolean isMyTankInTarget() {
        knownMyTank = true;
        if (getFace() == FACE.NORTH) {
            if (myTank.getY() < this.getY() && this.getGunBombX() > myTank.getX() && this.getGunBombX() < myTank.getRightX()) {
                return true;
            }

        } else if (getFace() == FACE.EAST) {
            if (myTank.getX() > this.getX() && this.getGunBombY() > myTank.getY() && this.getGunBombY() < myTank.getDownY()) {
                return true;
            }

        } else if (getFace() == FACE.SOUTH) {
            if (myTank.getY() > this.getY() && this.getGunBombX() > myTank.getX() && this.getGunBombX() < myTank.getRightX()) {
                return true;
            }

        } else if (getFace() == FACE.WEST) {
            if (myTank.getX() < this.getX() && this.getGunBombY() > myTank.getY() && this.getGunBombY() < myTank.getDownY()) {
                return true;
            }
        }
        return false;
    }

    public void tryToFire(List<Bomb> bombs) {
        if (isMyTankInTarget()) {
            fireBomb(bombs);
        }
    }

    private boolean isMyTankInSignt() {
        if (getFace() == FACE.NORTH) {
            if (myTank.getY() < this.getY() && this.getGunBombX() > myTank.getX() - SIGHT_NAR && this.getGunBombX() < myTank.getRightX() + SIGHT_NAR) {
                return true;
            }

        } else if (getFace() == FACE.EAST) {
            if (myTank.getX() > this.getX() && this.getGunBombY() > myTank.getY() - SIGHT_NAR && this.getGunBombY() < myTank.getDownY() + SIGHT_NAR) {
                return true;
            }

        } else if (getFace() == FACE.SOUTH) {
            if (myTank.getY() > this.getY() && this.getGunBombX() > myTank.getX() - SIGHT_NAR && this.getGunBombX() < myTank.getRightX() + SIGHT_NAR) {
                return true;
            }

        } else if (getFace() == FACE.WEST) {
            if (myTank.getX() < this.getX() && this.getGunBombY() > myTank.getY() - SIGHT_NAR && this.getGunBombY() < myTank.getDownY() + SIGHT_NAR) {
                return true;
            }
        }
        return false;
    }

    private void moveRandom(List<Tank> otherTanks) {
        int moveTypeNum = Random.nextInt(4);

        switch (moveTypeNum) {
            case 0:
                moveUp(otherTanks);
                moveType = MOVETYPE.UP;
                break;
            case 1:
                moveRight(otherTanks);
                moveType = MOVETYPE.RIGHT;
                break;
            case 2:
                moveDown(otherTanks);
                moveType = MOVETYPE.DOWN;
                break;
            case 3:
                moveLeft(otherTanks);
                moveType = MOVETYPE.LEFT;
                break;

        }
        moveRandomLastTime = System.currentTimeMillis();
    }

    private void moveRandomForAWhile(List<Tank> otherTanks) {
        long timeDiff = System.currentTimeMillis() - moveRandomLastTime;

        if (timeDiff / 1000 > Random.nextInt(4)) {
            moveRandom(otherTanks);
        } else {
            switch (moveType) {
                case UP:
                    moveUp(otherTanks);
                    break;
                case RIGHT:
                    moveRight(otherTanks);
                    break;
                case DOWN:
                    moveDown(otherTanks);
                    break;
                case LEFT:
                    moveLeft(otherTanks);
                    break;
            }
        }
        if (knownMyTank) {
            changeFaceWhenKnown();
        }
    }

    public void tryToMove(List<Tank> otherTanks) {
        if (!isMyTankInTarget()) {
            if (isMyTankInSignt()) {
                knownMyTank = true;
            }
            moveRandomForAWhile(otherTanks);

        }

    }

    protected void limitInBorder(List<Tank> otherTanks) {
        if (this.getX() < 0) {
            this.setX(0);
            moveRandom(otherTanks);
        } else if (this.getY() < 0) {
            this.setY(0);
            moveRandom(otherTanks);
        } else if (this.getRightX() >= MyCanvas.canvasWidth) {
            this.setX(MyCanvas.canvasWidth - this.getySize() - 1);
            moveRandom(otherTanks);
        } else if (this.getDownY() >= MyCanvas.canvasHeight) {
            this.setY(MyCanvas.canvasHeight - this.getySize() - 1);
            moveRandom(otherTanks);
        }
    }

    private void tryHitMyTank() {
        if (isOverlapWithOtherTank(myTank)) {
            myTank.setHit(true);
        }
    }

    @Override
    public void moveUp(List<Tank> otherTanks) {
        this.setFace(FACE.NORTH);
        this.setY(this.getY() - speed);
        limitInBorder(otherTanks);
        tryHitMyTank();

        for (Tank otherTank : otherTanks) {
            if (isOverlapWithOtherTank(otherTank)) {
                //this.setY(this.getY() + speed);
                moveRandom(otherTanks);
                break;
            }
        }
    }

    @Override
    public void moveDown(List<Tank> otherTanks) {
        this.setFace(FACE.SOUTH);
        this.setY(this.getY() + speed);
        limitInBorder(otherTanks);
        tryHitMyTank();

        for (Tank otherTank : otherTanks) {
            if (isOverlapWithOtherTank(otherTank)) {
                //this.setY(this.getY() - speed);
                moveRandom(otherTanks);
                break;
            }
        }
    }

    @Override
    public void moveLeft(List<Tank> otherTanks) {
        this.setFace(FACE.WEST);
        this.setX(this.getX() - speed);
        limitInBorder(otherTanks);
        tryHitMyTank();

        for (Tank otherTank : otherTanks) {
            if (isOverlapWithOtherTank(otherTank)) {
                //this.setX(this.getX() + speed);
                moveRandom(otherTanks);
                break;
            }
        }
    }

    @Override
    public void moveRight(List<Tank> otherTanks) {
        this.setFace(FACE.EAST);
        this.setX(this.getX() + speed);
        limitInBorder(otherTanks);
        tryHitMyTank();

        for (Tank otherTank : otherTanks) {
            if (isOverlapWithOtherTank(otherTank)) {
                //this.setX(this.getX() - speed);
                moveRandom(otherTanks);
                break;
            }
        }
    }

    private void changeFaceWhenKnown() {
        if (this.getX() == myTank.getX()) {
            if (this.getY() > myTank.getY()) {
                this.setFace(FACE.NORTH);
            } else {
                this.setFace(FACE.SOUTH);
            }
        } else if (this.getY() == myTank.getY()) {
            if (this.getX() > myTank.getX()) {
                this.setFace(FACE.WEST);
            } else {
                this.setFace(FACE.EAST);
            }
        }
    }

}
