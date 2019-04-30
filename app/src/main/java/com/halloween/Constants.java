package com.halloween;

import android.content.Context;
import android.util.Log;

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

    public static float BACKGROUND_X_AXIS = 0.0f;

    public static final int MAX_HEALTH_MAIN_CHARACTER = 1000;
    public static final int MAX_HEALTH_BOSS = 1000;
    public static final int MAX_SCORE = 1000;

    public final static int ZOMBIE_ATTACK= 30;
    public final static int ZOMBIE_DAMAGE = 20;
    public final static int ZOMBIE_STARTING_HP = 50;
    public final static int ZOMBIE_POINT = 100;
    public final static int ZOMBIE_V_X = 2;
    public final static int ZOMBIE_SCALE = 2;

    public static final float MAIN_CHARACTER_V_X = 10f;
    public static final float MAIN_CHARACTER_V_Y = -50f;
    public static final float GRAVITY = 20f;

    public static int  MAIN_CHARACTER_ATTACK_POWER = 10;
    public static final double INVINCIBLE_TIME = 1500;
    public static final int MAIN_CHARACTER_MAX_SCORE = 2250;

    public static final int backgroundMapAssetHeight = 578;
    public static float getRelativeXPosition(float x, GAME_STATE game_state) {
        switch (game_state) {
            case PLAY:
                return (x - BACKGROUND_X_AXIS) * SCREEN_WIDTH / (backgroundMapAssetHeight * SCREEN_WIDTH / SCREEN_HEIGHT);
            default:
                return -10000;
        }
    }
    public static boolean isInScreenRange(float x, float offset, GAME_STATE game_state) {
        float left = getRelativeXPosition(x, game_state);
        float right = getRelativeXPosition(x + offset, game_state);
        Log.d("range",left+ " "+right);
        switch (game_state) {
            case PLAY:
                if (left > 0 || right < SCREEN_WIDTH)
                    return true;
                return false;
            default: return false;
        }
    }
}
