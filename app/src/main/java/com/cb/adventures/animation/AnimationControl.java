package com.cb.adventures.animation;

import android.graphics.Canvas;

import com.cb.adventures.animation.Animation;
import com.cb.adventures.view.IView;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by jenics on 2015/9/17.
 */
public class AnimationControl implements IView
{
    private static AnimationControl mInstance;

    private LinkedList<Animation> mQueueAnimaion;
    private LinkedList<Animation> delayAddAnimations;

    private AnimationControl() {
        mQueueAnimaion = new LinkedList<>();
        delayAddAnimations = new LinkedList<>();
    }

    /**
     * 获得实例
     *
     * @return 实例
     */
    public synchronized static AnimationControl getInstance() {
        if (mInstance == null) {
            mInstance = new AnimationControl();
        }
        return mInstance;
    }

    /**
     * 加入动画列表
     *
     * @param animation
     */
    public synchronized void addAnimation(Animation animation) {
        delayAddAnimations.add(animation);
    }

    public synchronized void clear() {

        if (mQueueAnimaion != null)
            mQueueAnimaion.clear();
        if (delayAddAnimations != null)
            delayAddAnimations.clear();

    }

    public synchronized void animate() {
        Iterator<Animation> iterator = mQueueAnimaion.iterator();
        while (iterator.hasNext()) {
            Animation animation = iterator.next();
            if (animation.animate() || animation.isStop()) {
                iterator.remove();
                animation.notifyAnimationEnd(animation.isStop);
            }
        }
        if (!delayAddAnimations.isEmpty()) {
            mQueueAnimaion.addAll(delayAddAnimations);
            delayAddAnimations.clear();
        }
    }

    @Override
    public boolean isClickable() {
        return false;
    }

    @Override
    public boolean isVisiable() {
        return false;
    }

    @Override
    public void onClick() {

    }

    @Override
    public synchronized void draw(Canvas canvas) {
        Iterator<Animation> iterator = mQueueAnimaion.iterator();
        while (iterator.hasNext()) {
            Animation animation = iterator.next();
            animation.getView().draw(canvas);
        }
    }

    public LinkedList<Animation> getQueueAnimaion() {
        return mQueueAnimaion;
    }
}
