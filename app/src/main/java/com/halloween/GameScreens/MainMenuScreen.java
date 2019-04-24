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
        this.playGameButtonPosition = new Point((background.getWidth() - playGameButton.getWidth())/2, 3*background.getHeight()/5);
        this.reset();
    }

    @Override
    public void reset() {
        currentMenuState = MENU_STATE.LOADING;
        currentPlayGameButton = playGameButton;
        paint = new Paint();
        paint.setAlpha(0);
    }

    @Override
    public void update() {
        switch (currentMenuState){
            case LOADING:
                if (this.paint.getAlpha() < 255){
                    this.paint.setAlpha(this.paint.getAlpha() + 3);
                }
                if (this.paint.getAlpha() >= 255){
                    this.paint.setAlpha(255);
                    this.currentMenuState = MENU_STATE.LOADING;
                }
                break;
            case CLOSING:
                if (this.paint.getAlpha() > 0){
                    this.paint.setAlpha(this.paint.getAlpha() - 5);
                }
                if (this.paint.getAlpha() <= 0){
                    this.reset();
                    Constants.CURRENT_GAME_STATE = Constants.GAME_STATE.PLAY;
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
        float x = event.getX();
        float y = event.getY();
        if (isInRangeOfPlayButton(x, y)) {
            currentPlayGameButton = playGameButtonHover;
        }
        else{
            currentPlayGameButton = playGameButton;
        }

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
        if( x > playGameButtonPosition.x &&
                x < playGameButtonPosition.x + playGameButton.getWidth() &&
                y > playGameButtonPosition.y &&
                y < playGameButtonPosition.y + playGameButton.getHeight() )
            return true;
        else return false;
    }
}