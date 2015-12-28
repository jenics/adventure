package com.cb.adventures.prop;

/**
 * Created by jenics on 2015/12/28.
 */
public interface IEquipment extends IProp {
    /**
     * 装备
     */
    void equip();

    /**
     * 卸下装备
     */
    void unEquip();

    /**
     * @return 装备位置，头，脚。腿等等
     */
    int getEquipLocation();
}
