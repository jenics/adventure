package com.cb.adventures.animation;

import android.graphics.Canvas;

import com.cb.adventures.view.BaseView;

import java.util.LinkedList;

/**
 * 直接控制baseview的动画
 * Created by jenics on 2015/9/17.
 */
public class ViewAnimation implements IAnimation {
    @Override
    public boolean isStop() {
        return isStop;
    }

    protected long mStartTime;

    protected boolean isStop = false;
    protected BaseView mView;


    public BaseView getView() {
        return mView;
    }

    public ViewAnimation(BaseView view){
        mView = view;
        mAnimationListeners = new LinkedList<>();
    }

    protected LinkedList<OnAniamtionListener> mAnimationListeners;


    @Override
    public synchronized void startAnimation() {
        isStop = false;
        if(mAnimationListeners != null && mAnimationListeners.size() > 0){
            for(OnAniamtionListener listener : mAnimationListeners)
                listener.onAnimationBegin();
        }
        AnimationControl.getInstance().addAnimation(this);
        mStartTime = System.currentTimeMillis();
    }

    @Override
    public void stopAnimation() {
        isStop = true;
    }

    @Override
    public boolean animate() {
        return isStop();
    }

    /**
     * 动画停止
     * @param isForce   是否外界强制停止
     */
    @Override
    public synchronized void notifyAnimationEnd(boolean isForce){
        if(mAnimationListeners != null && mAnimationListeners.size() > 0){
            for(OnAniamtionListener listener : mAnimationListeners)
                listener.onAnimationEnd(mView,isForce);
        }
    }

    public synchronized void setOnAnimationListener(OnAniamtionListener listener){
        if(listener != null)
            mAnimationListeners.add(listener);
    }
}
