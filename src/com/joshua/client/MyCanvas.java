package com.joshua.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class MyCanvas {
    private Canvas canvas;
    private Context2d context;

    static final int canvasHeight = 500;
    static final int canvasWidth = 400;

    private Tank tank;

    public MyCanvas() {
        canvas = Canvas.createIfSupported();

        if (canvas == null) {
            RootPanel.get().add(new Label("Sorry, your browser doesn't support the HTML5 Canvas element"));
            return;
        }

        canvas.setStyleName("mainCanvas");
        canvas.setWidth(canvasWidth + "px");
        canvas.setCoordinateSpaceWidth(canvasWidth);

        canvas.setHeight(canvasHeight + "px");
        canvas.setCoordinateSpaceHeight(canvasHeight);

        RootPanel.get().add(canvas);

        context = canvas.getContext2d();
    }

    public void setTank(Tank tank) {
        this.tank = tank;
    }

    public void drawTank() {
        tank.drawTank(context);
    }
    
    public void clear(){
        context.clearRect(0, 0, canvas.getOffsetWidth(), canvas.getOffsetHeight());
    }

    public Context2d getContext() {
        return context;
    }
    
    
}
