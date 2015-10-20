package com.cb.adventures.state.spriteState;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.state.BaseState;
import com.cb.adventures.view.Sprite;

/**
 * Created by jenics on 2015/10/12.
 */
public class AttackState extends SpriteBaseState {
    int direction;

    int frameIndex ;
    Bitmap bitmap;
    int frameCount;
    int rowIndex;
    int width;
    int height;

    public AttackState(int id,Sprite sprite,int direction,int frameCount,int rowIndex , Bitmap bitmap,int width,int height) {
        super(id,sprite);
        this.direction = direction;
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
            sprite.changeState(Sprite.STATE_STOP,true);
        }

        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        //super.draw(canvas);
        float x = sprite.getPt().x - width/2;
        float y = sprite.getPt().y - height/2;

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
    }
}
