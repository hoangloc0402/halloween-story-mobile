package com.halloween.GameScreens;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import com.halloween.Constants;
import com.halloween.GameContents.JoyStick;
import com.halloween.GameObjects.MainCharacter;
import com.halloween.R;

public class PlayingScreen implements GameScreen{
    private MainCharacter mainCharacter;
    private JoyStick joyStick;
    private Bitmap pauseButton;
    private Point pauseButtonPosition;

    public PlayingScreen() {
        this.pauseButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.pause_button);
        this.pauseButton = Bitmap.createScaledBitmap(pauseButton, 150, 150, false);
        this.pauseButtonPosition = new Point(Constants.SCREEN_WIDTH - 200, 50);

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

        canvas.drawColor(Color.WHITE);
        this.mainCharacter.draw(canvas);
        canvas.drawBitmap(pauseButton, pauseButtonPosition.x, pauseButtonPosition.y, new Paint());
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
        switch(event.getAction())
        {
            case MotionEvent.ACTION_UP:
                if (isInRangeOfPauseButton(x, y)) {
                    Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.PAUSE;
                }
                break;
        }
    }

    public boolean isInRangeOfPauseButton(float x, float y){
        if (
                x >pauseButtonPosition.x &&
                x < pauseButtonPosition.x + pauseButton.getWidth() &&
                y > pauseButtonPosition.y &&
                y < pauseButtonPosition.y + pauseButton.getHeight())
            return true;
        else return false;
    }

}
