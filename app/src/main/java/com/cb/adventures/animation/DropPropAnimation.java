package com.cb.adventures.animation;

import android.graphics.PointF;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.view.BaseView;

/**
 * 掉落物品动画
 * Created by chengbo01 on 2015/12/29.
 * email : jenics@live.com
 */
public class DropPropAnimation extends ViewAnimation {
    /**
     * 掉落起始位置
     */
    private PointF ptSrc;
    /**
     * 掉落动画持续时间
     */
    private final int timeDuration = 600;
    private long secondStartTime;

    public static final int UP = 0;
    public static final int DOWN = 1;
    private int direction;
    private float distance;
    public DropPropAnimation(BaseView view) {
        super(view);
        ptSrc = new PointF();
        ptSrc.x = view.getPt().x;
        ptSrc.y = view.getPt().y;
        direction = UP;
        distance = mView.getHeight(); ///先向上跑一阵
    }
    @Override
    public boolean animate() {
        ///先向上走一段
        long timeNow = System.currentTimeMillis();

        if (direction == UP) {
            float timeRatio = (timeNow-mStartTime)/(timeDuration*0.2f);
            float y = ptSrc.y-distance*timeRatio;
            mView.setPt(ptSrc.x,y);
            if (ptSrc.y - mView.getPt().y >= distance ) {
                mView.setPt(ptSrc.x,ptSrc.y - distance);
                ///开始往下跑
                direction = DOWN;
                distance = GameConstants.sGameHeight*GameConstants.sBottomRatio - mView.getPt().y;
                secondStartTime = timeNow;
                ptSrc.x = mView.getPt().x;
                ptSrc.y = mView.getPt().y;
            }

        }else {
            float timeRatio = (timeNow-secondStartTime)/(timeDuration*0.8f);
            float y = ptSrc.y + distance*timeRatio;
            mView.setPt(ptSrc.x,y);
            if (mView.getPt().y - ptSrc.y >= distance ) {
                mView.setPt(ptSrc.x,ptSrc.y + distance);
                return true;
            }
        }
        return false;
    }
}
