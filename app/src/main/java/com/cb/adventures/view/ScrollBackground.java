package com.cb.adventures.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by jenics on 2015/10/7.
 */
public class ScrollBackground extends BaseView{
    private Bitmap bmp1;
    private Bitmap bmp2;

    private RectF rt1;
    private RectF rt2;

    private int screemWidth;
    private int screemHeight;


    public ScrollBackground(){
        isClickable = false;
        rt1 = new RectF();
        rt2 = new RectF();
    }

    public void init(Bitmap bmp1,Bitmap bmp2,int screemWidth,int screemHeight){
        this.bmp1 = bmp1;
        this.bmp2 = bmp2;
        this.screemWidth = screemWidth;
        this.screemHeight = screemHeight;
        rt1.left = 0.0f;
        rt1.top = 0.0f;
        rt1.right = screemWidth;
        rt1.bottom = screemHeight;
        rt2.left = screemWidth;
        rt2.top = 0.0f;
        rt2.right = screemWidth+screemWidth;
        rt2.bottom = screemHeight;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        scroll();
        canvas.drawBitmap(bmp1,
                new Rect(   ///src rect
                        0,
                        0,
                        bmp1.getWidth(),
                        bmp1.getHeight()),
                rt1, null);

        canvas.drawBitmap(bmp2,
                new Rect(   ///src rect
                        0,
                        0,
                        bmp2.getWidth(),
                        bmp2.getHeight()),
                rt2, null);

    }

    private void scroll(){
        rt1.left -= 10;
        rt1.right -= 10;
        rt2.left -= 10;
        rt2.right -= 10;

        if(rt1.left < -screemWidth){
            rt1.left = screemWidth;
            rt1.right = screemWidth + screemWidth;
            rt2.left = 0;
            rt2.right = screemWidth;
        }else if(rt2.left < -screemWidth){
            rt1.left = 0;
            rt1.right = screemWidth;
            rt2.left = screemWidth;
            rt2.right = screemWidth + screemWidth;
        }
    }
}
