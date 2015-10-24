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
        void onAnimationEnd(BaseView view);
        void onAnimationBegin();
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

    protected void onAnimationEnd(){
        if(mAnimationListeners != null && mAnimationListeners.size() > 0){
            for(OnAniamtionListener listener : mAnimationListeners)
                listener.onAnimationEnd(mView);
        }
    }

    public void setOnAnimationListener(OnAniamtionListener listener){
        if(listener != null)
            mAnimationListeners.add(listener);
    }
}
