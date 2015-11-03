package com.cb.adventures.data;

import com.cb.adventures.observer.IPropetryObserver;

import java.util.LinkedList;

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

    /**
     * 封装一套属性改变，用来做观察者模式
     * 放在纯净数据里显得不合适
     */
    private LinkedList<IPropetryObserver> propetryObservers;
    public void notifyPropetryChange() {
        for(IPropetryObserver observer : propetryObservers) {
            observer.onPropetryChange(this);
        }
    }


    public Propetry() {
        propetryObservers = new LinkedList<>();
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
        notifyPropetryChange();
    }

    public int getDefensivePower() {
        return defensivePower;
    }

    public void setDefensivePower(int defensivePower) {
        this.defensivePower = defensivePower;
        notifyPropetryChange();
    }

    public int getBloodVolume() {
        return bloodVolume;
    }

    public void setBloodVolume(int bloodVolume) {
        if(bloodVolume < 0) {
            this.bloodVolume = 0;
        } else {
            this.bloodVolume = bloodVolume;
        }
        notifyPropetryChange();
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
        if(magicVolume < 0) {
            this.magicVolume = 0;
        } else {
            this.magicVolume = magicVolume;
        }
        notifyPropetryChange();
    }

    public int getAttackLength() {
        return attackLength;
    }

    public void setAttackLength(int attackLength) {
        this.attackLength = attackLength;
        notifyPropetryChange();
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
        notifyPropetryChange();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        notifyPropetryChange();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        notifyPropetryChange();
    }

    public int getMagicTotalVolume() {
        return magicTotalVolume;
    }

    public void setMagicTotalVolume(int magicTotalVolume) {
        this.magicTotalVolume = magicTotalVolume;
        notifyPropetryChange();
    }

    public int getBloodTotalVolume() {
        return bloodTotalVolume;
    }

    public void setBloodTotalVolume(int bloodTotalVolume) {
        this.bloodTotalVolume = bloodTotalVolume;
        notifyPropetryChange();
    }
}
