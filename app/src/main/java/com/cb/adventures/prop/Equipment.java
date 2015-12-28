package com.cb.adventures.prop;

import com.cb.adventures.data.EquipmentPropetry;
import com.cb.adventures.view.Player;

/**
 * 装备
 * Created by jenics on 2015/12/28.
 */
public class Equipment implements IEquipment{
    /**
     * 盔甲
     */
    public static final int EQUIP_ARMOUR = 0;
    /**
     * 武器
     */
    public static final int EQUIP_WEAPON = 1;
    private Player mPlayer;
    private EquipmentPropetry equipmentPropetry;
    Equipment(Player player) {
        mPlayer = player;
    }

    public EquipmentPropetry getEquipmentPropetry() {
        return equipmentPropetry;
    }

    public void setEquipmentPropetry(EquipmentPropetry equipmentPropetry) {
        this.equipmentPropetry = equipmentPropetry;
    }

    @Override
    public long getPropId() {
        return equipmentPropetry.getEquipmentId();
    }

    @Override
    public String getDescription() {
        return equipmentPropetry.getDesc();
    }

    @Override
    public void use() {
        equip();
    }


    @Override
    public int getMaxStackSize() {
        return 0;
    }

    @Override
    public void equip() {
        mPlayer.equipment(this);
    }

    @Override
    public void unEquip() {
        mPlayer.unEquipment(this);
    }

    @Override
    public int getEquipLocation() {
        return equipmentPropetry.getLoc();
    }

    @Override
    public String getName() {
        return equipmentPropetry.getName();
    }
}
