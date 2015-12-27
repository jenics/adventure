package com.cb.adventures.data;

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
     * 动画属性
     */
    private AnimationPropetry animationPropetry;



    /**
     * 命中目标时中止技能
     */
    private boolean interruptWhileHit;



    /**
     * 技能类型
     * 可以是主动攻击，被动，BUFF,DEBUFF
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




    /**
     * 击中效果技能id
     */
    private int hitEffectId;



    /**
     * 技能ICON资源名
     */
    private String icon;


    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public int getAnimationType() {
        if (animationPropetry != null) {
            return animationPropetry.getAnimationType();
        }
        return -1;
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
        return animationPropetry.getFrames();
    }

    public int getHitEffectId() {
        return hitEffectId;
    }

    public void setHitEffectId(int hitEffectId) {
        this.hitEffectId = hitEffectId;
    }

    public long getTimeDuration() {
        return animationPropetry.getTimeDuration();
    }





    public SkillPropetry() {
        freeMagic = 0;      ///技能释放魔力值默认为0
        extraDefensive = 0; ///技能附加防御默认为0;
        extraAttack = 0;    ///技能附加攻击默认为0;
        extraBlood = 0;     ///技能附加血量默认为0;
        extraMagic = 0;     ///技能附加蓝默认为0
        interruptWhileHit = false;
    }



    public float getActionRange() {
        return animationPropetry.getActionRange();
    }

    public SrcInfo getSrcInfo() {
        return animationPropetry.getSrcInfo();
    }

    public float getMaxMoveDistance() {
        return animationPropetry.getMaxMoveDistance();
    }

    public int getSkillType() {
        return skillType;
    }

    public void setSkillType(int skillType) {
        this.skillType = skillType;
    }

    public boolean isInterruptWhileHit() {
        return interruptWhileHit;
    }

    public void setInterruptWhileHit(boolean interruptWhileHit) {
        this.interruptWhileHit = interruptWhileHit;
    }

    public AnimationPropetry getAnimationPropetry() {
        return animationPropetry;
    }

    public void setAnimationPropetry(AnimationPropetry animationPropetry) {
        this.animationPropetry = animationPropetry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
