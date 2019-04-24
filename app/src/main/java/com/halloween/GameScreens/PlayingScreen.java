package com.halloween.GameScreens;

import android.graphics.Canvas;
import android.view.MotionEvent;
import com.halloween.GameObjects.MainCharacter;

public class PlayingScreen implements GameScreen{
    private MainCharacter mainCharacter;

    public PlayingScreen() {
        this.reset();
    }

    @Override
    public void reset() {
        this.mainCharacter = new MainCharacter();
    }

    @Override
    public void update() {
        mainCharacter.update();
    }

    @Override
    public void draw(Canvas canvas) {
        this.mainCharacter.draw(canvas);
    }

    @Override
    public void terminate() {

    }

    @Override
    public void receiveTouch(MotionEvent event) {

    }


}
