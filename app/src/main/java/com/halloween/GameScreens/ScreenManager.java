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
                return graveyardScreen;
            case MAIN_MENU:
                return mainMenuScreen;
            case PAUSE:
                return pauseScreen;
            case BOSS:
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
        this.getActiveScreen().draw(canvas);
    }


}