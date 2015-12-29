package com.cb.adventures.data;

/**
 * 道具属性
 * Created by chengbo01 on 2015/12/29.
 * email : jenics@live.com
 */
public class PropPropetry {
    /**
     * 消耗品id
     */
    private int propId;

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

    /**
     * 最大堆叠数量
     */
    private int maxStackSize;

    public int getPropId() {
        return propId;
    }

    public void setPropId(int propId) {
        this.propId = propId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

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

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public void setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
    }
}
