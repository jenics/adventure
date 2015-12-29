package com.cb.adventures.animation;

import android.graphics.PointF;

import com.cb.adventures.view.BaseView;

/**
 * 捡起物品动画
 * Created by chengbo01 on 2015/12/29.
 * email : jenics@live.com
 */
public class PickUpPropAnimation extends ViewAnimation{
    /**
     * 跟随坐标
     */
    private PointF ptFollow;
    public PickUpPropAnimation(BaseView view) {
        super(view);
    }
    @Override
    public boolean animate() {

        return false;
    }
}
