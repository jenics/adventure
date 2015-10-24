package com.cb.adventures.data;

/**
 * 属性类
 * Created by jenics on 2015/10/24.
 */
public class Propetry {
    /**
     * 攻击力
     */
    private int attackPower;
    /**
     * 防御力
     */
    private int defensivePower;
    /**
     * 血量
     */
    private int bloodVolume;
    /**
     * 魔力值
     */
    private int magicVolume;
    /**
     * 血量总量
     */
    private int bloodTotalVolume;
    /**
     * 魔力值总量
     */
    private int magicTotalVolume;
    /**
     * 攻击距离
     */
    private int attackLength;
    /**
     * 等级
     */
    private int rank;

    /**
     * 宽度
     */
    private int width;

    /**
     * 高度
     */
    private int height;

    public Propetry() {
        this.attackPower = 0;
        this.defensivePower = 0;
        this.bloodVolume = 0;
        this.magicVolume = 0;
        this.bloodTotalVolume = 0;
        this.magicTotalVolume = 0;
        this.attackLength = 0;
        this.rank = 0;
        this.width = 0;
        this.height = 0;
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

    public float getMagicRatio() {
        return magicVolume*1.0f / magicTotalVolume;
    }

    public float getBloodRatio() {
        return bloodVolume*1.0f / bloodTotalVolume;
    }

    public void setMagicVolume(int magicVolume) {
        this.magicVolume = magicVolume;
    }

    public int getAttackLength() {
        return attackLength;
    }

    public void setAttackLength(int attackLength) {
        this.attackLength = attackLength;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMagicTotalVolume() {
        return magicTotalVolume;
    }

    public void setMagicTotalVolume(int magicTotalVolume) {
        this.magicTotalVolume = magicTotalVolume;
    }

    public int getBloodTotalVolume() {
        return bloodTotalVolume;
    }

    public void setBloodTotalVolume(int bloodTotalVolume) {
        this.bloodTotalVolume = bloodTotalVolume;
    }
}
