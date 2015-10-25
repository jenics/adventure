package com.cb.adventures.factory;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.data.GameData;
import com.cb.adventures.data.SkillPropetry;
import com.cb.adventures.skill.HitEffectSkill;
import com.cb.adventures.skill.Skill;
import com.cb.adventures.skill.StaticFrameSkill;

/**
 * Created by jenics on 2015/10/25.
 */
public class SkillFactory implements IFactory {
    @Override
    public Skill create(int id) {
        SkillPropetry skillPropetry = GameData.getInstance().findSkill(id);
        Skill skill = null;
        if(skillPropetry != null) {
            if(skillPropetry.getSkillType() == GameConstants.SKILL_STATIC_FRAME) {
                skill = new StaticFrameSkill();
                skill.setSkillPropetry(skillPropetry);
            }else if(skillPropetry.getSkillType() == GameConstants.SKILL_HIT_EFFECTIVE) {
                skill = new HitEffectSkill();
                skill.setSkillPropetry(skillPropetry);
            }
        }

        return skill;
    }
}
