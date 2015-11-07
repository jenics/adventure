package com.cb.adventures.skill;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.utils.CLog;
import com.cb.adventures.view.BaseView;

/**
 * Created by AI on 2015/10/25.
 */
public class StaticFrameSkill extends Skill{
    public StaticFrameSkill() {

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
            mFrameIndex = 0;
            return true;
        }

        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        float x = getPt().x - disWidth/2;
        float y = getPt().y - disHeight/2;

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
                            width * colIndex,
                            rowIndex * height,
                            width * colIndex + width,
                            rowIndex * height + height),
                    new RectF(x,
                            y,
                            x + disWidth,
                            y + disHeight), null);
        }else if(mDirection == GameConstants.DIRECT_RIGHT) {
            Matrix matrix = new Matrix();
            matrix.postScale(-1, 1); //镜像垂直翻转


            Bitmap bmpTmp2 = Bitmap.createBitmap(mBitmap,
                    width * colIndex,
                    rowIndex * height,
                    width,
                    height,
                    matrix,
                    true);

            ///画技能
            canvas.drawBitmap(bmpTmp2,
                    new Rect(   ///src rect
                            0,0,width,height),
                    new RectF(x,
                            y,
                            x + disWidth,
                            y + disHeight), null);

            bmpTmp2.recycle();
            bmpTmp2 = null;
        }
    }
}
