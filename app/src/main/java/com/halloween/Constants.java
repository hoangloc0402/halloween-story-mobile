package com.halloween;

import android.content.Context;

public class Constants {
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    public static Context CURRENT_CONTEXT;
    public enum GAME_STATE {MAIN_MENU, PLAY, PAUSE, GAME_OVER}
    public static GAME_STATE CURRENT_GAME_STATE = GAME_STATE.MAIN_MENU;
    public static GAME_STATE PREVIOUS_GAME_STATE;
    public enum JOYSTICK_STATE {LEFT, RIGHT, MIDDLE}
    public static JOYSTICK_STATE CURRENT_JOYSTICK_STATE = JOYSTICK_STATE.MIDDLE;
    public static boolean JOYSTICK_ATK_STATE = false;
    public static boolean JOYSTICK_JUMP_STATE = false;
    public static final int MAX_HEALTH_MAIN_CHARACTER = 1000;

    public final static int ZOMBIE_ATTACK= 30;
    public final static int ZOMBIE_DAMAGE = 20;
    public final static int ZOMBIE_STARTING_HP = 50;
    public final static int ZOMBIE_POINT = 100;
    public final static float ZOMBIE_V_X = 0.8f;
}
