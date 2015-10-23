package com.cb.adventures.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.constants.GameConstants;

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
    private long lastTime;
    private int mDirection;

    private static final int STEP_LENGTH = 5;   ///没帧，地图移动的步长


    public ScrollBackground(){
        isClickable = false;
        rt1 = new RectF();
        rt2 = new RectF();
        mDirection = GameConstants.STATE_NONE;
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

    public void scrollTo(int direction){
        mDirection = direction;
    }

    public void stopScroll() {
        mDirection = GameConstants.STATE_STOP;
    }

    private void scroll() {
        if(mDirection == GameConstants.STATE_MOVE_RIGHT) {
            rt1.left -= STEP_LENGTH;
            rt1.right -= STEP_LENGTH;
            rt2.left -= STEP_LENGTH;
            rt2.right -= STEP_LENGTH;

            if (rt1.left < -screemWidth) {
                rt1.left = screemWidth;
                rt1.right = screemWidth + screemWidth;
                rt2.left = 0;
                rt2.right = screemWidth;
            } else if (rt2.left < -screemWidth) {
                rt1.left = 0;
                rt1.right = screemWidth;
                rt2.left = screemWidth;
                rt2.right = screemWidth + screemWidth;
            }
        }else if(mDirection == GameConstants.STATE_MOVE_LEFT){
            rt1.left += STEP_LENGTH;
            rt1.right += STEP_LENGTH;
            rt2.left += STEP_LENGTH;
            rt2.right += STEP_LENGTH;

            if (rt1.left > screemWidth) {
                rt1.left = -screemWidth;
                rt1.right = 0;
                rt2.left = 0;
                rt2.right = screemWidth;
            } else if (rt2.left > screemWidth) {
                rt1.left = -screemWidth;
                rt1.right = 0;
                rt2.left = 0;
                rt2.right = screemWidth;
            }
        }
    }
}
