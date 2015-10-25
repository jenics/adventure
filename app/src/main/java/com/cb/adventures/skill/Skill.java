package com.cb.adventures.skill;

import android.graphics.Canvas;

import com.cb.adventures.animation.Animation;
import com.cb.adventures.view.BaseView;

/**
 * 技能类
 * Created by jenics on 2015/10/25.
 */
public class Skill extends BaseView {
    
    /**
     * 静态ID，用来产生技能伤害ID
     */
    public static int sId = 0;
    /**
     * 技能伤害ID，一个技能唯一一个
     */
    protected int mId;
    /**
     * 技能类型，多个技能公用一个ID
     */
    protected int mSkillKind;
    public Skill(BaseView view,int skillKind) {
        sId ++;
        mId = sId;
        mSkillKind = skillKind;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
