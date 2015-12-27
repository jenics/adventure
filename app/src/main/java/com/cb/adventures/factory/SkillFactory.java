package com.cb.adventures.factory;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.data.GameData;
import com.cb.adventures.data.SkillPropetry;
import com.cb.adventures.skill.MoveFrameSkill;
import com.cb.adventures.skill.Skill;
import com.cb.adventures.skill.StaticFrameSkill;
import com.cb.adventures.skill.TimeFrameSkill;

/**
 * Created by jenics on 2015/10/25.
 */
public class SkillFactory implements IFactory {
    @Override
    public Skill create(int id) {
        SkillPropetry skillPropetry = GameData.getInstance().getSkillPropetry(id);
        Skill skill = null;
        if(skillPropetry != null) {
            if(skillPropetry.getAnimationType() == GameConstants.ANIMATION_STATIC_FRAME) {
                skill = new StaticFrameSkill();
                skill.setSkillPropetry(skillPropetry);
            }else if (skillPropetry.getAnimationType() == GameConstants.ANIMATION_STATIC_TIME_FRAME) {
                skill = new TimeFrameSkill();
                skill.setSkillPropetry(skillPropetry);
            } else if (skillPropetry.getAnimationType() == GameConstants.ANIMATION_MOVE_FRAME) {
                skill = new MoveFrameSkill();
                skill.setSkillPropetry(skillPropetry);
            } else if (skillPropetry.getAnimationType() == -1) {
                skill = new Skill();
                skill.setSkillPropetry(skillPropetry);
            }
        }
        return skill;
    }
}
