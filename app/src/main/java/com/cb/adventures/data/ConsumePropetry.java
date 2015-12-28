package com.cb.adventures.data;

/**
 * 消耗品属性
 * Created by jenics on 2015/10/24.
 */
public class ConsumePropetry {
    /**
     * 消耗品id
     */
    private int consumeId;
    /**
     * 使用后增加血量
     */
    private int bloodVolume;
    /**
     * 使用后增加魔力值
     */
    private int magicVolume;

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

    /**
     * 最大堆叠数量
     */
    private int maxStackSize;

    public int getConsumeId() {
        return consumeId;
    }

    public void setConsumeId(int consumeId) {
        this.consumeId = consumeId;
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

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public void setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
    }
}
