package com.cb.adventures.state.spriteState;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.view.Sprite;

/**
 * Created by jenics on 2015/10/12.
 */
public class StopState extends SpriteBaseState {
    int leftRowIndex ;
    int rightRowIndex ;
    Bitmap bitmap;
    int width;
    int height;

    public StopState(int id,Sprite sprite,int leftRowIndex , int rightRowIndex ,Bitmap bitmap,int width,int height) {
        super(id,sprite);
        this.leftRowIndex = leftRowIndex;
        this.rightRowIndex = rightRowIndex;
        this.bitmap = bitmap;
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean nextFrame() {
        //return super.nextFrame();
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        //super.draw(canvas);
        float x = sprite.getPt().x - 60/2;
        float y = sprite.getPt().y - 70/2;

        if (sprite.getDirection() == Sprite.DIRECTION_LEFT) {
            canvas.drawBitmap(bitmap,
                    new Rect(   ///src rect
                            0 + ((width - 60)/2),
                            leftRowIndex * height + (height-70),
                            0 + ((width - 60)/2) + 60,
                            leftRowIndex * height + (height-70) + 70),
                    new RectF(x,
                            y,
                            x + 100,
                            y + 112), null);
        } else {
            canvas.drawBitmap(bitmap,
                    new Rect(   ///src rect
                            0 + ((width - 60)/2),
                            rightRowIndex * height + (height-70),
                            0 + ((width - 60)/2) + 60,
                            rightRowIndex * height + (height-70) + 70),
                    new RectF(x,
                            y,
                            x + 100,
                            y + 112), null);
        }
    }

    @Override
    public void entry() {
        super.entry();
    }

    @Override
    public void leave() {
        super.leave();
    }
}
