package com.cb.adventures.animation;

/**
 * Created by jenics on 2015/9/15.
 */
public interface IAnimation {
    public void startAnimation();
    public void stopAnimation();

    /**
     *
     * @return true if animation finish
     */
    public boolean animate();
}


