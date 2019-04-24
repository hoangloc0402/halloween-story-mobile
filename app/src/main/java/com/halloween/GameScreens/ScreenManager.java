package com.halloween.GameScreens;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.halloween.Constants;

public class ScreenManager {
    private PlayingScreen playingScreen;
    private MainMenuScreen mainMenuScreen;


    public ScreenManager() {
        playingScreen = new PlayingScreen();
        mainMenuScreen = new MainMenuScreen();
    }

    public GameScreen getActiveScreen(){
        switch (Constants.CURRENT_GAME_STATE){
            case PLAY:
                return playingScreen;
            case MAIN_MENU:
                return mainMenuScreen;
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