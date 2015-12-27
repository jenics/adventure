package com.cb.adventures.animation;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.utils.FontFace;
import com.cb.adventures.view.BaseView;

/**
 * Created by jenics on 2015/12/26.
 * 受伤数值显示view
 */
public class InjuredValueAnimation extends SelfAnimation{
    private Paint.FontMetricsInt mFontMetricsInt;
    protected BaseView mAttachView;     ///挂靠的目标
    private String mStrTitle;

    /**
     * 初始y坐标
     */
    private float mSrcY;

    /**
     * 将向上移动的距离
     */
    private float mYMove;

    /**
     * 持续时间
     */
    private long mTimeDuration = 600;

    /**
     * 爆击
     */
    private boolean mIsCriticalStrike;

    /**
     * 字体大小
     */
    private int mTextSize;


    public InjuredValueAnimation(BaseView view, long injured, boolean isCritical) {
        setAttachView(view);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (isCritical) {
            mTextSize = 70;
            mPaint.setColor(Color.RED);
        } else {
            mTextSize = 50;
            mPaint.setColor(Color.YELLOW);
        }
        mPaint.setStrokeWidth(5);
        mPaint.setTextSize(mTextSize);

        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTypeface(FontFace.getInstance().getFontFace(FontFace.E_Font_Face.COMIXHEAVY));
        mFontMetricsInt = mPaint.getFontMetricsInt();

        mStrTitle = Long.toString(injured);
        setWidth((int) mPaint.measureText(mStrTitle));
        setHeight(mFontMetricsInt.bottom - mFontMetricsInt.top);
        setPt(mAttachView.getPt().x, mAttachView.getPt().y - GameConstants.sGameHeight * 0.05f);
        mSrcY = getPt().y;
        mIsCriticalStrike = isCritical;

        mYMove = GameConstants.sGameHeight * 0.05f;

    }

    public void setAttachView(BaseView mAttachView) {
        this.mAttachView = mAttachView;
    }

    @Override
    public boolean nextFrame() {
        long nowTime = System.currentTimeMillis();
        long timePassed = nowTime-mBeginTime;
        if (timePassed >= mTimeDuration) {
            return true;
        } else {
            float ratio = (timePassed*1.0f)/mTimeDuration;
            float move = ratio*mYMove;
            pt.y = mSrcY-move;
            mPaint.setTextSize(mTextSize-(ratio*mTextSize*0.2f));
        }
        return false;
    }


    @Override
    public void draw(Canvas canvas) {
        RectF targetRect = new RectF(
                mAttachView.getPt().x - getWidth() / 2,
                getPt().y-getHeight()/2,
                mAttachView.getPt().x + getWidth() / 2,
                getPt().y + getHeight()/2);
        int baseline = (int) ((targetRect.bottom + targetRect.top - mFontMetricsInt.bottom - mFontMetricsInt.top) / 2);
        canvas.drawText(mStrTitle, targetRect.centerX(), baseline, mPaint);
    }
}
