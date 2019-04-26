package com.halloween;

import android.content.Context;

public class Constants {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static Context CURRENT_CONTEXT;
    public static enum GAME_STATE {MAIN_MENU, PLAY, PAUSE, GAME_OVER}
    public static GAME_STATE CURRENT_GAME_STATE = GAME_STATE.MAIN_MENU;
}
