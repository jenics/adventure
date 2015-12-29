package com.cb.adventures.data;

/**
 * 消耗品属性
 * Created by jenics on 2015/10/24.
 */
public class ConsumePropetry extends PropPropetry {
    /**
     * 使用后增加血量
     */
    private int bloodVolume;
    /**
     * 使用后增加魔力值
     */
    private int magicVolume;

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

}
