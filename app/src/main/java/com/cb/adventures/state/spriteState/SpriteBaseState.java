package com.cb.adventures.state.spriteState;

import android.graphics.Canvas;

import com.cb.adventures.state.BaseState;
import com.cb.adventures.view.IView;
import com.cb.adventures.view.Sprite;

/**
 * Created by jenics on 2015/10/12.
 */
public class SpriteBaseState extends BaseState{
    protected Sprite sprite;
    public SpriteBaseState(int id,Sprite sprite) {
        super(id);
        this.sprite = sprite;
    }

    public boolean nextFrame(){
        return true;
    }

    public void draw(Canvas canvas){

    }
}
