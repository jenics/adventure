package com.cb.adventures.data;

import com.cb.adventures.skill.Skill;

import java.util.LinkedList;

/**
 * 技能属性
 * Created by jenics on 2015/10/25.
 */
public class SkillPropetry {
    /**
     * 技能ID，一个技能一个
     */
    private int skillId;

    /**
     * 技能类型，多个技能公用一个ID
     */
    private int skillType;

    /**
     * 技能附加伤害
     */
    private int extraAttack;

    /**
     * 作用目标,可以是多个，使用&
     */
    private int effectTarget;

    /**
     * 附加血量
     */
    private int extraBlood;

    /**
     * 附加魔力值
     */
    private int extraMagic;

    /**
     * 附加防御
     */
    private int extraDefensive;

    /**
     * 释放技能需要魔力值
     */
    private int freeMagic;

    private String name;

    private SrcInfo srcInfo;

    /**
     * 技能持续时间,单位ms
     */
    private long timeDuration;

    /**
     * 技能起始偏移
     */
    private int offsetX;

    /**
     * 击中效果技能id
     */
    private int hitEffectId;

    /**
     * 最大移动距离，只有MoveFrameSkill关心
     */
    private float maxMoveDistance;

    private LinkedList<Frame> frames;

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public int getSkillType() {
        return skillType;
    }

    public void setSkillType(int skillType) {
        this.skillType = skillType;
    }

    public int getExtraAttack() {
        return extraAttack;
    }

    public void setExtraAttack(int extraAttack) {
        this.extraAttack = extraAttack;
    }

    public int getEffectTarget() {
        return effectTarget;
    }

    public void setEffectTarget(int effectTarget) {
        this.effectTarget = effectTarget;
    }

    public int getExtraBlood() {
        return extraBlood;
    }

    public void setExtraBlood(int extraBlood) {
        this.extraBlood = extraBlood;
    }

    public int getExtraMagic() {
        return extraMagic;
    }

    public void setExtraMagic(int extraMagic) {
        this.extraMagic = extraMagic;
    }

    public int getExtraDefensive() {
        return extraDefensive;
    }

    public void setExtraDefensive(int extraDefensive) {
        this.extraDefensive = extraDefensive;
    }

    public int getFreeMagic() {
        return freeMagic;
    }

    public void setFreeMagic(int freeMagic) {
        this.freeMagic = freeMagic;
    }

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

    public int getHitEffectId() {
        return hitEffectId;
    }

    public void setHitEffectId(int hitEffectId) {
        this.hitEffectId = hitEffectId;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public long getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(long timeDuration) {
        this.timeDuration = timeDuration;
    }



    public SkillPropetry() {
        if(frames == null) {
            frames = new LinkedList<>();
        }
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
}
