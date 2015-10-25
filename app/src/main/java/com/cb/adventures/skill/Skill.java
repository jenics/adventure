package com.cb.adventures.skill;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.cb.adventures.animation.Animation;
import com.cb.adventures.animation.SkillAnimation;
import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.data.SkillPropetry;
import com.cb.adventures.factory.SkillFactory;
import com.cb.adventures.utils.ImageLoader;
import com.cb.adventures.view.BaseView;

/**
 * 技能类
 * Created by jenics on 2015/10/25.
 */
public class Skill extends BaseView {
    protected int mFrameIndex;
    protected int mFrameCount;
    protected Bitmap mBitmap;
    protected long mLastTime;

    private SkillPropetry mSkillPropetry;
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

    public SkillPropetry getSkillPropetry() {
        return mSkillPropetry;
    }

    public void setSkillPropetry(SkillPropetry mSkillPropetry) {
        this.mSkillPropetry = mSkillPropetry;
        if(mBitmap == null) {
            mBitmap = ImageLoader.getmInstance().loadBitmap(mSkillPropetry.getSrcInfo().getSrcName());
            width = mBitmap.getWidth() / mSkillPropetry.getSrcInfo().getRowFramCount();
            height = mBitmap.getHeight() / mSkillPropetry.getSrcInfo().getColFramCont();
        }

        mFrameCount = mSkillPropetry.getFrames().size();
    }

    public void startSkill() {
        SkillAnimation skillAnimation = new SkillAnimation(this);
        skillAnimation.setOnAnimationListener(new Animation.OnAniamtionListener() {
            @Override
            public void onAnimationEnd(BaseView view) {
                if(getSkillPropetry().getHitEffectId() != 0) {
                    Skill skill = new SkillFactory().create(getSkillPropetry().getHitEffectId());
                    skill.setPt(getPt().x,getPt().y);
                    skill.setDirection(GameConstants.DIRECT_RIGHT);
                    skill.startSkill();
                }
            }

            @Override
            public void onAnimationBegin() {

            }
        });
        skillAnimation.startAnimation();

        mLastTime = System.currentTimeMillis();
        mFrameIndex = 0;
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
