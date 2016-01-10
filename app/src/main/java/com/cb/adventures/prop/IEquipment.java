package com.cb.adventures.prop;

import com.cb.adventures.data.IPropetry;

/**
 * Created by jenics on 2015/12/28.
 * 装备接口
 */
public interface IEquipment extends IProp , IPropetry{
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
