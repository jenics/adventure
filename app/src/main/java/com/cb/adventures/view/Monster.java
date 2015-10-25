package com.cb.adventures.view;

import android.graphics.Bitmap;

import com.cb.adventures.data.Propetry;

/**
 * 怪物类
 * Created by jenics on 2015/10/24.
 */
public class Monster extends Sprite {

    /**
     * 属性
     */
    private Propetry mPropetry;

    public Propetry getPropetry() {
        return mPropetry;
    }

    public void setPropetry(Propetry mPropetry) {
        this.mPropetry = mPropetry;
    }

    public Monster(Bitmap bitmap, int leftRowIndex, int rightRowIndex, int frameCount, int rowCount, int moveStep, int frameInterval) {
        super(bitmap, leftRowIndex, rightRowIndex, frameCount, rowCount, moveStep, frameInterval);
        mPropetry = new Propetry();
    }

    public Monster(Bitmap bitmap, int leftRowIndex, int rightRowIndex, int frameCount, int rowCount, int moveStep) {
        super(bitmap, leftRowIndex, rightRowIndex, frameCount, rowCount, moveStep);
        mPropetry = new Propetry();
    }



    /**
     * 受到伤害
     * @param injured 伤害值
     * @param skillId 技能唯一ID
     */
    public void beInjured(int injured,int skillId) {

    }

    public void attack() {

    }
}
