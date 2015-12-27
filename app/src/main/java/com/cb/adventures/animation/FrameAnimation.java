package com.cb.adventures.animation;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.data.AnimationPropetry;
import com.cb.adventures.utils.ImageLoader;
import com.cb.adventures.view.BaseView;

/**
 * Created by jenics on 2015/11/29.
 */
public class FrameAnimation extends SelfAnimation {
    protected int mFrameIndex;
    protected int mFrameCount;
    protected long mLastTime;

    protected boolean mIsStopInLast;          ///完成后是否需要停止在最后一帧
    protected int mCurrentLoopTimes;

    private final int mSkillMoveStep = 30;
    private int mMoveCount = 0;

    /**
     * 一帧的宽度，不带表实际宽度
     */
    protected int mFrameWidth;
    /**
     * 一帧的高度，不代表实际高度
     */
    protected int mFrameHeight;

    protected BaseView mAttachView;     ///挂靠的目标

    private AnimationPropetry mAnimationPropetry;

    protected int mDirection;

    public int getDirection() {
        return mDirection;
    }

    public void setDirection(int mDirection) {
        this.mDirection = mDirection;
    }

    public FrameAnimation() {
        mAnimationPropetry = new AnimationPropetry();
        mFrameIndex = 0;
        mCurrentLoopTimes = 0;
    }

    public void setAttachView(BaseView mAttachView) {
        this.mAttachView = mAttachView;
    }

    public AnimationPropetry getAnimationPropetry() {
        return mAnimationPropetry;
    }

    public void setAnimationPropetry(AnimationPropetry animationPropetry) {
        this.mAnimationPropetry = animationPropetry;
        if(mBitmap == null) {
            mBitmap = ImageLoader.getmInstance().loadBitmap(mAnimationPropetry.getSrcInfo().getSrcName());
            mFrameWidth = mBitmap.getWidth() / mAnimationPropetry.getSrcInfo().getColFramCont();
            mFrameHeight = mBitmap.getHeight() / mAnimationPropetry.getSrcInfo().getRowFramCount();
            width = (int) (mFrameWidth* GameConstants.zoomRatio) ;
            height = (int) (mFrameHeight*GameConstants.zoomRatio);
        }

        mFrameCount = mAnimationPropetry.getFrames().size();
    }

    @Override
    public boolean nextFrame() {
        long nowTime = System.currentTimeMillis();

        /**
         * 距离限制动画
         */
        if (getAnimationPropetry().getMaxMoveDistance() > 0) {
            if(mDirection == GameConstants.DIRECT_LEFT) {
                getPt().x -= mSkillMoveStep;
            } else {
                getPt().x += mSkillMoveStep;
            }
            mMoveCount += mSkillMoveStep;
            if(mMoveCount > getAnimationPropetry().getMaxMoveDistance()) {
                return true;
            }
        }

        /**
         * 时间限制动画
         */
        if (getAnimationPropetry().getTimeDuration() > 0) {
            if ((nowTime - mBeginTime) > getAnimationPropetry().getTimeDuration()) {
                return true;
            }
        }

        if (nowTime - mLastTime < 100) {
            return false;
        }
        mLastTime = nowTime;

        mFrameIndex++;
        if (mFrameIndex >= mFrameCount) {
            mCurrentLoopTimes ++;
            mFrameIndex = 0;
            if (mAnimationPropetry.getLoopTimes() == GameConstants.INFINITE) {  ///无限循环
                return false;
            }
            ///如果要停在最后一帧
            if(mIsStopInLast) {
                mFrameIndex = mFrameCount-1;
                return false;
            } else if(mCurrentLoopTimes >= mAnimationPropetry.getLoopTimes()){  ///如果已经达到循环次数，跳出
                return true;
            }
        }
        return false;
    }


    @Override
    public void draw(Canvas canvas) {
        BaseView baseView = mAttachView == null ? this : mAttachView;
        float x = baseView.getPt().x - width / 2;
        float y = baseView.getPt().y - height / 2;

        /**
         * index的有效性确认
         */
        mFrameIndex = Math.min(mFrameIndex,mFrameCount-1);

        int rowIndex = getAnimationPropetry().getFrames().get(mFrameIndex).getRow();
        int colIndex = getAnimationPropetry().getFrames().get(mFrameIndex).getCol();

        canvas.drawBitmap(mBitmap,
                new Rect(   ///src rect
                        mFrameWidth * colIndex,
                        rowIndex * mFrameHeight,
                        mFrameWidth * colIndex + mFrameWidth,
                        rowIndex * mFrameHeight + mFrameHeight),
                new RectF(x,
                        y,
                        x + width,
                        y + height), null);
    }
}
