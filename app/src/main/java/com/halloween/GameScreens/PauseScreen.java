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

public class PauseScreen implements GameScreen {
    private Bitmap background;
    private Bitmap currentButtonMainMenu, buttonMainMenu, buttonMainMenuHover;
    private Bitmap currentButtonResume, buttonResume, buttonResumeHover;
    private Bitmap currentButtonExit, buttonExit, buttonExitHover;
    private Point buttonMainMenuPosition;
    private Point buttonResumePosition;
    private Point buttonExitPosition;

    private enum MENU_STATE {LOADING, WAITING, BACK_TO_MAIN, RESUME, EXIT}
    private MENU_STATE currentMenuState;
    private Paint paint;

    public PauseScreen() {
        this.background = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.pause_screen);
        this.background = Bitmap.createScaledBitmap(background, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, false);

        int width = Constants.SCREEN_WIDTH/4;
        int height = Constants.SCREEN_HEIGHT/10;

        this.buttonMainMenu = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.menu);
        this.buttonMainMenu = Bitmap.createScaledBitmap(buttonMainMenu, width, height, false);
        this.buttonMainMenuHover = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.menu_hover);
        this.buttonMainMenuHover = Bitmap.createScaledBitmap(buttonMainMenuHover, width, height, false);

        this.buttonResume = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.resume);
        this.buttonResume = Bitmap.createScaledBitmap(buttonResume, width, height, false);
        this.buttonResumeHover = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.resume_hover);
        this.buttonResumeHover = Bitmap.createScaledBitmap(buttonResumeHover, width, height, false);

        this.buttonExit = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.exit);
        this.buttonExit = Bitmap.createScaledBitmap(buttonExit, width, height, false);
        this.buttonExitHover = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.exit_hover);
        this.buttonExitHover = Bitmap.createScaledBitmap(buttonExitHover, width, height, false);

        this.buttonMainMenuPosition = new Point((Constants.SCREEN_WIDTH - buttonMainMenu.getWidth())/2, 4*Constants.SCREEN_HEIGHT/9);
        this.buttonResumePosition = new Point(buttonMainMenuPosition.x, buttonMainMenuPosition.y + height+ 50);
        this.buttonExitPosition = new Point(buttonMainMenuPosition.x, buttonResumePosition.y + height + 50);
        this.paint = new Paint();
        this.reset();
    }

    @Override
    public void reset() {
        currentMenuState = MENU_STATE.LOADING;
        currentButtonMainMenu = buttonMainMenu;
        currentButtonResume = buttonResume;
        currentButtonExit = buttonExit;
        paint.setAlpha(0);
    }

    @Override
    public void update() {
        switch (currentMenuState){
            case WAITING:
                break;
            case LOADING:
                this.paint.setAlpha(this.paint.getAlpha() + 40);
                if (this.paint.getAlpha() >= 225){
                    this.paint.setAlpha(255);
                    this.currentMenuState = MENU_STATE.WAITING;
                }
                break;
            case BACK_TO_MAIN:
                this.paint.setAlpha(this.paint.getAlpha() - 50);
                if (this.paint.getAlpha() <= 50){
                    Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.MAIN_MENU;
                    this.reset();
                }
                break;
            case RESUME:
                this.paint.setAlpha(this.paint.getAlpha() - 50);
                if (this.paint.getAlpha() <= 50){
                    if (Constants.isInGraveyard)
                        Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.PLAY;
                    else Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.BOSS;
                    this.reset();
                }
                break;
            case EXIT:
                this.paint.setAlpha(this.paint.getAlpha() - 30);
                if (this.paint.getAlpha() <= 30){
                    Constants.MAIN_ACTIVITY.finish();
                }
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, 0, 0, paint);
        canvas.drawBitmap(currentButtonMainMenu, buttonMainMenuPosition.x, buttonMainMenuPosition.y, paint);
        canvas.drawBitmap(currentButtonResume, buttonResumePosition.x, buttonResumePosition.y, paint);
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

        if (isInRangeOfMainMenuButton(x, y))
            currentButtonMainMenu = buttonMainMenu;
        else
            currentButtonMainMenu = buttonMainMenuHover;

        if (isInRangeOfResumeButton(x, y))
            currentButtonResume = buttonResume;
        else
            currentButtonResume = buttonResumeHover;

        if (isInRangeOfExitButton(x, y))
            currentButtonExit = buttonExit;
        else
            currentButtonExit = buttonExitHover;

        switch(event.getAction())
        {
            case MotionEvent.ACTION_UP:
                if (isInRangeOfMainMenuButton(x, y)) {
                    currentMenuState = MENU_STATE.BACK_TO_MAIN;
                }
                else if (isInRangeOfResumeButton(x, y)) {
                    currentMenuState = MENU_STATE.RESUME;
                }
                else if (isInRangeOfExitButton(x, y)) {
                    currentMenuState = MENU_STATE.EXIT;
                }
                break;
        }
    }

    public boolean isInRangeOfMainMenuButton(float x, float y){
        if (
                x > buttonMainMenuPosition.x &&
                x < buttonMainMenuPosition.x + currentButtonMainMenu.getWidth() &&
                y > buttonMainMenuPosition.y &&
                y < buttonMainMenuPosition.y + currentButtonMainMenu.getHeight())
            return true;
        else return false;
    }

    public boolean isInRangeOfResumeButton(float x, float y){
        if (
                x > buttonResumePosition.x &&
                x < buttonResumePosition.x + currentButtonResume.getWidth() &&
                y > buttonResumePosition.y &&
                y < buttonResumePosition.y + currentButtonResume.getHeight())
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
