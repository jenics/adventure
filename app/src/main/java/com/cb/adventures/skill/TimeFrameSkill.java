package com.cb.adventures.skill;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.view.BaseView;
import com.cb.adventures.view.Map;

/**
 * Created by jenics on 2015/11/1.
 * 时间帧技能
 */
public class TimeFrameSkill extends Skill {
    @Override
    public boolean nextFrame() {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastTime < 100) {
            return false;
        }
        mLastTime = nowTime;

        mFrameIndex++;
        if (mFrameIndex >= mFrameCount) {
            mFrameIndex = 0;
        }

        if (getSkillPropetry().getTimeDuration() == GameConstants.INFINITE) {
            /**
             * 无限时间
             */
            return false;
        } else if ((nowTime - mBeginTime) > getSkillPropetry().getTimeDuration()){

            return true;
        }

        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        BaseView baseView = mAttachView == null ? this : mAttachView;

        float x = baseView.getPt().x - width / 2;
        float y = baseView.getPt().y - height / 2;

        PointF ptScreem = Map.toScreemPt(new PointF(x, y));
        x = ptScreem.x;
        y = ptScreem.y;

        /**
         * index的有效性确认
         */
        mFrameIndex = Math.min(mFrameIndex, mFrameCount - 1);

        int rowIndex = getSkillPropetry().getFrames().get(mFrameIndex).getRow();
        int colIndex = getSkillPropetry().getFrames().get(mFrameIndex).getCol();
        ///画攻击效果

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

    }
}
