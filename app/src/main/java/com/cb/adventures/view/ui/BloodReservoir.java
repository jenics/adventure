package com.cb.adventures.view.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.data.Propetry;
import com.cb.adventures.utils.ImageLoader;
import com.cb.adventures.view.BaseView;

/**
 * 血蓝槽
 * Created by jenics on 2015/10/24.
 */
public class BloodReservoir extends BaseView {
    private Propetry propetry;
    private Bitmap bitmap;
    private Bitmap background;

    private int backgroundHeight;

    private int topRed;
    private int topBlue;


    public BloodReservoir() {
    }

    public void init() {
        bitmap = ImageLoader.getInstance().loadBitmap(GameConstants.RED_BLUE_NAME);
        background = ImageLoader.getInstance().loadBitmap(GameConstants.RED_BLUE_BOTTOM);

        ///宽度是屏幕宽度的0.2倍
        width = (int) (GameConstants.sGameWidth*0.2);
        height = (int) (GameConstants.sGameHeight*0.07);

        backgroundHeight = (int)(height*0.24);
        ///
        pt.x = width/2 + 15;
        pt.y = (float) (height/2 + GameConstants.sGameHeight*0.05);

        topRed = (int) (GameConstants.sGameHeight*0.05 + height*0.121);
        topBlue = (int) (GameConstants.sGameHeight*0.05 + height*0.62);
    }

    public void setPropetry(Propetry propetry) {
        this.propetry = propetry;
    }

    @Override
    public void draw(Canvas canvas) {
        float x = getPt().x - width/2;
        float y = getPt().y - height/2;

        ///画血槽
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
        //if(propetry.getBloodVolume() > 0) {
            ///画血槽背景(红条）
            canvas.drawBitmap(background,
                    new Rect(   ///src rect
                            0,
                            0,
                            background.getWidth(),
                            background.getHeight()),
                    new RectF(pt.x + width/2 - width*(1.0f-propetry.getBloodRatio()),
                            topRed,
                            pt.x + width/2,
                            topRed + backgroundHeight), null);
        //}

        //if(propetry.getMagicVolume() > 0) {
            ///画血槽背景(蓝条）
            canvas.drawBitmap(background,
                    new Rect(   ///src rect
                            0,
                            0,
                            background.getWidth(),
                            background.getHeight()),
                    new RectF(pt.x + width/2 - width*(1.0f-propetry.getMagicRatio()),
                            topBlue,
                            pt.x + width/2,
                            topBlue + backgroundHeight), null);
        //}
    }
}
