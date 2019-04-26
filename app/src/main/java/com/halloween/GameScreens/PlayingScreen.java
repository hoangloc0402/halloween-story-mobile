package com.halloween.GameScreens;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

import com.halloween.Constants;
import com.halloween.GameContents.JoyStick;
import com.halloween.GameObjects.MainCharacter;
import com.halloween.R;

public class PlayingScreen implements GameScreen{
    private MainCharacter mainCharacter;
    private JoyStick joyStick;

    public PlayingScreen() {
        this.reset();
        this.joyStick = new JoyStick();
    }

    @Override
    public void reset() {
        this.mainCharacter = new MainCharacter();
    }

    @Override
    public void update() {
//        mainCharacter.update();
        joyStick.backToCenter();
    }

    @Override
    public void draw(Canvas canvas) {
//        this.mainCharacter.draw(canvas);
        this.joyStick.draw(canvas);
    }

    @Override
    public void terminate() {

    }

    @Override
    public void receiveTouch(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (joyStick.isInRange(x, y)) {
            joyStick.updatePosition(x, y);
        }
    }


}
