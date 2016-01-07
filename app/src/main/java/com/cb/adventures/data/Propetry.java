package com.cb.adventures.data;

import com.cb.adventures.observer.IPropetryObserver;

import java.util.LinkedList;

/**
 * 属性类,所有有生命的，都继承自这个属性，比如怪物，玩家，BOSS
 * Created by jenics on 2015/10/24.
 */
public class Propetry {
    /**
     * 攻击力
     */
    protected int attackPower;
    /**
     * 防御力
     */
    protected int defensivePower;

    /**
     * 血量总量，只是基础属性，还要加上装备所附加的
     */
    protected int bloodTotalVolume;
    /**
     * 魔力值总量，只是基础属性，还要加上装备所附加的
     */
    protected int magicTotalVolume;

    /**
     * 等级
     */
    protected int rank;

    /**
     * 速度
     */
    protected float speed;

    /**
     * 暴击率
     */
    private float criticalRate;
    /**
     * 暴击伤害
     */
    private float criticalDamage;

    /**
     * 血量,当前血量
     */
    protected int bloodVolume;
    /**
     * 魔力值，当前魔力
     */
    protected int magicVolume;


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
        this.rank = 0;
        this.speed = 0;
        criticalDamage = 0.0f;
        criticalRate = 0.0f;
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



    public void setMagicVolume(int magicVolume) {
        if(magicVolume < 0) {
            this.magicVolume = 0;
        } else {
            this.magicVolume = magicVolume;
        }
        notifyPropetryChange();
    }


    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
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

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getCriticalRate() {
        return criticalRate;
    }

    public void setCriticalRate(float criticalRate) {
        this.criticalRate = criticalRate;
    }

    public float getCriticalDamage() {
        return criticalDamage;
    }

    public void setCriticalDamage(float criticalDamage) {
        this.criticalDamage = criticalDamage;
    }
}
