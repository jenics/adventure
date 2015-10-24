package com.cb.adventures.view;

import android.graphics.Bitmap;

import com.cb.adventures.data.Propetry;

/**
 * 怪物类
 * Created by jenics on 2015/10/24.
 */
public class Monster extends Sprite {
    public Propetry getmPropetry() {
        mPropetry = new Propetry();
        return mPropetry;
    }

    public void setmPropetry(Propetry mPropetry) {
        this.mPropetry = mPropetry;
    }

    /**
     * 属性
     */
    private Propetry mPropetry;

    public Monster(Bitmap bitmap, int leftRowIndex, int rightRowIndex, int frameCount, int rowCount, int moveStep, int frameInterval) {
        super(bitmap, leftRowIndex, rightRowIndex, frameCount, rowCount, moveStep, frameInterval);
    }

    public Monster(Bitmap bitmap, int leftRowIndex, int rightRowIndex, int frameCount, int rowCount, int moveStep) {
        super(bitmap, leftRowIndex, rightRowIndex, frameCount, rowCount, moveStep);
    }


}
