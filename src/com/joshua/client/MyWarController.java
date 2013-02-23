package com.joshua.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;

public class MyWarController extends Timer implements NativePreviewHandler {
    private static final int KEY_Z = 90;
    private static final int KEY_Z_U = 20;
    private static final int SPEED = 50;

    private int destroyTankCount;
    private long playTime;

    private MyCanvas myCanvas;
    private Tank myTank;
    private List<Bomb> bombs = new ArrayList<Bomb>();
    private List<Tank> enermyTanks = new ArrayList<Tank>();

    public MyWarController(MyCanvas myCanvas) {
        this.myCanvas = myCanvas;

        Event.addNativePreviewHandler(this);
    }

    public void start() {
        if (myTank == null) {
            myTank = new Tank();
            myCanvas.setTank(myTank);
        }
        myCanvas.drawTank();
        playTime = System.currentTimeMillis();

        scheduleRepeating(SPEED);
    }

    public void stop() {
        cancel();
    }

    private void reDrawElements() {
        myCanvas.clear();
        myCanvas.drawTank();
        drawBombs();
        drawEnermyTanks();
        drawData();
    }

    public void moveUp() {
        if (!checkHit()) {
            myTank.moveUp(enermyTanks);
            reDrawElements();
        }
    }

    public void moveDown() {
        if (!checkHit()) {
            myTank.moveDown(enermyTanks);
            reDrawElements();
        }
    }

    public void moveLeft() {
        if (!checkHit()) {
            myTank.moveLeft(enermyTanks);
            reDrawElements();
        }
    }

    public void moveRight() {
        if (!checkHit()) {
            myTank.moveRight(enermyTanks);
            reDrawElements();
        }
    }

    public void fireBomb() {
        Bomb b = new Bomb(myTank.getGunBombX(), myTank.getGunBombY(), myTank.getFace());
        b.setSpeed(0.08);
        bombs.add(b);
        b.drawBomb(myCanvas.getContext());
    }

    @Override
    public void onPreviewNativeEvent(NativePreviewEvent event) {
        if (!event.isCanceled()) {
            if (event.getTypeInt() == Event.ONKEYDOWN) {
                switch (event.getNativeEvent().getKeyCode()) {
                    case KeyCodes.KEY_UP:
                        moveUp();
                        break;
                    case KeyCodes.KEY_DOWN:
                        moveDown();
                        break;
                    case KeyCodes.KEY_LEFT:
                        moveLeft();
                        break;
                    case KeyCodes.KEY_RIGHT:
                        moveRight();
                        break;
                    case KEY_Z:
                        fireBomb();
                        break;
                    case KEY_Z_U:
                        fireBomb();
                        break;

                }
                if(event.getNativeEvent().getCtrlKey()){
                    fireBomb();
                }
            }
        }
    }

    public void drawBombs() {
        ArrayList<Bomb> newList = new ArrayList<Bomb>();
        for (Bomb b : bombs) {
            if (!b.isOutOfRange()) {
                newList.add(b);
                b.drawBomb(myCanvas.getContext());
                b.tryHit(myTank);
                b.tryHit(enermyTanks);
            }
        }
        bombs = newList;

    }

    private List<Tank> getOtherTanks(int currentIdx) {
        List<Tank> newList = new ArrayList<Tank>();

        for (int i = 0; i < enermyTanks.size(); i++) {
            if (i != currentIdx) {
                newList.add(enermyTanks.get(i));
            }
        }
        return newList;
    }

    private void drawEnermyTanks() {
        ArrayList<Tank> newList = new ArrayList<Tank>();

        for (int i = 0; i < enermyTanks.size(); i++) {
            Tank et = enermyTanks.get(i);
            if (!et.isHit()) {
                et.drawTank(myCanvas.getContext());
                newList.add(et);

                EnermyTank ent = (EnermyTank) et;
                ent.tryToFire(bombs);

            } else {
                destroyTankCount++;
            }

        }

        enermyTanks = newList;
    }

    public boolean checkHit() {
        if (myTank.isHit()) {
            int time = Math.round((System.currentTimeMillis() - playTime) / 1000);
            Window.alert("You tank was destroy! Time:" + time + "; Kill:" + destroyTankCount + ".");
            stop();
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        if (!checkHit()) {
            reDrawElements();

            EnermyTank eTank = EnermyTankFactory.placeNewEnermyTank(myTank, enermyTanks);
            if (eTank != null) {
                enermyTanks.add(eTank);
            }

            for (int i = 0; i < enermyTanks.size(); i++) {
                Tank et = enermyTanks.get(i);
                if (!et.isHit()) {

                    EnermyTank ent = (EnermyTank) et;
                    List<Tank> otherTanks = getOtherTanks(i);
                    ent.tryToMove(otherTanks);
                }

            }
        }
    }

    private void drawData() {
        int time = Math.round((System.currentTimeMillis() - playTime) / 1000);
        myCanvas.getContext().fillText("Time:" + time + "; Kill:" + destroyTankCount, MyCanvas.canvasWidth - 80, MyCanvas.canvasHeight - 12);
        return;
    }
}
