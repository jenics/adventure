package com.cb.adventures.skill;

import android.graphics.Canvas;

import com.cb.adventures.animation.FrameAnimation;
import com.cb.adventures.animation.ViewAnimation;
import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.data.SkillPropetry;
import com.cb.adventures.utils.ImageLoader;
import com.cb.adventures.view.BaseView;


/**
 * 技能类
 * Created by jenics on 2015/10/25.
 */
public class Skill extends FrameAnimation implements ViewAnimation.OnAniamtionListener{
    /**
     * 技能施放方
     */
    protected int cast;
    /**
     * 施放者ID
     * 如果技能释放方是玩家，这个字段代表玩家ID
     * 如果技能施放方是怪物，这个字段代表怪物ID
     */
    protected int castId;

    public int getCast() {
        return cast;
    }

    public void setCast(int cast) {
        this.cast = cast;
    }

    public int getCastId() {
        return castId;
    }

    public void setCastId(int castId) {
        this.castId = castId;
    }

    /**
     * 攻击力
     */
    private int attackPower;

    /**
     * 暴击率
     */
    private float criticalRate;
    /**
     * 暴击伤害
     */
    private float criticalDamage;

    public float getCriticalRate() {
        return criticalRate;
    }

    public void setCriticalRate(float criticalRate) {
        this.criticalRate = criticalRate;
    }

    public float getCriticalDamage() {
        return criticalDamage;
    }

    public void setCriticalDamage(float criticalDamage) {
        this.criticalDamage = criticalDamage;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    /**
     * 技能挂靠的目标
     */
    protected BaseView mAttachView;

    private SkillPropetry mSkillPropetry;


    public interface OnSkillAnimationListener {
        void onSkillBegin(Skill skill);
        void onSkillEnd(Skill skill,boolean isForce);
    }
    private OnSkillAnimationListener listener;

    public OnSkillAnimationListener getListener() {
        return listener;
    }

    public void setListener(OnSkillAnimationListener listener) {
        this.listener = listener;
    }

    /**
     * 静态ID，用来产生技能伤害ID
     */
    public static int sHurtId = 0;
    /**
     * 技能伤害ID，一个技能对象一个，每个实例对象都不同
     */
    protected int mHurtId;

    /**
     * 是否已经作用过该伤害
     */
    protected boolean isHurted;


    /**
     * 技能方向
     */
    protected int mDirection;

    public int getDirection() {
        return mDirection;
    }

    public void setDirection(int mDirection) {
        this.mDirection = mDirection;
    }

    public Skill() {
        sHurtId ++;
        mHurtId = sHurtId;
        mSkillPropetry = new SkillPropetry();
        mFrameIndex = 0;
        isHurted = false;
    }

    public BaseView getAttachView() {
        return mAttachView;
    }

    public void setAttachView(BaseView mAttachView) {
        this.mAttachView = mAttachView;
    }

    public SkillPropetry getSkillPropetry() {
        return mSkillPropetry;
    }

    public void setSkillPropetry(SkillPropetry mSkillPropetry) {
        this.mSkillPropetry = mSkillPropetry;
        if(mBitmap == null && mSkillPropetry.getAnimationPropetry() != null) {
            mBitmap = ImageLoader.getInstance().loadBitmap(mSkillPropetry.getSrcInfo().getSrcName());
            mFrameWidth = mBitmap.getWidth() / mSkillPropetry.getSrcInfo().getColFramCont();
            mFrameHeight = mBitmap.getHeight() / mSkillPropetry.getSrcInfo().getRowFramCount();

            width = (int) (mFrameWidth* GameConstants.zoomRatio) ;
            height = (int) (mFrameHeight*GameConstants.zoomRatio);

            mFrameCount = mSkillPropetry.getFrames().size();
        }

    }


    public void startSkill() {
        startAnimation();
        mBeginTime = mLastTime = System.currentTimeMillis();
        mFrameIndex = 0;
    }

    /**
     * 停止技能，将会从动画列表中移除
     */
    public void stopSkill() {
        stopAnimation();
    }


    @Override
    public void draw(Canvas canvas) {
        //super.draw(canvas);
    }

    @Override
    public void onAnimationEnd(BaseView view,boolean isForce) {
        if(listener != null) {
            listener.onSkillEnd(this,isForce);
        }
    }

    @Override
    public void onAnimationBegin() {
        if(listener != null) {
            listener.onSkillBegin(this);
        }
    }

    public int getHurtId() {
        return mHurtId;
    }

    public void setHurtId(int mHurtId) {
        this.mHurtId = mHurtId;
    }

    public boolean isHurted() {
        return isHurted;
    }

    public void setIsHurted(boolean isHurted) {
        this.isHurted = isHurted;
    }


    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }
}
