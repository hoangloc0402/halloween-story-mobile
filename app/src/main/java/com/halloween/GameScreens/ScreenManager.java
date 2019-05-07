package com.halloween.GameScreens;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.halloween.Constants;

public class ScreenManager {
    private GraveyardScreen graveyardScreen;
    private MainMenuScreen mainMenuScreen;
    private PauseScreen pauseScreen;
    private BossScreen bossScreen;

    public ScreenManager() {
        graveyardScreen = new GraveyardScreen();
        mainMenuScreen = new MainMenuScreen();
        pauseScreen = new PauseScreen();
        bossScreen = new BossScreen();
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
        if (Constants.CURRENT_GAME_STATE == Constants.GAME_STATE.PAUSE) {
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