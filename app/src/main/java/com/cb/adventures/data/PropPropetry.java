package com.cb.adventures.data;

import com.cb.adventures.utils.CLog;

/**
 * 道具属性
 * Created by chengbo01 on 2015/12/29.
 * email : jenics@live.com
 */
public class PropPropetry implements Cloneable {
    public static final int PROP_TYPE_CONSUME = 0;
    public static final int PROP_TYPE_EQUIP = 1;
    public static final int PROP_TYPE_MONEY = 2;
    /**
     * 对象公用，自增的
     */
    private static long incrementId = 0;
    /**
     * 消耗品id
     */
    private int propId;

    /**
     * 对象ID
     */
    private long objId;

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
     * 额外信息
     */
    private String extra;

    /**
     * 消耗品类型
     */
    private int propType;



    /**
     * 最大堆叠数量
     */
    private int maxStackSize;

    public PropPropetry() {
        maxStackSize = 1;
    }

    public int getPropId() {
        return propId;
    }

    public long incrementId() {
        return  ++incrementId;
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

    public long getObjId() {
        return objId;
    }

    public void setObjId(long objId) {
        this.objId = objId;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getPropType() {
        return propType;
    }

    public void setPropType(int propType) {
        this.propType = propType;
    }

    public Object clone() {
        PropPropetry o = null;
        try {
            o = (PropPropetry) super.clone();
            o.objId = incrementId();
        } catch (CloneNotSupportedException e) {
            CLog.e("MonsterPropetry", "error in clone");
            e.printStackTrace();
        }
        return o;
    }
}
