package com.cb.adventures.constants;

/**
 * Created by jenics on 2015/10/20.
 */
public class GameConstants {

    public static final int STATE_NONE = -1;
    public static final int STATE_MOVE_LEFT = 0;
    public static final int STATE_MOVE_RIGHT = 1;
    public static final int STATE_ATTACK_LEFT = 2;
    public static final int STATE_ATTACK_RIGHT = 3;
    public static final int STATE_STOP = 4;
    public static final int STATE_JUMP = 5;

    public static int sLeftBoundary;
    public static int sRightBoundary;

    public static int sGameWidth;
    public static int sGameHeight;

    public static final String DIRECTION_CONTROLLER_NAME = "direction_controller.png";
    public static final String DIRECTION_CENTER_NAME = "direction_center.png";
    public static final String DIRECTION_ATTACK_NAME = "controller_attack.png";

    public static final String[] monsterNames = {
            "blackpig.png"
    };
    public static final int BLACK_PIG_ID = 0;

}
