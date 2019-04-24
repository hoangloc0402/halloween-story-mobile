package com.halloween.GameScreens;

import android.graphics.Canvas;
import android.view.MotionEvent;

public class ScreenManager {
    public static String ACTIVE_SCENE;
    private PlayingScreen mainScreen;


    public ScreenManager() {
        ACTIVE_SCENE = "mainScreen";
        mainScreen = new PlayingScreen();
    }

    public GameScreen getScreenByName(String name){
        switch (name){
            case "mainScreen":
                return  mainScreen;
            default:
                return null;
        }
    }

    public void receiveTouch(MotionEvent event){
        this.getScreenByName(ACTIVE_SCENE).receiveTouch(event);
    }

    public void update(){
        this.getScreenByName(ACTIVE_SCENE).update();
    }

    public void draw(Canvas canvas){
        this.getScreenByName(ACTIVE_SCENE).draw(canvas);
    }


}