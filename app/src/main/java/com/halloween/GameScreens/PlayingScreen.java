package com.halloween.GameScreens;

import android.app.usage.ConfigurationStats;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

import com.halloween.Constants;
import com.halloween.GameContents.JoyStick;
import com.halloween.GameObjects.MainCharacter;
import com.halloween.R;

public class PlayingScreen implements GameScreen{
    private MainCharacter mainCharacter;
    private JoyStick joyStick;
    private Bitmap pauseButton;
    private Bitmap jumpButton;
    private Bitmap atkButton;
    private  Bitmap background;
    private Point pauseButtonPosition;
    private Point jumpButtonPosition;
    private Point atkButtonPosition;


    public PlayingScreen() {
        this.background = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.main_menu_background);
        this.background = Bitmap.createScaledBitmap(background, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, false);

        this.pauseButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.pause_button);
        this.pauseButton = Bitmap.createScaledBitmap(pauseButton, 150, 150, false);
        this.pauseButtonPosition = new Point(Constants.SCREEN_WIDTH - 200, 50);

        this.jumpButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.pause_button);
        this.jumpButton = Bitmap.createScaledBitmap(jumpButton, 150, 150, false);
        this.jumpButtonPosition = new Point(Constants.SCREEN_WIDTH - 200, Constants.SCREEN_HEIGHT - 200);

        this.atkButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.pause_button);
        this.atkButton = Bitmap.createScaledBitmap(atkButton, 150, 150, false);
        this.atkButtonPosition = new Point(Constants.SCREEN_WIDTH - 400, Constants.SCREEN_HEIGHT - 200);

        this.reset();
        this.joyStick = new JoyStick();
    }

    public void resume() {
        joyStick.backToCenter();
    }

    @Override
    public void reset() {
        this.mainCharacter = new MainCharacter(50, 50);
    }

    @Override
    public void update() {
        mainCharacter.update();
        joyStick.update();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(background, 0, 0 , new Paint());
        canvas.drawBitmap(pauseButton, pauseButtonPosition.x, pauseButtonPosition.y, new Paint());
        this.mainCharacter.draw(canvas);
        this.joyStick.draw(canvas);
        canvas.drawBitmap(atkButton, atkButtonPosition.x, atkButtonPosition.y, new Paint());
        canvas.drawBitmap(jumpButton, jumpButtonPosition.x, jumpButtonPosition.y, new Paint());
    }

    @Override
    public void terminate() {

    }

    @Override
    public void receiveTouch(MotionEvent event) {
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);
        

        switch(maskedAction)
        {
            case MotionEvent.ACTION_UP:
                Log.d("MOTION:", "ACTION_UP" + x + " " + y );
                if (isInRangeOfPauseButton(x, y)) {
                    joyStick.backToCenter();
                    Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.PAUSE;
                }
                if (joyStick.isInRange(x, y)) {
                    joyStick.backToCenter();
                }
                break;
            case MotionEvent.ACTION_DOWN:
                Log.d("MOTION:", "ACTION_DOWN" + x + " " + y);
                if (joyStick.isInRange(x, y)) {
                    joyStick.updatePosition(x, y);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("MOTION:", "ACTION_MOVE" + x + " " + y);
                if (joyStick.isInRange(x, y)) {
                    joyStick.updatePosition(x, y);
                } else {
                    if (joyStick.isPressed) {
                        joyStick.backToCenter();
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.d("MOTION:", "ACTION_POINTER_DOWN" + x + " " + y);
                if (joyStick.isInRange(x, y)) {
                    joyStick.updatePosition(x, y);
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.d("MOTION:", "ACTION_POINTER_UP" + x + " " + y);
                if (isInRangeOfPauseButton(x, y)) {
                    joyStick.backToCenter();
                    Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.PAUSE;
                }
                if (joyStick.isInRange(x, y)) {
                    joyStick.backToCenter();
                }
                break;
            case MotionEvent.ACTION_OUTSIDE:
                Log.d("MOTION:", "ACTION_OUTSIDE" + x + " " + y);
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.d("MOTION:", "ACTION_CANCEL" + x + " " + y);
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
