package com.cb.adventures.animation;

import android.graphics.PointF;

import com.cb.adventures.view.BaseView;
import com.cb.adventures.view.Map;

/**
 * 捡起物品动画
 * Created by chengbo01 on 2015/12/29.
 * email : jenics@live.com
 */
public class PickUpPropAnimation extends ViewAnimation{
    /**
     * 跟随坐标,是一个引用
     */
    private PointF ptFollow;
    /**
     * 整个过程希望是800ms内完成
     */
    private long timeDuration = 400;
    /**
     * 当前时间
     */
    private long timeNow;
    public PickUpPropAnimation(BaseView view,PointF pt) {
        super(view);
        ptFollow = pt;
    }
    @Override
    public boolean animate() {
        timeNow = System.currentTimeMillis();
        long timePassed = timeNow-mStartTime;
        float timeRatio = timePassed*1.0f/800.0f;
        ///向着ptFollow的位置移动x坐标以及y坐标

        if (timePassed >= timeDuration) {
            return true;
        }

        mView.setPt(mView.getPt().x + timeRatio*(ptFollow.x-mView.getPt().x),
                mView.getPt().y + timeRatio*(ptFollow.y-mView.getPt().y));

        return false;
    }

}
