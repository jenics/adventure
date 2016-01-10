package com.cb.adventures.animation;

import android.graphics.Canvas;

import com.cb.adventures.view.IDrawable;
import com.cb.adventures.view.IView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by jenics on 2015/9/17.
 * 动画控制器，核心类
 */
public class AnimationControl implements IDrawable {
    private static AnimationControl mInstance;

    private LinkedList<IAnimation> mQueueAnimaion;
    private LinkedList<IAnimation> delayAddViewAnimations;
    private final ReentrantReadWriteLock mReentrantReadWriteLock = new ReentrantReadWriteLock();

    public ReentrantReadWriteLock getReentrantReadWriteLock() {
        return mReentrantReadWriteLock;
    }

    private AnimationControl() {
        mQueueAnimaion = new LinkedList<>();
        delayAddViewAnimations = new LinkedList<>();
    }

    /**
     * 获得实例
     *
     * @return 实例
     */
    public static synchronized AnimationControl getInstance() {
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
    public  void addAnimation(IAnimation animation) {
        mReentrantReadWriteLock.writeLock().lock();
        delayAddViewAnimations.add(animation);
        mReentrantReadWriteLock.writeLock().unlock();
    }

    /**
     * 清空所有动画
     */
    public void clear() {
        mReentrantReadWriteLock.writeLock().lock();
        if (mQueueAnimaion != null)
            mQueueAnimaion.clear();
        if (delayAddViewAnimations != null)
            delayAddViewAnimations.clear();
        mReentrantReadWriteLock.writeLock().unlock();
    }

    public void animate() {
        mReentrantReadWriteLock.writeLock().lock();
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
        mReentrantReadWriteLock.writeLock().unlock();
    }

    @Override
    public void draw(Canvas canvas) {
        mReentrantReadWriteLock.readLock().lock();
        for (IAnimation animation : mQueueAnimaion) {
            if (animation instanceof IView)
                ((IView)animation).draw(canvas);
        }
        mReentrantReadWriteLock.readLock().unlock();
    }

    public LinkedList<IAnimation> getQueueAnimaion() {
        return mQueueAnimaion;
    }
}
