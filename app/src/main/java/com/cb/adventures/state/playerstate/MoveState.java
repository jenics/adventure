package com.cb.adventures.state.playerstate;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

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
        //return super.nextFrame();

        frameIndex++;
        if (frameIndex >= frameCount) {
            frameIndex = 1;
        }

        CLog.d("nextFrame", String.format("%d:", frameIndex));

        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        //super.draw();
        ///假设人物在中心点，按60*70截取
        float x = player.getPt().x - width/2;
        float y = player.getPt().y - height/2;
        canvas.drawBitmap(bitmap,
                new Rect(   ///src rect
                        width * frameIndex,
                        rowIndex * height,
                        width * frameIndex + width,
                        rowIndex * height + height),
                new RectF(x,
                        y,
                        x + width,
                        y + height), null);
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
