package com.cb.adventures.data;

/**
 * 装备属性
 * Created by jenics on 2015/10/24.
 */
public class EquipmentPropetry extends PropPropetry{

    private Propetry propetry;

    /**
     * 装备位置
     */
    private int loc;

    public EquipmentPropetry() {
        propetry = new Propetry();
        setPropType(PROP_TYPE_EQUIP);
    }


    public int getAttackPower() {
        return propetry.getAttackPower();
    }

    public void setAttackPower(int attackPower) {
        propetry.setAttackPower(attackPower);
    }

    public int getDefensivePower() {
        return propetry.getDefensivePower();
    }

    public void setDefensivePower(int defensivePower) {
        propetry.setDefensivePower(defensivePower);
    }

    public int getBloodTotalVolume() {
        return propetry.getBloodTotalVolume();
    }

    public void setBloodTotalVolume(int bloodVolume) {
        propetry.setBloodTotalVolume(bloodVolume);
    }

    public int getMagicTotalVolume() {
        return propetry.getMagicTotalVolume();
    }

    public void setMagicTotalVolume(int magicVolume) {
        propetry.setMagicTotalVolume(magicVolume);
    }

    public int getRank() {
        return propetry.getRank();
    }

    public void setRank(int rank) {
        propetry.setRank(rank);
    }

    public int getLoc() {
        return loc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }

    public float getSpeed() {
        return propetry.getSpeed();
    }

    public void setSpeed(float speed) {
        propetry.setSpeed(speed);
    }

    public float getCriticalRate() {
        return propetry.getCriticalRate();
    }

    public void setCriticalRate(float criticalRate) {
        propetry.setCriticalRate(criticalRate);
    }

    public float getCriticalDamage() {
        return propetry.getCriticalDamage();
    }

    public void setCriticalDamage(float criticalDamage) {
        propetry.setCriticalDamage(criticalDamage);
    }
}
