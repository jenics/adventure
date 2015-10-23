package com.cb.adventures.state.playerstate;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.view.Player;

/**
 * Created by jenics on 2015/10/12.
 */
public class AttackState extends PlayerBaseState {
    int frameIndex;
    Bitmap bitmap;
    int frameCount;
    int rowIndex;
    int width;
    int height;

    public AttackState(int id,Player player,int frameCount,int rowIndex , Bitmap bitmap,int width,int height) {
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
            frameIndex = 0;
            player.changeState(GameConstants.STATE_STOP,true);
        }

        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        //super.draw(canvas);
        float x = player.getPt().x - width/2;
        float y = player.getPt().y - height/2;

        ///画攻击效果
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

        ///画技能
        canvas.drawBitmap(bitmap,
                new Rect(   ///src rect
                        width * frameIndex,
                        (rowIndex+1) * height,
                        width * frameIndex + width,
                        (rowIndex+1) * height + height),
                new RectF(x-50,
                        y,
                        x-50+width,
                        y + height), null);
    }

    @Override
    public void entry() {
        frameIndex = 0;
        super.entry();
    }

    @Override
    public void leave() {
        super.leave();
        player.setIsAttacking(false);
    }
}
