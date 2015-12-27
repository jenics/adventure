package com.cb.adventures.skill;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.data.SkillPropetry;
import com.cb.adventures.view.BaseView;

/**
 * Created by AI on 2015/10/25.
 */
public class StaticFrameSkill extends Skill{
    public StaticFrameSkill() {
        isNeedStopLastFrame = false;        ///是否需要停止在最后一帧
        isLoop = false;                     ///是否循环
    }

    private boolean isNeedStopLastFrame;
    private boolean isLoop;

    public boolean isNeedStopLastFrame() {
        return isNeedStopLastFrame;
    }

    public void setIsNeedLastFrame(boolean isNeedLastFrame) {
        this.isNeedStopLastFrame = isNeedLastFrame;
    }

    public void setIsLoop(boolean isLoop) {
        this.isLoop = isLoop;
    }

    @Override
    public void setSkillPropetry(SkillPropetry mSkillPropetry) {
        super.setSkillPropetry(mSkillPropetry);
        if (mSkillPropetry.getAnimationPropetry().getLoopTimes() == -1) {
            setIsLoop(true);
        } else if(mSkillPropetry.getAnimationPropetry().isStopInLast()) {
            setIsNeedLastFrame(true);
        }
    }

    @Override
    public boolean nextFrame() {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastTime < 100) {
            return false;
        }
        mLastTime = nowTime;

        mFrameIndex++;
        if (mFrameIndex >= mFrameCount) {

            if (isLoop) {
                mFrameIndex = 0;
                return false;
            }

            if(isNeedStopLastFrame) {
                mFrameIndex = mFrameCount-1;
                return false;
            } else {
                mFrameIndex = 0;
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



        int rowIndex = getSkillPropetry().getFrames().get(mFrameIndex).getRow();
        int colIndex = getSkillPropetry().getFrames().get(mFrameIndex).getCol();
        ///画攻击效果
        if(mDirection == GameConstants.DIRECT_LEFT) {
            ///画技能
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
        }else if(mDirection == GameConstants.DIRECT_RIGHT) {
            Matrix matrix = new Matrix();
            matrix.postScale(-1, 1); //镜像垂直翻转


            Bitmap bmpTmp2 = Bitmap.createBitmap(mBitmap,
                    mFrameWidth * colIndex,
                    rowIndex * mFrameHeight,
                    mFrameWidth,
                    mFrameHeight,
                    matrix,
                    true);

            ///画技能
            canvas.drawBitmap(bmpTmp2,
                    new Rect(   ///src rect
                            0,0,mFrameWidth,mFrameHeight),
                    new RectF(x,
                            y,
                            x + width,
                            y + height), null);

            bmpTmp2.recycle();
        }
    }
}
