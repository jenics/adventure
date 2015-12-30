package com.cb.adventures.animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.data.AnimationPropetry;
import com.cb.adventures.utils.ImageLoader;

/**
 * 卷轴效果动画，用来做新地图名字显示，技能名字显示，有点像跑马灯那样
 * Created by jenics on 2015/12/13.
 */
public class ScrollAnimation extends SelfAnimation {
    private RectF rt1;
    private RectF rt2;

    protected long mLastTime;

    private int cursor;         ///游标

    private Paint.FontMetricsInt mFontMetricsInt;

    private long timeDuration;

    private AnimationPropetry mAnimationPropetry;

    private String mStrTitle;

    public void setmStrTitle(String mStrTitle) {
        this.mStrTitle = mStrTitle;
    }

    public ScrollAnimation() {
        mAnimationPropetry = new AnimationPropetry();
        rt1 = new RectF();
        rt2 = new RectF();
    }

    public AnimationPropetry getAnimationPropetry() {
        return mAnimationPropetry;
    }

    /**
     * @param timeDuration 持续时间，单位s
     */
    public void setTimeDuration(long timeDuration) {
        this.timeDuration = timeDuration;
    }

    public void setAnimationPropetry(AnimationPropetry animationPropetry) {
        this.mAnimationPropetry = animationPropetry;
        timeDuration = animationPropetry.getTimeDuration();
        if (mBitmap == null) {
            mBitmap = ImageLoader.getInstance().loadBitmap(mAnimationPropetry.getSrcInfo().getSrcName());
            setWidth(mBitmap.getWidth());
            setHeight(mBitmap.getHeight());
            cursor = 0;
            rt1.top = getPt().y-getHeight()/2;
            rt1.bottom = getPt().y + getHeight()/2;
            rt2.top = getPt().y-getHeight()/2;
            rt2.bottom = getPt().y + getHeight()/2;

            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStrokeWidth(5);
            mPaint.setTextSize(50);
            mPaint.setColor(Color.BLUE);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mFontMetricsInt = mPaint.getFontMetricsInt();
        }
    }

    @Override
    public boolean nextFrame() {
        long nowTime = System.currentTimeMillis();

        /**
         * 时间限制动画
         */
             if (timeDuration > 0) {
            if ((nowTime - mBeginTime) > timeDuration) {
                return true;
            }
        }

        if (nowTime - mLastTime < 100) {
            return false;
        }

        cursor -= 10;

        if (cursor < 0) {
            cursor = mBitmap.getWidth();
        }

        rt1.left = getPt().x - getWidth() / 2;
        rt1.right = rt1.left + cursor;

        rt2.left = rt1.right;
        rt2.right = rt2.left + mBitmap.getWidth() - cursor;

        mLastTime = nowTime;

        return false;
    }


    @Override
    public void draw(Canvas canvas) {
        if (cursor == 0 || cursor == mBitmap.getWidth()) {
            canvas.drawBitmap(mBitmap,
                    new Rect(   ///src rect
                            0,
                            0,
                            mBitmap.getWidth(),
                            mBitmap.getHeight()),
                    rt1.width() == 0 ? rt2 : rt1, null);

            RectF targetRect = new RectF(getPt().x - getWidth() / 2, getPt().y-getHeight()/2, getPt().x + getWidth() / 2, getPt().y + getHeight()/2);
            int baseline = (int) ((targetRect.bottom + targetRect.top - mFontMetricsInt.bottom - mFontMetricsInt.top) / 2);
            canvas.drawText(mStrTitle, targetRect.centerX(), baseline, mPaint);

            return;
        }
        Bitmap bmpTmp = Bitmap.createBitmap(mBitmap,
                mBitmap.getWidth() - cursor,
                0,
                cursor,
                mBitmap.getHeight(),
                null,
                true);

        canvas.drawBitmap(bmpTmp,
                new Rect(   ///src rect
                        0,
                        0,
                        bmpTmp.getWidth(),
                        bmpTmp.getHeight()),
                rt1, null);

        bmpTmp.recycle();

        bmpTmp = Bitmap.createBitmap(mBitmap,
                0,
                0,
                mBitmap.getWidth() - cursor,
                mBitmap.getHeight(),
                null,
                true);
        canvas.drawBitmap(bmpTmp,
                new Rect(   ///src rect
                        0,
                        0,
                        bmpTmp.getWidth(),
                        bmpTmp.getHeight()),
                rt2, null);
        bmpTmp.recycle();

        RectF targetRect = new RectF(getPt().x - getWidth() / 2, getPt().y-getHeight()/2, getPt().x + getWidth() / 2, getPt().y + getHeight()/2);
        int baseline = (int) ((targetRect.bottom + targetRect.top - mFontMetricsInt.bottom - mFontMetricsInt.top) / 2);
        canvas.drawText(mStrTitle, targetRect.centerX(), baseline, mPaint);
    }

}
