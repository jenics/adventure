package com.cb.adventures.constants;

import java.security.PublicKey;

/**
 * Created by jenics on 2015/10/20.
 */
public class GameConstants {

    /**
     * 状态，方向
     */
    public static final int DIRECT_NONE = -1;
    public static final int STATE_NONE = -1;
    public static final int DIRECT_LEFT = 0x0;
    public static final int STATE_MOVE_LEFT = 0;
    public static final int DIRECT_RIGHT = 0x1;
    public static final int STATE_MOVE_RIGHT = 1;
    public static final int STATE_ATTACK_LEFT = 2;
    public static final int STATE_ATTACK_RIGHT = 3;
    public static final int STATE_STOP_LEFT = 4;
    public static final int STATE_STOP_RIGHT = 5;


    /**
     * 屏幕属性
     */
    public static int sLeftBoundary;
    public static int sRightBoundary;
    public static int sGameWidth;
    public static int sGameHeight;

    /**
     * 资源名字
     */
    public static final String DIRECTION_CONTROLLER_NAME = "direction_controller.png";
    public static final String DIRECTION_CENTER_NAME = "direction_center.png";
    public static final String DIRECTION_ATTACK_NAME = "controller_attack.png";
    public static final String RED_BLUE_NAME = "redblue.png";
    public static final String RED_BLUE_BOTTOM = "redbluebottom.png";

    public static final String[] monsterNames = {
            "blackpig.png"
    };

    /**
     * 怪物类型
     */
    public static final int BLACK_PIG_ID = 0;

    /**
     * 物品槽
     */
    public static final int EQUIPMENT_NUM = 2;
    public static final int EQUIPMENT_WEAPON = 0;   ///武器
    public static final int EQUIPMENT_CLOTHING = 1; ///衣服

    /**
     * 通过状态码获取方向信息
     * @param state 状态码
     * @return 0 左  1 右
     */
    public static int getDirection(int state) {
        return (state & DIRECT_RIGHT);
    }

    public static boolean isAttack(int state) {
        return state == STATE_ATTACK_LEFT || state == STATE_ATTACK_RIGHT;
    }

    public static boolean isMove(int state) {
        return state == STATE_MOVE_LEFT || state == STATE_MOVE_RIGHT;
    }

}
