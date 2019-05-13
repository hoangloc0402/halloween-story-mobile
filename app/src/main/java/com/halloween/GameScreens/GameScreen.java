package com.halloween.GameScreens;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface GameScreen {
    public void update();

    public void draw(Canvas canvas);

    public void terminate();

    public void reset();

    public void receiveTouch(MotionEvent event);
}
