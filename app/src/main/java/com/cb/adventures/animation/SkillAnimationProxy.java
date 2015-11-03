package com.cb.adventures.animation;

import com.cb.adventures.view.BaseView;

/**
 * 技能动画proxy
 * Created by jenics on 2015/10/25.
 */
public class SkillAnimationProxy extends Animation {
    public SkillAnimationProxy(BaseView view) {
        super(view);
    }

    @Override
    public boolean animate() {
        return getView().nextFrame();
    }
}
