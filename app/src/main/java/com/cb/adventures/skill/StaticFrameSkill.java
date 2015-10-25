package com.cb.adventures.skill;

import android.graphics.Canvas;

import com.cb.adventures.view.BaseView;

/**
 * Created by AI on 2015/10/25.
 */
public class StaticFrameSkill extends Skill{
    public StaticFrameSkill(BaseView view, int skillKind) {
        super(view, skillKind);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
