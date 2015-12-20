package com.cb.adventures.data;

import java.util.LinkedList;

/**
 * Created by jenics on 2015/11/29.
 */
public class AnimationPropetry {

    /**
     * 帧宽度的比例,有些帧过大，修正一下，比如0.3
     */
    private float actionRange;

    /**
     * 动画ID
     */
    private int animationId;

    /**
     * 动画名
     */
    private String name;

    /**
     * 动画类型
     */
    private int animationType;

    /**
     * 位图信息
     */
    private SrcInfo srcInfo;

    /**
     * 动画持续时间,单位ms
     */
    private long timeDuration;

    /**
     * 循环次数
     * 如果为-1 则是无限循环，0 和 1都是只循环一次
     */
    private int loopTimes;

    /**
     * 动画最大移动距离
     */
    private float maxMoveDistance;

    /**
     * 是否在最后一帧停下来
     */
    private boolean isStopInLast;

    private LinkedList<Frame> frames;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LinkedList<Frame> getFrames() {
        return frames;
    }

    public void setFrames(LinkedList<Frame> frames) {
        this.frames = frames;
    }

    public long getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(long timeDuration) {
        this.timeDuration = timeDuration;
    }

    public AnimationPropetry() {
        if(frames == null) {
            frames = new LinkedList<>();
        }
        actionRange = 1.0f;
        loopTimes = -1;         ///默认无限循环
        timeDuration = -1;
        maxMoveDistance = -1;
        isStopInLast = false;
    }

    public void setActionRange(float actionRange) {
        this.actionRange = actionRange;
    }

    public float getActionRange() {
        return actionRange;
    }

    public SrcInfo getSrcInfo() {
        return srcInfo;
    }

    public void setSrcInfo(SrcInfo srcInfo) {
        this.srcInfo = srcInfo;
    }

    public float getMaxMoveDistance() {
        return maxMoveDistance;
    }

    public void setMaxMoveDistance(float maxMoveDistance) {
        this.maxMoveDistance = maxMoveDistance;
    }

    public int getAnimationId() {
        return animationId;
    }

    public void setAnimationId(int animationId) {
        this.animationId = animationId;
    }

    public int getLoopTimes() {
        return loopTimes;
    }

    public void setLoopTimes(int loopTimes) {
        this.loopTimes = loopTimes;
    }

    public boolean isStopInLast() {
        return isStopInLast;
    }

    public void setIsStopInLast(boolean isStopInLast) {
        this.isStopInLast = isStopInLast;
    }

    public int getAnimationType() {return animationType;}

    public void setAnimationType(int type) {this.animationType = type;}
}
