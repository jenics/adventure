package com.cb.adventures.animation;

import com.cb.adventures.view.BaseView;

import java.util.Stack;

/**
 * Created by jenics on 2015/9/20.
 * 动画集
 */
public class AnimationSet implements ViewAnimation.OnAniamtionListener {
    public static int ANIMATION_DEAL = 0;           ///发牌动画
    public static int ANIMATION_MOVE = 1;           ///移动扑克动画
    public static int ANIMATION_WIN_GROUP = 2;        ///胜利一组
    public static int ANIMATION_WIN_GAME = 3;       ///游戏胜利
    public static int ANIMATION_AUTO_DEAL = 4;      ///开局自动发扑克

    private int animationType;
    private OnAnimationSetListener onAnimationSetListener;
    public interface OnAnimationSetListener {
        public void onAnimationSetEnd(int type);
    }
    public void setOnAnimationSetListener(OnAnimationSetListener listener){
        onAnimationSetListener = listener;
    }

    private Stack<ViewAnimation> viewAnimations;
    private int animationSize = 0;
    private int animationCount = 0;
    private boolean isSyn = false;
    public AnimationSet(int type) {
        viewAnimations = new Stack<>();
        animationType = type;
        animationCount = 0;
        animationSize = 0;
    }
    @Override
    public void onAnimationEnd(BaseView view,boolean isForce) {
        if(isSyn) {
            animationCount++;
            if (animationCount == animationSize) {
                if(onAnimationSetListener != null) {
                    onAnimationSetListener.onAnimationSetEnd(animationType);
                }
            }

            viewAnimations.clear();
        }else {
            ViewAnimation viewAnimation = viewAnimations.pop();
            if(viewAnimation != null){
                viewAnimation.setOnAnimationListener(this);
                viewAnimation.startAnimation();
            }else {
                if(onAnimationSetListener != null) {
                    onAnimationSetListener.onAnimationSetEnd(animationType);
                }
                viewAnimations.clear();
            }
        }
    }

    @Override
    public void onAnimationBegin() {

    }

    public void pushAnimation(ViewAnimation viewAnimation) {
        viewAnimations.add(viewAnimation);
    }

    /**
     * 同步开启动画
     */
    public void synStartAnimations() {
        isSyn = true;
        animationSize = viewAnimations.size();
        if(animationSize == 0) {
            if(onAnimationSetListener != null) {
                onAnimationSetListener.onAnimationSetEnd(animationType);
            }
        }
        for(ViewAnimation viewAnimation : viewAnimations) {
            viewAnimation.setOnAnimationListener(this);
            viewAnimation.startAnimation();
        }
    }

    /**
     * 异步开始动画
     */
    public void asynStartAnimations() {
        isSyn = false;
        animationSize = viewAnimations.size();
        if(animationSize == 0) {
            if(onAnimationSetListener != null) {
                onAnimationSetListener.onAnimationSetEnd(animationType);
            }
        }
        ViewAnimation viewAnimation = viewAnimations.pop();
        if(viewAnimation != null){
            viewAnimation.setOnAnimationListener(this);
            viewAnimation.startAnimation();
        }
    }
}
