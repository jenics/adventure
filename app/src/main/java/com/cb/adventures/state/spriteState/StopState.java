package com.cb.adventures.state.spriteState;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.view.Sprite;

/**
 * Created by jenics on 2015/10/12.
 */
public class StopState extends SpriteBaseState {
    private int leftRowIndex ;
    private int rightRowIndex ;
    private Bitmap bitmap;
    private int width;
    private int height;

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
        float x = sprite.getPt().x - width/2;
        float y = sprite.getPt().y - height/2;

        if (sprite.getDirection() == GameConstants.DIRECTION_LEFT) {
            canvas.drawBitmap(bitmap,
                    new Rect(   ///src rect
                            0,
                            leftRowIndex * height,
                            width,
                            leftRowIndex * height + height),
                    new RectF(x,
                            y,
                            x + width,
                            y + width), null);
        } else {
            canvas.drawBitmap(bitmap,
                    new Rect(   ///src rect
                            0,
                            rightRowIndex * height,
                            width,
                            rightRowIndex * height + height),
                    new RectF(x,
                            y,
                            x + width,
                            y + height), null);
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
