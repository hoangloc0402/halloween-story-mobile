package com.halloween;

import android.content.Context;
import android.util.Log;

public class Constants {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static Context CURRENT_CONTEXT;
    public static enum GAME_STATE {MAIN_MENU, PLAY, PAUSE, GAME_OVER, BOSS}
    public static GAME_STATE CURRENT_GAME_STATE = GAME_STATE.PLAY;
    public static GAME_STATE PREVIOUS_GAME_STATE;
    public static enum JOYSTICK_STATE {LEFT, RIGHT, MIDDLE};
    public static JOYSTICK_STATE CURRENT_JOYSTICK_STATE = JOYSTICK_STATE.MIDDLE;
    public static boolean JOYSTICK_ATK_STATE = false;
    public static boolean JOYSTICK_JUMP_STATE = false;
    public static boolean JOYSTICK_TRANSFORM_STATE = false;

    public static float BACKGROUND_X_AXIS = 0.0f;

    public static final int MAX_HEALTH_BOSS = 1000;

    public final static int ZOMBIE_ATTACK= 30;
    public final static int ZOMBIE_DAMAGE = 20;
    public final static float ZOMBIE_STARTING_HP = 50;
    public final static int ZOMBIE_POINT = 100;
    public final static int ZOMBIE_V_X = 2;
    public final static int ZOMBIE_SCALE = 2;
    public final static float ZOMBIE_FOLLOW_DISTANCE = 40000;
    public final static float ZOMBIE_ATTACK_DISTANCE = 10000;

    public final static int GARGOYLE_ATTACK= 30;
    public final static int GARGOYLE_DAMAGE = 20;
    public final static float GARGOYLE_STARTING_HP = 50;
    public final static int GARGOYLE_POINT = 100;
    public final static float GARGOYLE_V_X = 2;
    public final static float GARGOYLE_V_Y = 2;
    public final static int GARGOYLE_SCALE = 2;
    public final static float GARGOYLE_FOLLOW_DISTANCE = 4000000;
    public final static float GARGOYLE_ATTACK_DISTANCE = 10000;

    public static final float MAIN_CHARACTER_V_X = 5f;
    public static final float MAIN_CHARACTER_V_Y = -50f;
    public static final float GRAVITY = 2.5f;
    public static final int  MAIN_CHARACTER_ATTACK_POWER = 10;
    public static final int MAIN_CHARACTER_MAX_SCORE = 1000;

    public static final int MAIN_CHARACTER_MAX_MANA = 1000;
    public static boolean MAIN_CHARACTER_IS_FULL_MANA = false;
    public static final int MANA_INCREASE_SPEED = 40;
    public static final int MANA_DECREASE_SPEED = 5;
    public static final int MAX_HEALTH_MAIN_CHARACTER = 1000;
    public static final long INVINCIBLE_TIME = 4000;
    public static final long BLINK_TIME = 150;

//    public static final long JUMP_TIME = 16;

    public static final int backgroundMapAssetHeight = 578;
    public static final int backgroundBossMapAssetHeight = 780;

    public static final int FIRE_TRAP_DAMAGE = 50;
    public static final int CAMP_FIRE_DAMAGE = 10;
    public static final int SPEAR_DAMAGE = 1000;
    public static final int SPEAR_HORIZONTAL_DAMAGE = 30;
    public static final int SPEAR_VERTICAL_DAMAGE = 30;


    public static float getRelativeXPosition(float x, GAME_STATE game_state)  {
        return getRelativeXPosition(x);
    }

    public static float getRelativeXPosition(float x) {
        switch (CURRENT_GAME_STATE) {
            case PLAY:
                return (x - BACKGROUND_X_AXIS) * SCREEN_WIDTH / (backgroundMapAssetHeight * SCREEN_WIDTH / SCREEN_HEIGHT);
            case BOSS:
                return (x - BACKGROUND_X_AXIS) * SCREEN_WIDTH / (backgroundBossMapAssetHeight * SCREEN_WIDTH / SCREEN_HEIGHT);
            default:
                return -10000;
        }
    }

    public static float getAbsoluteXLength(float x) {
        switch (CURRENT_GAME_STATE) {
            case PLAY:
                return x * (backgroundMapAssetHeight * SCREEN_WIDTH / SCREEN_HEIGHT) / SCREEN_WIDTH;
            case BOSS:
                return x * (backgroundBossMapAssetHeight * SCREEN_WIDTH / SCREEN_HEIGHT) / SCREEN_WIDTH;
            default:
                return 0;
        }
    }
    public static boolean isInScreenRange(float x, float offset, GAME_STATE game_state) {
        return isInScreenRange(x, offset);
    }
    public static boolean isInScreenRange(float x, float offset) {
        GAME_STATE game_state = CURRENT_GAME_STATE;
        float left = getRelativeXPosition(x, game_state);
        float right = getRelativeXPosition(x + offset, game_state);
        switch (game_state) {
            case PLAY:
            case BOSS:
                if (left > 0 || right < SCREEN_WIDTH)
                    return true;
                return false;
            default: return false;
        }
    }
}
