package com.cb.adventures.animation;

/**
 * Created by jenics on 2015/9/15.
 */
public interface IAnimation {
    void startAnimation();
    void stopAnimation();

    /**
     *
     * @return true if animation finish
     */
    boolean animate();
}


