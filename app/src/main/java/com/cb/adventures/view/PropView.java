package com.cb.adventures.view;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.cb.adventures.animation.DropPropAnimation;
import com.cb.adventures.animation.IAnimation;
import com.cb.adventures.animation.PickUpPropAnimation;
import com.cb.adventures.data.PropPropetry;
import com.cb.adventures.utils.ImageLoader;

import java.lang.ref.WeakReference;

/**
 * Created by chengbo01 on 2015/12/29.
 * 掉落的物品
 * email : jenics@live.com
 */
public class PropView extends BaseView implements IAnimation.OnAniamtionListener{
    private WeakReference<DropPropAnimation> mDropPropAnimation;
    private WeakReference<PickUpPropAnimation> mPickUpPropAnimation;
    @Override
    public void onAnimationEnd(BaseView view, boolean isForce) {
        if (isPicking && null != mPickUpPropListener) {
            mPickUpPropListener.onPickUpOver(this);
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
        void onPickUpBegin(PropView prop);
        /**
         * 捡起完成
         * @param prop 道具接口
         */
        void onPickUpOver(PropView prop);
    }
    private PickUpPropListener mPickUpPropListener;
    private PropPropetry prop;
    /**
     * 如果开始动画了就是开始捡了
     */
    private boolean isPicking;
    public PropView(PropPropetry prop) {
        isPicking = false;
        this.prop = prop;
        mBitmap = ImageLoader.getInstance().loadBitmap(prop.getIcon());
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
        mDropPropAnimation = new WeakReference<>(new DropPropAnimation(this));
        mDropPropAnimation.get().startAnimation();
    }

    /**
     * 外面在发起这个捡起动作的时候，会做一系列检测，比如背包还有没有空位，能否叠加
     * @param pt 跟随这个坐标。
     */
    public void pickUp(PointF pt) {
        if (!canPickedUp()) {
            return;
        }
        isPicking = true;
        final DropPropAnimation dropPropAnimation = mDropPropAnimation.get();
        if (dropPropAnimation != null) {
            dropPropAnimation.stopAnimation();
        }
        mPickUpPropAnimation = new WeakReference<>(new PickUpPropAnimation(this,pt));
        mPickUpPropAnimation.get().setOnAnimationListener(this);
        mPickUpPropAnimation.get().startAnimation();
    }

    public boolean canPickedUp() {
        return !isPicking;
    }

    public void setPickUpPropListener(PickUpPropListener listener) {
        mPickUpPropListener = listener;
    }
}
