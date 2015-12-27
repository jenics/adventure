package com.cb.adventures.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by jenics on 2015/10/7.
 */
public class BaseView implements IView  {
    protected PointF pt;    ///中心点

    /**
     * Z轴深度
     */
    protected int mZorder;

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

    protected int width;    ///宽度
    protected int height;   ///高度
    protected boolean isClickable;
    protected boolean isVisiable;

    protected Paint mPaint;
    protected Bitmap mBitmap;

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
