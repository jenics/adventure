package com.cb.adventures.skill;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.view.Map;

/**
 * Created by AI on 2015/10/27.
 * 移动帧动画
 */
public class MoveFrameSkill extends Skill{
    //技能设置的每次位移距离，也可以直接设置技能需要释放的距离，然后在nextFrame中计算单步位移距离
    private int mSkillMoveStep = 30;
    private int mMoveCount = 0;
    @Override
    public boolean nextFrame() {
        long nowTime = System.currentTimeMillis();

        if(mDirection == GameConstants.DIRECT_LEFT) {
            getPt().x -= mSkillMoveStep;
        } else {
            getPt().x += mSkillMoveStep;
        }
        mMoveCount += mSkillMoveStep;
        if(mMoveCount > getSkillPropetry().getMaxMoveDistance()) {
            return true;
        }

        /**
         * 换帧控制器
         */
        if (nowTime - mLastTime < 90) {
            return false;
        }
        mLastTime = nowTime;

        mFrameIndex++;
        if (mFrameIndex >= mFrameCount) {
            mFrameIndex = 0;

        }
        return false;
    }
    
    @Override
    public void draw(Canvas canvas) {
        float x = getPt().x - width/2;
        float y = getPt().y - height/2;

        PointF ptScreem = Map.toScreemPt(new PointF(x, y));
        x = ptScreem.x;
        y = ptScreem.y;

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
                            0, 0, mFrameWidth, mFrameHeight),
                    new RectF(x,
                            y,
                            x + width,
                            y + height), null);

            bmpTmp2.recycle();
        }
    }
}
