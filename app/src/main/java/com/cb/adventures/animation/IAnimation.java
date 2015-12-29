package com.cb.adventures.animation;

import com.cb.adventures.view.BaseView;
import com.cb.adventures.view.IDrawable;

/**
 * Created by jenics on 2015/9/15.
 */
public interface IAnimation {

    public interface OnAniamtionListener{
        /**
         * @param view
         * @param isForce 是否外界强制停止
         */
        void onAnimationEnd(BaseView view,boolean isForce);
        void onAnimationBegin();
    }
    /**
     * 开始动画
     */
    void startAnimation();

    /**
     * 停止动画，强制停止
     */
    void stopAnimation();

    /**
     *
     * @return true if animation finish automatic
     */
    boolean animate();

    /**
     * @return true if animation is force stop
     */
    boolean isStop();

    void notifyAnimationEnd(boolean isForce);
}


