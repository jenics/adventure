package com.cb.adventures.view;

import com.cb.adventures.skill.Skill;

/**
 * Created by jenics on 2015/11/8.
 * 受伤能力接口
 */
public interface IHurtable {
    /**
     * 受到技能伤害
     * @param skill 技能
     */
    void onHurted(Skill skill);
}
