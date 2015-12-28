package com.cb.adventures.data;

/**
 * 装备属性
 * Created by jenics on 2015/10/24.
 */
public class EquipmentPropetry {
    /**
     * 装备id
     */
    private int equipmentId;
    /**
     * 攻击力
     */
    private int attackPower;
    /**
     * 防御力
     */
    private int defensivePower;
    /**
     * 血量增幅
     */
    private int bloodVolume;
    /**
     * 魔力增幅
     */
    private int magicVolume;

    /**
     * 穿戴等级
     */
    private int rank;

    /**
     * 装备位置
     */
    private int loc;

    /**
     * 描述
     */
    private String desc;

    /**
     * 图标名
     */
    private String icon;

    /**
     * 名字
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public int getDefensivePower() {
        return defensivePower;
    }

    public void setDefensivePower(int defensivePower) {
        this.defensivePower = defensivePower;
    }

    public int getBloodVolume() {
        return bloodVolume;
    }

    public void setBloodVolume(int bloodVolume) {
        this.bloodVolume = bloodVolume;
    }

    public int getMagicVolume() {
        return magicVolume;
    }

    public void setMagicVolume(int magicVolume) {
        this.magicVolume = magicVolume;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getLoc() {
        return loc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }
}
