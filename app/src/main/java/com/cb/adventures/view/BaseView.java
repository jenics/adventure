package com.cb.adventures.view;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by jenics on 2015/10/7.
 */
public class BaseView implements IView {
    protected PointF pt;
    protected boolean isClickable;
    protected boolean isVisiable;

    public BaseView() {
        isClickable = true;
        isVisiable = true;
        pt = new PointF();
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
}
