package com.halloween.GameScreens;

import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.halloween.Constants;

public class ScreenManager {
    private GraveyardScreen graveyardScreen;
    private MainMenuScreen mainMenuScreen;
    private PauseScreen pauseScreen;
    private BossScreen bossScreen;
    private GameOverScreen gameOverScreen;

    public ScreenManager() {
        mainMenuScreen = new MainMenuScreen();
        graveyardScreen = new GraveyardScreen();
        pauseScreen = new PauseScreen();
        bossScreen = new BossScreen();
        gameOverScreen = new GameOverScreen();
    }

    public GameScreen getActiveScreen(){
        switch (Constants.CURRENT_GAME_STATE){
            case PLAY:
                Constants.isInGraveyard = true;
                return graveyardScreen;
            case MAIN_MENU:
                return mainMenuScreen;
            case PAUSE:
                return pauseScreen;
            case GAME_OVER:
                return gameOverScreen;
            case BOSS:
                Constants.isInGraveyard = false;
                return bossScreen;
            default:
                return null;
        }
    }

    public void receiveTouch(MotionEvent event){
        this.getActiveScreen().receiveTouch(event);
    }

    public void update(){
        this.getActiveScreen().update();
    }

    public void draw(Canvas canvas){
        if (Constants.CURRENT_GAME_STATE == Constants.GAME_STATE.PAUSE || Constants.CURRENT_GAME_STATE == Constants.GAME_STATE.GAME_OVER || Constants.CURRENT_GAME_STATE == Constants.GAME_STATE.WIN) {
            if (Constants.isInGraveyard)
                graveyardScreen.draw(canvas);
            else
                bossScreen.draw(canvas);
            this.getActiveScreen().draw(canvas);
        }
        else
            this.getActiveScreen().draw(canvas);
    }


}