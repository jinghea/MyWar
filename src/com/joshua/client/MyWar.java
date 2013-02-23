package com.joshua.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MyWar implements EntryPoint {

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        MyCanvas canvas = new MyCanvas();
        MyWarController controller = new MyWarController(canvas);
        controller.start();
    }
}
