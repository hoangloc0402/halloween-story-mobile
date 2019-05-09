package com.halloween.GameScreens;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import com.halloween.Constants;
import com.halloween.MainActivity;
import com.halloween.R;

public class GameOverScreen implements GameScreen {
    private Bitmap background;
    private Bitmap currentButtonRestart, buttonRestart, buttonRestartHover;
    private Bitmap currentButtonExit, buttonExit, buttonExitHover;
    private Point buttonRestartPosition;
    private Point buttonExitPosition;

    private enum MENU_STATE {LOADING, WAITING, BACK_TO_MAIN, EXIT}
    private MENU_STATE currentMenuState;
    private Paint paint;

    public GameOverScreen() {
        this.background = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.gameover_bg);
        this.background = Bitmap.createScaledBitmap(background, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, false);

        int width = Constants.SCREEN_WIDTH/4;
        int height = Constants.SCREEN_HEIGHT/10;

        this.buttonRestart = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.restart);
        this.buttonRestart = Bitmap.createScaledBitmap(buttonRestart, width, height, false);
        this.buttonRestartHover = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.restart_hover);
        this.buttonRestartHover = Bitmap.createScaledBitmap(buttonRestartHover, width, height, false);

        this.buttonExit = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.exit);
        this.buttonExit = Bitmap.createScaledBitmap(buttonExit, width, height, false);
        this.buttonExitHover = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.exit_hover);
        this.buttonExitHover = Bitmap.createScaledBitmap(buttonExitHover, width, height, false);

        this.buttonRestartPosition = new Point((Constants.SCREEN_WIDTH - buttonRestart.getWidth())/2, (int) (0.61f *Constants.SCREEN_HEIGHT));
        this.buttonExitPosition = new Point(buttonRestartPosition.x, buttonRestartPosition.y + height + 25);
        this.paint = new Paint();
        this.reset();
    }

    @Override
    public void reset() {
        currentMenuState = MENU_STATE.LOADING;
        currentButtonRestart = buttonRestart;
        currentButtonExit = buttonExit;
        paint.setAlpha(0);
    }

    @Override
    public void update() {
        switch (currentMenuState){
            case WAITING:
                break;
            case LOADING:
                this.paint.setAlpha(this.paint.getAlpha() + 10);
                if (this.paint.getAlpha() >= 225){
                    this.paint.setAlpha(255);
                    this.currentMenuState = MENU_STATE.WAITING;
                }
                break;
            case BACK_TO_MAIN:
                this.paint.setAlpha(this.paint.getAlpha() - 10);
                if (this.paint.getAlpha() <= 50){
                    Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.MAIN_MENU;
                    this.reset();
                }
                break;
            case EXIT:
                this.paint.setAlpha(this.paint.getAlpha() - 10);
                if (this.paint.getAlpha() <= 30){
                    Constants.MAIN_ACTIVITY.finish();
                }
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, 0, 0, paint);
        canvas.drawBitmap(currentButtonRestart, buttonRestartPosition.x, buttonRestartPosition.y, paint);
        canvas.drawBitmap(currentButtonExit, buttonExitPosition.x, buttonExitPosition.y, paint);
    }

    @Override
    public void terminate() {

    }

    @Override
    public void receiveTouch(MotionEvent event) {
        if (currentMenuState != MENU_STATE.WAITING)
            return;

        float x = event.getX();
        float y = event.getY();

        if (isInRangeOfRestartButton(x, y))
            currentButtonRestart = buttonRestartHover;
        else
            currentButtonRestart = buttonRestart;

        if (isInRangeOfExitButton(x, y))
            currentButtonExit = buttonExitHover;
        else
            currentButtonExit = buttonExit;

        switch(event.getAction())
        {
            case MotionEvent.ACTION_UP:
                if (isInRangeOfRestartButton(x, y)) {
                    currentMenuState = MENU_STATE.BACK_TO_MAIN;
                }
                else if (isInRangeOfExitButton(x, y)) {
                    currentMenuState = MENU_STATE.EXIT;
                }
                break;
        }
    }

    public boolean isInRangeOfRestartButton(float x, float y){
        if (
                x > buttonRestartPosition.x &&
                        x < buttonRestartPosition.x + currentButtonRestart.getWidth() &&
                        y > buttonRestartPosition.y &&
                        y < buttonRestartPosition.y + currentButtonRestart.getHeight())
            return true;
        else return false;
    }

    public boolean isInRangeOfExitButton(float x, float y){
        if (
                x > buttonExitPosition.x &&
                        x < buttonExitPosition.x + currentButtonExit.getWidth() &&
                        y > buttonExitPosition.y &&
                        y < buttonExitPosition.y + currentButtonExit.getHeight())
            return true;
        else return false;
    }
}
