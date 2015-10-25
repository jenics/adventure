package com.cb.adventures.skill;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.constants.GameConstants;

/**
 * Created by jenics on 2015/10/25.
 */
public class HitEffectSkill extends Skill {
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
            return true;
        }

        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        float x = getPt().x - width / 2;
        float y = getPt().y - height / 2;

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
                        width * colIndex,
                        rowIndex * height,
                        width * colIndex + width,
                        rowIndex * height + height),
                new RectF(x,
                        y,
                        x + width,
                        y + height), null);

    }
}
