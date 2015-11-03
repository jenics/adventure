package com.cb.adventures.animation;

import com.cb.adventures.view.BaseView;

import java.util.LinkedList;

/**
 * Created by jenics on 2015/9/17.
 */
public class Animation implements IAnimation {
    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }

    protected boolean isStop = false;
    protected BaseView mView;
    public interface OnAniamtionListener{
        /**
         * @param view
         * @param isForce 是否外界强制停止
         */
        void onAnimationEnd(BaseView view,boolean isForce);
        void onAnimationBegin();
    }

    public BaseView getView() {
        return mView;
    }

    public Animation(BaseView view){
        mView = view;
        mAnimationListeners = new LinkedList<>();
    }

    protected LinkedList<OnAniamtionListener> mAnimationListeners;

    @Override
    public void startAnimation() {
        isStop = false;
        if(mAnimationListeners != null && mAnimationListeners.size() > 0){
            for(OnAniamtionListener listener : mAnimationListeners)
                listener.onAnimationBegin();
        }
        AnimationControl.getInstance().addAnimation(this);
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
    protected void notifyAnimationEnd(boolean isForce){
        if(mAnimationListeners != null && mAnimationListeners.size() > 0){
            for(OnAniamtionListener listener : mAnimationListeners)
                listener.onAnimationEnd(mView,isForce);
        }
    }

    public void setOnAnimationListener(OnAniamtionListener listener){
        if(listener != null)
            mAnimationListeners.add(listener);
    }
}
