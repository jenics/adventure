package com.cb.adventures.constants;

/**
 * Created by jenics on 2015/10/20.
 */
public class GameConstants {

    /**
     * 由于所有的素材都是以任务105*105为比例缩放的
     * 所以要计算
     */
    public static float zoomRatio;

    /**
     * 状态，每个状态都具有方向信息
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
     * 控制器
     */
    public static final int CONTROL_LEFT = 0;
    public static final int CONTROL_RIGHT = 1;
    public static final int CONTROL_ATTACK = 2;
    public static final int CONTROL_SPECIAL_KEY_0 = 3;
    public static final int CONTROL_SPECIAL_KEY_1 = 4;
    public static final int CONTROL_SPECIAL_KEY_2 = 5;
    public static final int CONTROL_SPECIAL_KEY_3 = 6;

    /**
     * 功能键类型
     */
    public static final int FUNCTION_TYPE_CONSUMABLE = 0;    ///消耗品
    public static final int FUNCTION_TYPE_SKILL = 1;        ///技能

    /**
     * 时间
     */
    public static final long TIME_INFINITE = -1;    ///无限时间

    public static final long INFINITE = -1;    ///无限

    /**
     * 屏幕属性
     */
    public static int sLeftBoundary;
    public static int sRightBoundary;
    public static int sGameWidth;
    public static int sGameHeight;
    /**
     * 玩家和怪物在屏幕的位置比例
     */
    public final static float sYpointRatio = 0.6f;

    /**
     * 地图底部占比
     */
    public final static float sBottomRatio = 0.714f;

    /**
     * 资源名字
     */
    public static final String DIRECTION_CONTROLLER_NAME = "ui/direction_controller.png";
    public static final String DIRECTION_CENTER_NAME = "ui/direction_center.png";
    public static final String DIRECTION_ATTACK_NAME = "ui/controller_attack.png";
    public static final String RED_BLUE_NAME = "ui/redblue.png";
    public static final String RED_BLUE_BOTTOM = "ui/redbluebottom.png";
    public static final String GAME_SKILL_NAME = "ui/skill_back.png";
    public static final String INVENTORY_NAME = "ui/inventory.jpg";
    public static final String INVENTORY_SELECTED_NAME = "ui/selected.png";
    public static final String MAP_BOTTOM_NAME = "map/bottom.jpg";

    public static final String PLAYER1_ATTACK_NAME = "player/attack.png";
    public static final String PLAYER1_NAME = "player/xunlei.png";


    public static final String[] monsterNames = {
            "blackpig.png"
    };

    /**
     * 怪物类型
     */
    public static final int BLACK_PIG_ID = 1;

    /**
     * 物品槽
     */
    public static final int EQUIPMENT_NUM = 2;
    public static final int EQUIPMENT_WEAPON = 0;   ///武器
    public static final int EQUIPMENT_CLOTHING = 1; ///衣服

    /**
     * 动画类型
     */
    public static final int ANIMATION_STATIC_FRAME = 1;               ///静止帧动画，在固定坐标静静播
    public static final int ANIMATION_MOVE_FRAME = 2;                 ///移动帧动画，到指定距离结束
    public static final int ANIMATION_STATIC_TIME_FRAME = 3;          ///时间静止帧限制动画，超过限定时间结束

    /**
     * 技能分类
     */
    public static final int SKILL_TYPE_ACTIVE_ATTACK = 0;    ///主动攻击类技能
    public static final int SKILL_TYPE_PASSIVE = 1;          ///被动技能
    public static final int SKILL_TYPE_BUFF = 2;             ///BUFF技能
    public static final int SKILL_TYPE_DEBUFF = 2;           ///DEBUFF技能
    public static final int SKILL_TYPE_ANIMATION = 3;       ///不产生任何作用，仅仅是动画

    /**
     * 技能作用目标
     */
    public static final int EFFECT_TARGET_SELF = 1;     ///作用自身
    public static final int EFFECT_TARGET_FRIENDLY = 2; ///作用友方
    public static final int EFFECT_TARGET_ENEMY = 4;    ///作用敌方

    /**
     * 技能ID映射，在skill.xml中skillId标签中使用
     */
    public static final int SKILL_ID_NORMAL = 1;                ///迅雷普通攻击
    public static final int SKILL_ID_HIT_EFFECTIVE_NORMAL = 2;  ///迅雷普通攻击击中效果
    public static final int SKILL_ID_BINGHJIAN = 3;             ///冰剑
    public static final int SKILL_ID_BUFF_1 = 4;                ///BUFF1
    public static final int SKILL_ID_HUOJIAN = 5;               ///火剑
    public static final int SKILL_ID_SHENGJIAN = 6;             ///圣剑
    public static final int SKILL_ID_RENDAOFEIBIAO = 7;         ///忍道飞镖
    public static final int SKILL_ID_HIT_EFFECTIVE_RENDAO = 8;  ///忍道飞镖击中效果
    public static final int SKILL_ID_LEVEL_UP = 9;              ///升级效果
    public static final int SKILL_ID_XIAOFEIBIAO = 10;           ///小飞镖
    public static final int SKILL_ID_DEAD = 11;                 ///死亡
    public static final int SKILL_ID_REVIVE = 12;               ///复活
    public static final int SKILL_ID_TRANSFER_MATRIX = 13;      ///关卡
    public static final int SKILL_ID_AUTO_SCROLL = 14;          ///自动滚动

    /**
     * 技能施放者标识
     */
    public static final int CAST_PLAYER = 0;
    public static final int CAST_MONSTER = 1;

    public static boolean isTargetSelf(int target) {
        return ((target & EFFECT_TARGET_SELF) > 0);
    }

    public static boolean isTargetFriend(int target) {
        return ((target & EFFECT_TARGET_FRIENDLY) > 0);
    }

    public static boolean isTargetEnemy(int target) {
        return ((target & EFFECT_TARGET_ENEMY) > 0);
    }

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
