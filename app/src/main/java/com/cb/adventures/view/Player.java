package com.cb.adventures.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.data.EquipmentPropetry;
import com.cb.adventures.data.Propetry;
import com.cb.adventures.factory.SkillFactory;
import com.cb.adventures.skill.Skill;
import com.cb.adventures.state.BaseState;
import com.cb.adventures.state.IStateMgr;
import com.cb.adventures.state.playerstate.AttackState;
import com.cb.adventures.state.playerstate.MoveState;
import com.cb.adventures.state.playerstate.PlayerBaseState;
import com.cb.adventures.state.playerstate.StopState;
import com.cb.adventures.utils.CLog;

import java.util.HashMap;

/**
 * Created by jenics on 2015/10/21.
 */
public class Player extends BaseView implements IStateMgr, AttackState.OnAttackListener, Skill.OnSkillAnimationListener {
    private Propetry mPropetry;
    protected PlayerBaseState curState;
    protected HashMap<Integer, PlayerBaseState> stateHashMap;
    protected int frameCount;               ///一个方向的帧总数
    protected int perWidth;                 ///每一帧的宽度
    protected int perHeight;                ///每一帧的高度

    protected int attackPerWidth;           ///每一个攻击帧宽度
    protected int attackPerHeight;          ///每一个攻击帧的高度

    protected boolean isNeedRepeatAttack;   ///是否需要重复攻击
    protected int attackFrameCount;         ///攻击总帧数
    private long lastTime;
    private int leftRowIndex = 0;           ///方向左在第几行
    private int rightRowIndex = 1;          ///方向右在第几行

    /**
     * buff容器，同一个buff不允许同时存在
     * Integer 技能（BUFF)ID
     * SkillPropetry buff属性
     */
    private HashMap<Integer, Skill> bufferMap;

    private EquipmentPropetry[] mEquipmentPropetrys;

    private Bitmap bmp;
    private Bitmap accackBmp;

    public Player() {
        isNeedRepeatAttack = false;
        stateHashMap = new HashMap<>();
        bufferMap = new HashMap<>();
        mPropetry = new Propetry();
        mEquipmentPropetrys = new EquipmentPropetry[GameConstants.EQUIPMENT_NUM];
    }

    public Propetry getmPropetry() {
        return mPropetry;
    }

    /**
     * 攻击
     *
     * @param skillId 技能ID
     */
    public void attack(int skillId) {
        if (GameConstants.isAttack(curState.getStateId())) {
            /**
             * 攻击状态不要在次攻击
             */
            return;
        }
        Skill skill = new SkillFactory().create(skillId);

        if (skill.getSkillPropetry().getFreeMagic() > mPropetry.getMagicVolume()) {
            /**
             * 蓝不够
             */
            return;
        }

        if (skill.getSkillPropetry().getSkillType() == GameConstants.SKILL_TYPE_BUFF
                || skill.getSkillPropetry().getSkillType() == GameConstants.SKILL_TYPE_DEBUFF) {
            ///BUFF技能已作用
            Skill skillTmp = bufferMap.get(skillId);
            if (skillTmp != null) {
                skillTmp.stopSkill();///该技能已作用,停止旧的
            }
            skill.setAttachView(this);
        }

        /**
         * 扣除魔法值
         */
        mPropetry.setMagicVolume(mPropetry.getMagicVolume() - skill.getSkillPropetry().getFreeMagic());

        if (GameConstants.getDirection(curState.getStateId()) == GameConstants.DIRECT_LEFT) {
            changeState(GameConstants.STATE_ATTACK_LEFT);
            skill.setDirection(GameConstants.DIRECT_LEFT);
            skill.setPt(getPt().x, getPt().y);
            skill.startSkill();
        } else if (GameConstants.getDirection(curState.getStateId()) == GameConstants.DIRECT_RIGHT) {
            changeState(GameConstants.STATE_ATTACK_RIGHT);
            skill.setDirection(GameConstants.DIRECT_RIGHT);
            skill.setPt(getPt().x, getPt().y);
            skill.startSkill();
        }
    }

    /**
     * 是否成功移动
     *
     * @param direction 方向
     * @return
     */
    public boolean move(int direction) {
        if (GameConstants.isAttack(curState.getStateId())) {
            /**
             * 正在攻击中不允许移动
             */
            return false;
        }
        if (direction == curState.getStateId()) {
            /**
             * 相同的方向就不要移动了。
             */
            return false;
        }
        lastTime = System.currentTimeMillis();
        changeState(direction);
        return true;
    }

    /**
     * 停止
     */
    public void stop() {
        if (curState == null) {
            /**
             * curState为null,说明游戏开始，默认方向为右
             */
            changeState(GameConstants.STATE_STOP_RIGHT);
        } else if (GameConstants.isAttack(curState.getStateId())) {
            /**
             * 正在攻击中，不允许变化，攻击结束，自己会changestate到stop态
             */
            return;
        } else {
            changeState(GameConstants.getDirection(curState.getStateId()) == 0 ?
                    GameConstants.STATE_STOP_LEFT : GameConstants.STATE_STOP_RIGHT);
        }
    }

    public void init(Bitmap bitmap, int frameCount, int perWidth, int perHeght, int leftRowIndex, int rightRowIndex
            , Bitmap attack, int attackWidth, int attackHeight, int attackCount) {
        bmp = bitmap;

        this.frameCount = frameCount;
        this.perWidth = perWidth;
        this.perHeight = perHeght;
        this.leftRowIndex = leftRowIndex;
        this.rightRowIndex = rightRowIndex;

        accackBmp = attack;
        this.attackPerWidth = attackWidth;
        this.attackPerHeight = attackHeight;
        this.attackFrameCount = attackCount;

        pt.x = GameConstants.sGameWidth / 2;
        pt.y = GameConstants.sGameHeight * GameConstants.sYpointRatio;

        stop();
    }

    @Override
    public void draw(Canvas canvas) {
        if (curState == null)
            return;
        super.draw(canvas);

        long nowTime = System.currentTimeMillis();
        if (nowTime - lastTime < 100) {
            curState.draw(canvas);
            return;
        } else {
            curState.nextFrame();
            curState.draw(canvas);
        }
        lastTime = nowTime;
    }

    @Override
    public void changeState(int stateId) {
        CLog.d("changestate:", String.format("%d", stateId));
        PlayerBaseState disState = stateHashMap.get(stateId);

        if (curState == null || disState == null) {
            disState = (PlayerBaseState) createState(stateId);
            if (disState == null) {
                throw new IllegalStateException("create state failed!!!");
            }
            stateHashMap.put(stateId, disState);
            disState.entry();
            curState = disState;
        } else {
            if (stateId == curState.getStateId()) {
                /**
                 * 自己进入自己状态，直接返回
                 */
                return;
            }
            curState.leave();
            disState.entry();
            curState = disState;
        }
    }


    @Override
    public BaseState createState(int stateId) {
        BaseState state;
        switch (stateId) {
            case GameConstants.STATE_MOVE_LEFT:
                state = new MoveState(stateId, this, frameCount, leftRowIndex, bmp, perWidth, perHeight);
                break;
            case GameConstants.STATE_MOVE_RIGHT:
                state = new MoveState(stateId, this, frameCount, rightRowIndex, bmp, perWidth, perHeight);
                break;
            case GameConstants.STATE_ATTACK_LEFT:
                state = new AttackState(stateId, this, attackFrameCount, 0, accackBmp, attackPerWidth, attackPerHeight, this);
                break;
            case GameConstants.STATE_ATTACK_RIGHT:
                state = new AttackState(stateId, this, attackFrameCount, 0, accackBmp, attackPerWidth, attackPerHeight, this);
                break;
            case GameConstants.STATE_STOP_LEFT:
                state = new StopState(stateId, this, leftRowIndex, rightRowIndex, bmp, perWidth, perHeight);
                break;
            case GameConstants.STATE_STOP_RIGHT:
                state = new StopState(stateId, this, leftRowIndex, rightRowIndex, bmp, perWidth, perHeight);
                break;
            default:
                state = null;
                break;
        }
        return state;
    }

    @Override
    public void onAttackOver() {

    }

    /**
     * 装备物品
     *
     * @param equipmentId 装备ID
     */
    public void equipment(int equipmentId) {
        ///重新计算属性值
    }

    /**
     * 卸下物品
     *
     * @param equipmentId 装备ID
     */
    public void unEquipment(int equipmentId) {
        ///重新计算属性值
    }

    /**
     * 使用消耗品
     *
     * @param consumId 消耗品ID
     */
    public void use(int consumId) {
        ///增加物品增益效果
    }

    @Override
    public void onSkillBegin(Skill skill) {

    }

    @Override
    public void onSkillEnd(Skill skill, boolean isForce) {
        if (skill.getSkillPropetry().getSkillType() == GameConstants.SKILL_TYPE_BUFF
                || skill.getSkillPropetry().getSkillType() == GameConstants.SKILL_TYPE_DEBUFF) {
            /**
             * 卸下该属性,属性变化。
             */

            /**
             * 非强制停止的动画，才需要进行加减属性，否则外部已经处理过了
             */
            if (isForce) {
                ///非强制
                ///设置属性之类的。
                ///mPropetry.setDefensivePower(0);
            }

            bufferMap.remove(skill);


        }
    }
}
