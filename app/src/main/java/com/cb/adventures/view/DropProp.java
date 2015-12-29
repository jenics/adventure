package com.cb.adventures.view;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.cb.adventures.animation.DropPropAnimation;
import com.cb.adventures.animation.IAnimation;
import com.cb.adventures.data.PropPropetry;
import com.cb.adventures.prop.IProp;
import com.cb.adventures.utils.ImageLoader;

/**
 * Created by chengbo01 on 2015/12/29.
 * 掉落的物品
 * email : jenics@live.com
 */
public class DropProp extends BaseView implements IAnimation.OnAniamtionListener{
    @Override
    public void onAnimationEnd(BaseView view, boolean isForce) {
        if (isPicking && null != mPickUpPropListener) {
            mPickUpPropListener.onPickUpOver(prop);
        }
    }

    @Override
    public void onAnimationBegin() {

    }

    public interface PickUpPropListener {
        /**
         * 开始捡起
         * @param prop 道具接口
         */
        void onPickUpBegin(PropPropetry prop);
        /**
         * 捡起完成
         * @param prop 道具接口
         */
        void onPickUpOver(PropPropetry prop);
    }
    private PickUpPropListener mPickUpPropListener;
    private PropPropetry prop;
    /**
     * 如果开始动画了就是开始捡了
     */
    private boolean isPicking;
    public DropProp(PropPropetry prop) {
        isPicking = false;
        this.prop = prop;
        mBitmap = ImageLoader.getmInstance().loadBitmap(prop.getIcon());
        calcSize();
    }

    public PropPropetry getProp() {
        return prop;
    }



    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    /**
     * 掉落
     * @param pt 掉落坐标原点
     */
    public void drop(PointF pt) {
        setPt(pt);
        DropPropAnimation dropPropAnimation = new DropPropAnimation(this);
        dropPropAnimation.startAnimation();
    }

    /**
     * 外面在发起这个捡起动作的时候，会做一系列检测，比如背包还有没有空位，能否叠加
     * @param pt 跟随这个坐标。
     */
    public void pickUp(PointF pt) {

    }

    public boolean canPickedUp() {
        return !isPicking;
    }

    public void setPickUpPropListener(PickUpPropListener listener) {
        mPickUpPropListener = listener;
    }
}
