package com.halloween.GameScreens;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.halloween.GameContents.HealthBarBoss;

public class BossScreen implements GameScreen {


    private HealthBarBoss healthBarBoss;

    public BossScreen() {
        super();
        this.healthBarBoss = new HealthBarBoss();
        this.healthBarBoss.setNewHealth(1000);
    }

    @Override
    public void update() {
        this.healthBarBoss.update();
    }

    @Override
    public void draw(Canvas canvas) {
        this.healthBarBoss.draw(canvas);
    }

    @Override
    public void terminate() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void receiveTouch(MotionEvent event) {

    }
}
