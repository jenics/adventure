package com.cb.adventures.view.ui;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.utils.ImageLoader;
import com.cb.adventures.view.BaseView;

/**
 * Created by jenics on 2015/10/22.
 */
public class DirectionController extends BaseView {
    public DirectionController() {
    }

    public void init() {
        mBitmap = ImageLoader.getInstance().loadBitmap(GameConstants.DIRECTION_CONTROLLER_NAME);

        ///与下面有个屏幕左下有宽度*0.125的距离
        pt.x = GameConstants.sGameWidth*0.125f;
        pt.y = GameConstants.sGameHeight-pt.x;

        ///宽度是屏幕宽度的0.18倍
        width = height = (int) (GameConstants.sGameWidth*0.18);

        mPaint.setAlpha(170);

    }

    @Override
    public void draw(Canvas canvas) {
        float x = getPt().x - width/2;
        float y = getPt().y - height/2;

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

    }
}
