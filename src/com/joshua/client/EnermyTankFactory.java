package com.joshua.client;

import java.util.List;

import com.google.gwt.user.client.Random;

public class EnermyTankFactory {

    private static long lastTime;

    private static final int MAX_TANKS = 12;

    private static EnermyTank create(Tank myTank) {

        long timeDiff = System.currentTimeMillis() - lastTime;

        int r = Random.nextInt(10);

        if ((timeDiff / 1000) > r ) {
            EnermyTank tank = new EnermyTank(myTank);
            lastTime = System.currentTimeMillis();
            return tank;
        }

        return null;
    }

    public static EnermyTank placeNewEnermyTank(Tank myTank, List<? extends Tank> tanks) {
        if (tanks.size() > MAX_TANKS) {
            return null;
        }

        EnermyTank e = create(myTank);
        if (e != null) {
            int x = Random.nextInt(MyCanvas.canvasWidth - e.getySize());
            int y = Random.nextInt(MyCanvas.canvasHeight - e.getySize());
            if (canPlace(e, x, y, myTank, tanks)) {
                e.setX(x);
                e.setY(y);
                e.setFace(createRamdomFace());
                return e;
            }

        }

        return null;
    }

    private static FACE createRamdomFace() {
        int r = Random.nextInt(4);
        switch (r) {
            case 0:
                return FACE.NORTH;
            case 1:
                return FACE.EAST;
            case 2:
                return FACE.SOUTH;
            case 3:
                return FACE.WEST;
        }

        return FACE.SOUTH;
    }

    private static boolean canPlace(Tank t, int x, int y, Tank myTank, List<? extends Tank> tanks) {
        int regionMinX = x - t.getySize();
        int regionMaxX = x + t.getySize();
        int regionMinY = y - t.getySize();
        int regionMaxY = y + t.getySize();

        if (!canPlace(regionMinX, regionMaxX, regionMinY, regionMaxY, myTank)) {
            return false;
        }

        for (Tank existTank : tanks) {
            if (!canPlace(regionMinX, regionMaxX, regionMinY, regionMaxY, existTank)) {
                return false;
            }
        }
        return true;
    }

    private static boolean canPlace(int regionMinX, int regionMaxX, int regionMinY, int regionMaxY, Tank existTank) {
        if (existTank.getRightX() >= regionMinX && existTank.getX() <= regionMaxX && existTank.getDownY() >= regionMinY && existTank.getY() <= regionMaxY) {
            return false;
        } else {
            return true;
        }
    }
}
