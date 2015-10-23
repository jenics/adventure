package com.cb.adventures.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.utils.ImageLoader;

/**
 * Created by jenics on 2015/10/23.
 */
public class AttackController extends BaseView {
    private Bitmap bitmap;
    public AttackController() {
    }

    public void init() {
        bitmap = ImageLoader.getmInstance().loadBitmap(GameConstants.DIRECTION_ATTACK_NAME);
        ///宽度是屏幕宽度的0.15倍
        width = height = (int) (GameConstants.sGameWidth*0.15);
        ///
        pt.x = GameConstants.sGameWidth - width/2 - 15;
        pt.y = (float) (GameConstants.sGameHeight - height/2 - GameConstants.sGameHeight*0.1);
    }

    public void reset() {
        pt.x = GameConstants.sGameWidth*0.125f;
        pt.y = GameConstants.sGameHeight-pt.x;
    }

    @Override
    public void draw(Canvas canvas) {
        float x = getPt().x - width/2;
        float y = getPt().y - height/2;

        ///画控制器框框
        canvas.drawBitmap(bitmap,
                new Rect(   ///src rect
                        0,
                        0,
                        bitmap.getWidth(),
                        bitmap.getHeight()),
                new RectF(x,
                        y,
                        x + width,
                        y + height), null);

    }
}