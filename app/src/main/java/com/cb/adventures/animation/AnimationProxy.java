package com.cb.adventures.animation;

import com.cb.adventures.view.BaseView;

/**
 * 动画proxy
 * Created by jenics on 2015/10/25.
 */
public class AnimationProxy extends Animation {
    public AnimationProxy(BaseView view) {
        super(view);
    }

    @Override
    public boolean animate() {
        return getView().nextFrame();
    }
}
