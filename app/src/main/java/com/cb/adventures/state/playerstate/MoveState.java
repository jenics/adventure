package com.cb.adventures.state.playerstate;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.utils.CLog;
import com.cb.adventures.view.Player;
import com.cb.adventures.view.Sprite;

/**
 * Created by jenics on 2015/10/12.
 */
public class MoveState extends PlayerBaseState {
    int frameIndex = 0;
    int frameCount = 0;
    int rowIndex = 0;
    int width;
    int height;
    private Bitmap bitmap;
    public MoveState(int id,Player player,int frameCount,int rowIndex , Bitmap bitmap,int width,int height)
    {
        super(id,player);
        frameIndex = 0;
        this.rowIndex = rowIndex;
        this.frameCount = frameCount;
        this.bitmap = bitmap;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean nextFrame() {
        frameIndex++;
        if (frameIndex >= frameCount) {
            frameIndex = 1;
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        int disWidth = (int) (width* GameConstants.zoomRatio);
        int disHeight = (int) (height*GameConstants.zoomRatio);
        float x = player.getPt().x - disWidth/2;
        float y = player.getPt().y - disHeight/2;

        canvas.drawBitmap(bitmap,
                new Rect(   ///src rect
                        width * frameIndex,
                        rowIndex * height,
                        width * frameIndex + width,
                        rowIndex * height + height),
                new RectF(x,
                        y,
                        x + disWidth,
                        y + disHeight), player.getmPaint());
    }

    @Override
    public void entry() {
        super.entry();

        frameIndex = 0;
    }

    @Override
    public void leave() {
        super.leave();
    }
}
