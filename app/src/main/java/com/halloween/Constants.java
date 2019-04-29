package com.halloween;

import android.content.Context;

public class Constants {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static Context CURRENT_CONTEXT;
    public static enum GAME_STATE {MAIN_MENU, PLAY, PAUSE, GAME_OVER}
    public static GAME_STATE CURRENT_GAME_STATE = GAME_STATE.PLAY;
    public static GAME_STATE PREVIOUS_GAME_STATE;
    public static enum JOYSTICK_STATE {LEFT, RIGHT, MIDDLE};
    public static JOYSTICK_STATE CURRENT_JOYSTICK_STATE = JOYSTICK_STATE.MIDDLE;
    public static boolean JOYSTICK_ATK_STATE = false;
    public static boolean JOYSTICK_JUMP_STATE = false;

    public static final int MAX_HEALTH_MAIN_CHARACTER = 1000;

    public static final float MAIN_CHARACTER_V_X = 10f;
    public static final float MAIN_CHARACTER_V_Y = 21f;
    public static final float MAIN_CHARACTER_JUMP_HEIGHT = 300f;
    public static final float GRAVITY = 9.81f;

    public static int  MAIN_CHARACTER_ATTACK_POWER = 10;
    public static final double INVINCIBLE_TIME = 1500;
    public static final int MAIN_CHARACTER_MAX_SCORE = 2250;
}
