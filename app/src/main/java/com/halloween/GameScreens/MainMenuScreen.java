package com.halloween.GameScreens;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import com.halloween.Constants;
import com.halloween.GameObjects.MainCharacter;
import com.halloween.R;

public class MainMenuScreen implements GameScreen{
    private Bitmap background;
    private Bitmap currentPlayGameButton;
    private Bitmap playGameButton, playGameButtonHover;
    private Point playGameButtonPosition;
    private Paint paint;
    private enum MENU_STATE {LOADING, WAITING, CLOSING}
    private MENU_STATE currentMenuState;

    public MainMenuScreen() {
        this.background = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.main_menu_background);
        this.background = Bitmap.createScaledBitmap(background, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, false);

        this.playGameButton = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.play_button);
        this.playGameButton = Bitmap.createScaledBitmap(playGameButton, Constants.SCREEN_WIDTH/5, Constants.SCREEN_HEIGHT/10, false);

        this.playGameButtonHover = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.play_button_hover);
        this.playGameButtonHover = Bitmap.createScaledBitmap(playGameButtonHover, Constants.SCREEN_WIDTH/5, Constants.SCREEN_HEIGHT/10, false);
        this.playGameButtonPosition = new Point((Constants.SCREEN_WIDTH - playGameButton.getWidth())/2, 3*Constants.SCREEN_HEIGHT/5);

        paint = new Paint();
        this.reset();
    }

    @Override
    public void reset() {
        currentMenuState = MENU_STATE.LOADING;
        paint.setAlpha(0);
        currentPlayGameButton = playGameButton;
    }

    @Override
    public void update() {
        switch (currentMenuState){
            case WAITING:
                break;
            case LOADING:
                this.paint.setAlpha(this.paint.getAlpha() + 8);
                if (this.paint.getAlpha() >= 245){
                    this.paint.setAlpha(255);
                    this.currentMenuState = MENU_STATE.WAITING;
                }
                break;
            case CLOSING:
                this.paint.setAlpha(this.paint.getAlpha() - 15);
                if (this.paint.getAlpha() <= 10){
                    Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.PLAY;
                    this.reset();
                }
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(background, 0, 0 , paint);
        canvas.drawBitmap(currentPlayGameButton, playGameButtonPosition.x, playGameButtonPosition.y, paint);
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
        if (isInRangeOfPlayButton(x, y))
            currentPlayGameButton = playGameButtonHover;
        else
            currentPlayGameButton = playGameButton;

        switch(event.getAction())
        {
            case MotionEvent.ACTION_UP:
                if (isInRangeOfPlayButton(x, y)) {
                    currentMenuState = MENU_STATE.CLOSING;
                }
                else {
                    currentPlayGameButton = playGameButton;
                }
                break;
        }
    }

    public boolean isInRangeOfPlayButton(float x, float y){
        if (
                x > playGameButtonPosition.x &&
                x < playGameButtonPosition.x + currentPlayGameButton.getWidth() &&
                y > playGameButtonPosition.y &&
                y < playGameButtonPosition.y + currentPlayGameButton.getHeight())
            return true;
        else return false;
    }
}