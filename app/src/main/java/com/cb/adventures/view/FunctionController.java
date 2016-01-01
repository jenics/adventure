package com.cb.adventures.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.data.ConsumePropetry;
import com.cb.adventures.data.SkillPropetry;
import com.cb.adventures.prop.Consume;
import com.cb.adventures.prop.IProp;
import com.cb.adventures.prop.IStackable;
import com.cb.adventures.prop.IUsable;
import com.cb.adventures.utils.FontFace;
import com.cb.adventures.utils.ImageLoader;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by jenics on 2015/11/1.
 */
public class FunctionController extends BaseView {
    public static float WIDTH_RATIO = 0.1f;
    private Bitmap icon;
    private IUsable mUseable;
    private Paint.FontMetricsInt mFontMetricsInt;
    private final ReentrantReadWriteLock mReentrantReadWriteLock = new ReentrantReadWriteLock();

    public FunctionController() {
    }

    public void setUseable(IUsable useable) {
        mReentrantReadWriteLock.writeLock().lock();
        mUseable = useable;
        icon = ImageLoader.getInstance().loadBitmap(useable.getIcon());
        mReentrantReadWriteLock.writeLock().unlock();
    }

    public IUsable getUseable() {
        return mUseable;
    }


    public void init() {
        mBitmap = ImageLoader.getInstance().loadBitmap(GameConstants.GAME_SKILL_NAME);

        ///宽度是屏幕宽度的0.1
        width = height = (int) (GameConstants.sGameWidth * WIDTH_RATIO);
        mPaint.setAlpha(170);

        if (mPaint == null)
            mPaint = new Paint();
        mPaint.setAntiAlias(true);  ///抗锯齿
        mPaint.setTypeface(FontFace.getInstance().getFontFace(FontFace.E_Font_Face.COMIXHEAVY));
        mPaint.setTextSize(30);
        mPaint.setColor(Color.YELLOW);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mFontMetricsInt = mPaint.getFontMetricsInt();
    }

    @Override
    public void draw(Canvas canvas) {
        float x = getPt().x - width / 2;
        float y = getPt().y - height / 2;

        mReentrantReadWriteLock.readLock().lock();

        ///画控制器框框
        canvas.drawBitmap(mBitmap,
                new Rect(   ///src rect
                        0,
                        0,
                        mBitmap.getWidth(),
                        mBitmap.getHeight()),
                new RectF(x,
                        y,
                        x + width,
                        y + height), mPaint);

        ///画icon
        float ratio;
        float disWidth;
        float disHeight;
        if (icon.getWidth() > icon.getHeight()) {
            ///宽
            ratio = icon.getHeight() * 1.0f / icon.getWidth() * 1.0f;
            disHeight = height * ratio;
            canvas.drawBitmap(icon,
                    new Rect(   ///src rect
                            0,
                            0,
                            icon.getWidth(),
                            icon.getHeight()),
                    new RectF(x,
                            getPt().y - disHeight / 2,
                            x + width,
                            getPt().y + disHeight / 2), null);
        } else {
            ///高
            ratio = icon.getWidth() * 1.0f / icon.getHeight() * 1.0f;
            disWidth = height * ratio;
            canvas.drawBitmap(icon,
                    new Rect(   ///src rect
                            0,
                            0,
                            icon.getWidth(),
                            icon.getHeight()),
                    new RectF(getPt().x - disWidth / 2,
                            y,
                            getPt().x + disWidth / 2,
                            y + height), null);
        }

        if (mUseable != null) {
            if (mUseable instanceof IStackable) {
                ///画叠加数量
                ratio = icon.getWidth() * 1.0f / icon.getHeight() * 1.0f;
                disWidth = height * ratio;
                String text = Integer.valueOf(((IStackable) mUseable).getCurrentStackSize()).toString();
                float textWidth = mPaint.measureText(text);
                canvas.drawText(text, getPt().x + disWidth / 2 - textWidth, y - mFontMetricsInt.descent, mPaint);
            }
        }
        mReentrantReadWriteLock.readLock().unlock();
    }
}
