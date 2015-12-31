package com.cb.adventures.prop;

import com.cb.adventures.data.EquipmentPropetry;
import com.cb.adventures.data.PropPropetry;
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
    public Equipment(Player player, PropPropetry propetry) {
        mPlayer = player;
        equipmentPropetry = (EquipmentPropetry) propetry;
    }

    public EquipmentPropetry getEquipmentPropetry() {
        return equipmentPropetry;
    }



    @Override
    public long getPropId() {
        return equipmentPropetry.getPropId();
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

    @Override
    public String getIcon() {
        return equipmentPropetry.getIcon();
    }

    @Override
    public int getCurrentStackSize() {
        return 1;
    }
}
