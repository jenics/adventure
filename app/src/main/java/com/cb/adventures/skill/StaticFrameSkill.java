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
    private int mDirection;
    private Bitmap mBitmap;
    private int mFrameCount;
    private int mRowIndex;
    private int mFrameIndex;

    public StaticFrameSkill(int skillKind, float x, float y,int direction,
                            Bitmap bitmap,int frameCount,
                            int rowIndex) {
        super(skillKind);
        mDirection = direction;
        mBitmap = bitmap;
        mFrameCount = frameCount;
        mRowIndex = rowIndex;

        pt.x = x;
        pt.y = y;

        this.width = bitmap.getWidth()/frameCount;
        this.height = bitmap.getHeight()/7;

        mFrameIndex = 0;

        mLastTime = System.currentTimeMillis();
    }



    private long mLastTime;
    @Override
    public boolean nextFrame() {
        long nowTime = System.currentTimeMillis();
        if (nowTime - mLastTime < 100) {
            return false;
        }
        mLastTime = nowTime;

        mFrameIndex++;
        if (mFrameIndex > mFrameCount) {
            mFrameIndex = 0;
            return true;
        }

        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        float x = getPt().x - width/2;
        float y = getPt().y - height/2;

        ///画攻击效果
        if(mDirection == GameConstants.DIRECT_LEFT) {
            ///画技能
            canvas.drawBitmap(mBitmap,
                    new Rect(   ///src rect
                            width * mFrameIndex,
                            mRowIndex * height,
                            width * mFrameIndex + width,
                            mRowIndex * height + height),
                    new RectF(x,
                            y,
                            x + width,
                            y + height), null);
        }else if(mDirection == GameConstants.DIRECT_RIGHT) {
            Matrix matrix = new Matrix();
            matrix.postScale(-1, 1); //镜像垂直翻转


            Bitmap bmpTmp2 = Bitmap.createBitmap(mBitmap,
                    width * Math.min(mFrameIndex,mFrameCount-1),
                    mRowIndex * height,
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
                            x + width,
                            y + height), null);

            bmpTmp2.recycle();
            bmpTmp2 = null;
        }
    }
}
