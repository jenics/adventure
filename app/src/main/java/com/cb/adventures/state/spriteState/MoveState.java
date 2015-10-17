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
public class MoveState extends SpriteBaseState {
    int direction;
    int frameIndex = 0;
    int frameCount = 0;
    int rowIndex = 0;
    int width;
    int height;
    private Bitmap bitmap;
    public MoveState(int id,Sprite sprite,int direction,int frameCount,int rowIndex , Bitmap bitmap,int width,int height)
    {
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
            frameIndex = 1;
        }

        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        //super.draw();
        ///假设人物在中心点，按60*70截取
        float x = sprite.getPt().x - 60/2;
        float y = sprite.getPt().y - 70/2;
        canvas.drawBitmap(bitmap,
                new Rect(   ///src rect
                        width * frameIndex + ((width - 60)/2),
                        rowIndex * height + (height-70),
                        width * frameIndex + ((width - 60)/2) + 60,
                        rowIndex * height + (height-70) + 70),
                new RectF(x,
                        y,
                        x + 100,
                        y + 112), null);
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
