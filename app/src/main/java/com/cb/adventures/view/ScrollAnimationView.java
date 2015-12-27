package com.cb.adventures.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

import com.cb.adventures.animation.AnimationProxy;
import com.cb.adventures.application.MyApplication;
import com.cb.adventures.data.AnimationPropetry;
import com.cb.adventures.utils.ImageLoader;

import java.lang.reflect.Type;

/**
 * Created by jenics on 2015/12/13.
 */
public class ScrollAnimationView extends BaseView {
    protected Bitmap mBitmap;

    private RectF rt1;
    private RectF rt2;

    protected long mLastTime;
    protected long mBeginTime;  ///开始时间

    private int cursor;         ///游标

    private Paint mPaint;
    private Paint.FontMetricsInt mFontMetricsInt;

    private long timeDuration;

    protected BaseView mAttachView;     ///挂靠的目标

    private AnimationProxy proxy;

    private AnimationPropetry mAnimationPropetry;

    protected int mDirection;

    public int getDirection() {
        return mDirection;
    }

    public void setDirection(int mDirection) {
        this.mDirection = mDirection;
    }

    private String mStrTitle;

    public String getmStrTitle() {
        return mStrTitle;
    }

    public void setmStrTitle(String mStrTitle) {
        this.mStrTitle = mStrTitle;
    }

    public ScrollAnimationView() {
        mAnimationPropetry = new AnimationPropetry();
        rt1 = new RectF();
        rt2 = new RectF();
    }

    public BaseView getmAttachView() {
        return mAttachView;
    }

    public void setAttachView(BaseView mAttachView) {
        this.mAttachView = mAttachView;
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
            mBitmap = ImageLoader.getmInstance().loadBitmap(mAnimationPropetry.getSrcInfo().getSrcName());
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


    public void startAnimation() {
        proxy = new AnimationProxy(this);
        //proxy.setOnAnimationListener(this);
        proxy.startAnimation();
        mBeginTime = mLastTime = System.currentTimeMillis();
    }

    /**
     * 停止技能，将会从动画列表中移除
     */
    public void stopAnimation() {
        if (proxy != null) {
            proxy.stopAnimation();
            proxy = null;
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
