package com.cb.adventures.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by jenics on 2015/10/7.
 */
public class BaseView implements IView  {
    /**
     * view的中心点
     */
    protected PointF pt;
    /**
     * Z轴深度
     */
    protected int mZorder;
    /**
     * 宽度
     */
    protected int width;
    /**
     * 高度
     */
    protected int height;
    /**
     * 是否可点击
     */
    protected boolean isClickable;
    /**
     * 是否可见
     */
    protected boolean isVisiable;
    /**
     * 画刷
     */
    protected Paint mPaint;
    /**
     * 位图
     */
    protected Bitmap mBitmap;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BaseView() {
        isClickable = true;
        isVisiable = true;
        pt = new PointF();
        mPaint = new Paint();
    }

    public PointF getPt() {
        return pt;
    }

    public void setPt(PointF pt) {
        this.pt.x = pt.x;
        this.pt.y = pt.y;
    }
    public void setPt(int x,int y) {
        this.pt.x = x;
        this.pt.y = y;
    }

    public void setPt(float x,float y) {
        this.pt.x = x;
        this.pt.y = y;
    }

    public void setIsClickable(boolean isClickable) {
        this.isClickable = isClickable;
    }

    public void setIsVisiable(boolean isVisiable) {
        this.isVisiable = isVisiable;
    }

    @Override
    public boolean isClickable() {
        return isClickable;
    }

    @Override
    public boolean isVisiable() {
        return isVisiable;
    }

    public void onClick(){

    }
    public void draw(Canvas canvas) {
        if (!isVisiable || mBitmap == null) {
            return;
        }
        float x = pt.x-mBitmap.getWidth()/2;
        float y = pt.y-mBitmap.getHeight()/2;
        canvas.drawBitmap(mBitmap,
                new Rect(0,
                        0,
                        mBitmap.getWidth(),
                        mBitmap.getHeight()),
                new RectF(x,
                        y,
                        x + getWidth(),
                        y + getHeight()),
                mPaint);
    }

    public void calcSize() {
        if (mBitmap != null) {
            width = mBitmap.getWidth();
            height = mBitmap.getHeight();
        }
    }

    public Paint getmPaint() {
        return mPaint;
    }

    public void setmPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }

    @Override
    public int getZOrder() {
        return mZorder;
    }
}
