package com.cb.adventures.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.cb.adventures.animation.FlashViewAnimation;
import com.cb.adventures.animation.FrameAnimation;
import com.cb.adventures.animation.InjuredValueAnimation;
import com.cb.adventures.animation.ScrollAnimation;
import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.data.GameData;
import com.cb.adventures.data.IPropetry;
import com.cb.adventures.data.PlayerPropetry;
import com.cb.adventures.factory.SkillFactory;
import com.cb.adventures.music.MusicManager;
import com.cb.adventures.skill.Skill;
import com.cb.adventures.state.BaseState;
import com.cb.adventures.state.IStateMgr;
import com.cb.adventures.state.playerstate.AttackState;
import com.cb.adventures.state.playerstate.MoveState;
import com.cb.adventures.state.playerstate.PlayerBaseState;
import com.cb.adventures.state.playerstate.StopState;
import java.util.HashMap;

/**
 * Created by jenics on 2015/10/21.
 * 玩家类
 */
public class Player extends BaseView implements IStateMgr,  Skill.OnSkillAnimationListener, IHurtable ,IPropetry{
    private PlayerPropetry mPropetry;
    private PlayerMediator mPlayerMediator;

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
     * 最后一次受伤害的时间
     */
    private long beInjuredTime = 0;
    /**
     * buff容器，同一个buff不允许同时存在
     * Integer 技能（BUFF)ID
     * SkillPropetry buff属性
     */
    private HashMap<Integer, Skill> bufferMap;
    private Bitmap accackBmp;

    private AttackState.OnAttackListener onAttackListener;

    public Player(PlayerMediator playerMediator) {
        isNeedRepeatAttack = false;
        mPlayerMediator = playerMediator;
        stateHashMap = new HashMap<>();
        bufferMap = new HashMap<>();
        mPropetry = new PlayerPropetry();
    }

    public void setOnAttackListener(AttackState.OnAttackListener onAttackListener) {
        this.onAttackListener = onAttackListener;
    }

    public void setRank(int rank) {
        caclBasePropetry(rank);
    }


    /**
     * 根据等级计算出基础属性
     * @param rank 玩家等级
     */
    public void caclBasePropetry(int rank) {
        mPropetry.setRank(rank);
        mPropetry.setSpeed(16.0f);
        mPropetry.setLevelupExp(GameData.getInstance().getExpTable(rank).getExp());
        if (1 == rank) {
            mPropetry.setBloodTotalVolume(100);
            mPropetry.setBloodVolume(100);
            mPropetry.setMagicTotalVolume(100);
            mPropetry.setMagicVolume(100);
            mPropetry.setAttackPower(50);
            mPropetry.setDefensivePower(30);
            mPropetry.setCriticalRate(3.0f);           ///百分之3的暴击率
            mPropetry.setCriticalDamage(200.0f);       ///百分之200的爆击伤害
        } else {
            int blood = (int) (100.0f + (1251.0f-100.0f)/99.0f * rank);
            int magic = (int) (100.0f + (845.0f-100.0f)/99.0f * rank);
            mPropetry.setBloodTotalVolume(blood);
            mPropetry.setBloodVolume(blood);
            mPropetry.setMagicTotalVolume(magic);
            mPropetry.setMagicVolume(magic);
            mPropetry.setAttackPower(50 + (341 - 50) / 99 * rank);
            mPropetry.setDefensivePower(30 + (246 - 30) / 99 * rank);
            mPropetry.setCriticalRate(3.0f + (10.0f - 3.0f) / 99.0f * rank);
            mPropetry.setCriticalDamage(200.0f + (300.0f - 200.0f) / 99.0f * rank);
        }
    }

    public PlayerPropetry getPropetry() {
        return mPropetry;
    }

    public float getMagicRatio() {

        return mPropetry.getMagicVolume()*1.0f / getMagicTotalVolume();
    }

    public float getBloodRatio() {

        return mPropetry.getBloodVolume()*1.0f / getBloodTotalVolume();
    }

    /**
     * 增加经验
     */
    public void addExp(long exp) {

        mPropetry.setCurExp(mPropetry.getCurExp() + exp);
        if (mPropetry.getCurExp() >= mPropetry.getLevelupExp()) {
            if (mPropetry.getRank() < GameConstants.MAX_LEVEL)
                onLevelUp();
        }
    }

    public void onLevelUp() {
        mPropetry.setRank(mPropetry.getRank() + 1);
        caclBasePropetry(mPropetry.getRank());
        ///当前经验值归零
        mPropetry.setCurExp(0);

        FrameAnimation frameAnimation = new FrameAnimation();
        frameAnimation.setAnimationPropetry(GameData.getInstance().getAnimationPropetry(GameConstants.SKILL_ID_LEVEL_UP));
        frameAnimation.setAttachView(this);
        frameAnimation.startAnimation();

        MusicManager.getInstance().playSound(MusicManager.STATEC_SOUND_TYPE_LEVELUP, 1);
    }

    /**
     * 攻击
     *
     * @param skillId 技能ID
     */
    public boolean attack(int skillId) {
        if (GameConstants.isAttack(curState.getStateId())) {
            /**
             * 攻击状态不要在次攻击
             */
            return false;
        }
        Skill skill = new SkillFactory().create(skillId);
        skill.setCast(GameConstants.CAST_PLAYER);
        if (skill.getSkillPropetry().getFreeMagic() > mPropetry.getMagicVolume()) {
            /**
             * 蓝不够
             */
            return false;
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
        //mPropetry.setMagicVolume(mPropetry.getMagicVolume() - skill.getSkillPropetry().getFreeMagic());

        skill.setAttackPower(skill.getSkillPropetry().getExtraAttack() + getAttackPower());
        skill.setCriticalRate(getCriticalRate());
        skill.setCriticalDamage(getCriticalDamage());
        if (GameConstants.getDirection(curState.getStateId()) == GameConstants.DIRECT_LEFT) {
            changeState(GameConstants.STATE_ATTACK_LEFT);
            skill.setDirection(GameConstants.DIRECT_LEFT);
            skill.setPt(getPt().x - skill.getWidth() / 2, getPt().y);
            skill.startSkill();
        } else if (GameConstants.getDirection(curState.getStateId()) == GameConstants.DIRECT_RIGHT) {
            changeState(GameConstants.STATE_ATTACK_RIGHT);
            skill.setDirection(GameConstants.DIRECT_RIGHT);
            skill.setPt(getPt().x + skill.getWidth() / 2, getPt().y);
            skill.startSkill();
        }

        ScrollAnimation scrollAnimation = new ScrollAnimation();
        scrollAnimation.setPt(GameConstants.sGameWidth / 2, GameConstants.sGameHeight * 0.4f);
        scrollAnimation.setAnimationPropetry(GameData.getInstance().getAnimationPropetry(GameConstants.SKILL_ID_AUTO_SCROLL));
        scrollAnimation.setTimeDuration(800);
        scrollAnimation.setStrTitle(skill.getSkillPropetry().getName());
        scrollAnimation.startAnimation();

        mPlayerMediator.stopScroll();
        return true;
    }

    /**
     * 是否成功移动
     *
     * @param direction 方向
     * @return true可以移动
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
        } else {
            changeState(GameConstants.getDirection(curState.getStateId()) == 0 ?
                    GameConstants.STATE_STOP_LEFT : GameConstants.STATE_STOP_RIGHT);
        }
    }

    public void init(Bitmap bitmap, int frameCount, int perWidth, int perHeght, int leftRowIndex, int rightRowIndex
            , Bitmap attack, int attackWidth, int attackHeight, int attackCount) {
        mBitmap = bitmap;

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
        //super.draw(canvas);

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
                state = new MoveState(stateId, this, frameCount, leftRowIndex, mBitmap, perWidth, perHeight);
                break;
            case GameConstants.STATE_MOVE_RIGHT:
                state = new MoveState(stateId, this, frameCount, rightRowIndex, mBitmap, perWidth, perHeight);
                break;
            case GameConstants.STATE_ATTACK_LEFT:
                state = new AttackState(stateId, this, attackFrameCount, 0, accackBmp, attackPerWidth, attackPerHeight, onAttackListener);
                break;
            case GameConstants.STATE_ATTACK_RIGHT:
                state = new AttackState(stateId, this, attackFrameCount, 0, accackBmp, attackPerWidth, attackPerHeight, onAttackListener);
                break;
            case GameConstants.STATE_STOP_LEFT:
                state = new StopState(stateId, this, leftRowIndex, rightRowIndex, mBitmap, perWidth, perHeight);
                break;
            case GameConstants.STATE_STOP_RIGHT:
                state = new StopState(stateId, this, leftRowIndex, rightRowIndex, mBitmap, perWidth, perHeight);
                break;
            default:
                state = null;
                break;
        }
        return state;
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
            bufferMap.remove(skill.getSkillPropetry().getSkillId());
        }
    }

    @Override
    public void onHurted(Skill skill) {
        /**
         * 两秒内无敌
         */
        if (beInjuredTime != 0 && (System.currentTimeMillis() - beInjuredTime < 2000)) {
            return;
        }

        beInjuredTime = System.currentTimeMillis();

        /**
         * 扣除血量
         */
        int hurt = skill.getSkillPropetry().getExtraAttack() - mPropetry.getDefensivePower();
        if (hurt <= 0) {
            ///防御力大于等于攻击力，强制扣除1血
            hurt = 1;
        }

        mPropetry.setBloodVolume(mPropetry.getBloodVolume() - hurt);
        if (skill.getSkillPropetry().getSkillId() == GameConstants.SKILL_ID_MONSTER_NORMAL) {
            InjuredValueAnimation injuredValueAnimation = new InjuredValueAnimation(this, -hurt, false);
            injuredValueAnimation.startAnimation();
            skill.stopSkill();
        }

        FlashViewAnimation flashAnimation = new FlashViewAnimation(this);
        flashAnimation.setTimeDuration(2000);
        flashAnimation.startAnimation();
        if (mPropetry.getBloodVolume() <= 0) {
            ///游戏结束
        }
    }

    @Override
    public int getAttackPower() {
        return mPropetry.getAttackPower() + mPlayerMediator.getEquipAttackPower();
    }

    @Override
    public int getDefensivePower() {
        return mPropetry.getDefensivePower() + mPlayerMediator.getEquipDefensivePower();
    }

    @Override
    public int getRank() {
        return mPropetry.getRank();
    }

    @Override
    public int getMagicTotalVolume() {
        return mPropetry.getMagicTotalVolume() + mPlayerMediator.getEquipMagicTotalVolume();
    }

    @Override
    public int getBloodTotalVolume() {
        return mPropetry.getBloodTotalVolume() + mPlayerMediator.getEquipBloodTotalVolume();
    }

    @Override
    public float getSpeed() {
        return mPropetry.getSpeed() + mPlayerMediator.getEquipSpeed();
    }

    @Override
    public float getCriticalRate() {
        return mPropetry.getCriticalRate() + mPlayerMediator.getEquipCriticalRate();
    }

    @Override
    public float getCriticalDamage() {
        return mPropetry.getCriticalDamage() + mPlayerMediator.getEquipCriticalDamage();
    }
}
