package com.cb.adventures.animation;

import android.graphics.Canvas;

import com.cb.adventures.view.IDrawable;
import com.cb.adventures.view.IView;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by jenics on 2015/9/17.
 */
public class AnimationControl implements IDrawable {
    private static AnimationControl mInstance;

    private LinkedList<IAnimation> mQueueAnimaion;
    private LinkedList<IAnimation> delayAddViewAnimations;

    private AnimationControl() {
        mQueueAnimaion = new LinkedList<>();
        delayAddViewAnimations = new LinkedList<>();
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
     * @param animation 动画接口
     */
    public synchronized void addAnimation(IAnimation animation) {
        delayAddViewAnimations.add(animation);
    }

    /**
     * 清空所有动画
     */
    public synchronized void clear() {
        if (mQueueAnimaion != null)
            mQueueAnimaion.clear();
        if (delayAddViewAnimations != null)
            delayAddViewAnimations.clear();
    }

    public synchronized void animate() {
        Iterator<IAnimation> iterator = mQueueAnimaion.iterator();
        while (iterator.hasNext()) {
            IAnimation animation = iterator.next();
            if (animation.animate() || animation.isStop()) {
                iterator.remove();
                animation.notifyAnimationEnd(animation.isStop());
            }
        }
        if (!delayAddViewAnimations.isEmpty()) {
            mQueueAnimaion.addAll(delayAddViewAnimations);
            delayAddViewAnimations.clear();
        }
    }

    @Override
    public synchronized void draw(Canvas canvas) {
        for (IAnimation animation : mQueueAnimaion) {
            animation.draw(canvas);
        }
    }

    public LinkedList<IAnimation> getQueueAnimaion() {
        return mQueueAnimaion;
    }
}
