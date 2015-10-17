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
        float x = sprite.getPt().x - 60/2;
        float y = sprite.getPt().y - 70/2;

        canvas.drawBitmap(bitmap,
                new Rect(   ///src rect
                        width * frameIndex + ((width - 60)/2),
                        rowIndex * height + ((height-70)/2),
                        width * frameIndex + ((width - 60)/2) + 60,
                        rowIndex * height + ((height-70)/2) + 70),
                new RectF(x,
                        y,
                        x + 100,
                        y + 112), null);

        canvas.drawBitmap(bitmap,
                new Rect(   ///src rect
                        width * frameIndex + ((width - 80)/2),
                        (rowIndex+1) * height + ((height-70)/2),
                        width * frameIndex + ((width - 80)/2) + 80,
                        (rowIndex+1) * height + ((height-70)/2) + 70),
                new RectF(x-100,
                        y,
                        x,
                        y + 112), null);
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
