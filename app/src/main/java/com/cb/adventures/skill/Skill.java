package com.cb.adventures.skill;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.cb.adventures.animation.Animation;
import com.cb.adventures.animation.SkillAnimationProxy;
import com.cb.adventures.data.SkillPropetry;
import com.cb.adventures.utils.ImageLoader;
import com.cb.adventures.view.BaseView;

/**
 * 技能类
 * Created by jenics on 2015/10/25.
 */
public class Skill extends BaseView implements Animation.OnAniamtionListener{
    protected int mFrameIndex;
    protected int mFrameCount;
    protected Bitmap mBitmap;
    protected long mLastTime;
    protected long mBeginTime;  ///开始时间

    protected BaseView mAttachView;     ///挂靠的目标

    private SkillAnimationProxy proxy;

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
    }

    public BaseView getmAttachView() {
        return mAttachView;
    }

    public void setmAttachView(BaseView mAttachView) {
        this.mAttachView = mAttachView;
    }

    public SkillPropetry getSkillPropetry() {
        return mSkillPropetry;
    }

    public void setSkillPropetry(SkillPropetry mSkillPropetry) {
        this.mSkillPropetry = mSkillPropetry;
        if(mBitmap == null) {
            mBitmap = ImageLoader.getmInstance().loadBitmap(mSkillPropetry.getSrcInfo().getSrcName());
            width = mBitmap.getWidth() / mSkillPropetry.getSrcInfo().getColFramCont();
            height = mBitmap.getHeight() / mSkillPropetry.getSrcInfo().getRowFramCount();
        }

        mFrameCount = mSkillPropetry.getFrames().size();
    }


    public void startSkill() {
        proxy = new SkillAnimationProxy(this);
        proxy.setOnAnimationListener(this);
        proxy.startAnimation();
        mBeginTime = mLastTime = System.currentTimeMillis();
        mFrameIndex = 0;
    }

    /**
     * 停止技能，将会从动画列表中移除
     */
    public void stopSkill() {
        if(proxy != null) {
            proxy.stopAnimation();
            proxy = null;
        }
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
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
}
