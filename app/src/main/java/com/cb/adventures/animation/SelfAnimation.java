package com.cb.adventures.animation;

import android.graphics.Canvas;

import com.cb.adventures.view.BaseView;
import com.cb.adventures.view.FrameView;

import java.util.LinkedList;

/**
 * 自己控制自己的动画，自成view
 * Created by jenics on 2015/12/27.
 */
public class SelfAnimation extends FrameView implements IAnimation {
    protected long mBeginTime;  ///开始时间

    @Override
    public boolean isStop() {
        return isStop;
    }

    protected boolean isStop = false;

    @Override
    public void draw(Canvas canvas) {

    }

    public SelfAnimation(){
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
        mBeginTime = System.currentTimeMillis();
    }

    @Override
    public void stopAnimation() {
        isStop = true;
    }

    @Override
    public boolean animate() {
        return nextFrame();
    }

    /**
     * 动画停止
     * @param isForce   是否外界强制停止
     */
    @Override
    public synchronized void notifyAnimationEnd(boolean isForce){
        if(mAnimationListeners != null && mAnimationListeners.size() > 0){
            for(OnAniamtionListener listener : mAnimationListeners)
                listener.onAnimationEnd(this,isForce);
        }
    }

    public synchronized void setOnAnimationListener(OnAniamtionListener listener){
        if(listener != null)
            mAnimationListeners.add(listener);
    }
}
