package com.cb.adventures.prop;

import android.widget.Toast;

import com.cb.adventures.application.AdventureApplication;
import com.cb.adventures.data.EquipmentPropetry;
import com.cb.adventures.data.PropPropetry;
import com.cb.adventures.view.Player;
import com.cb.adventures.view.ui.EquipmentBar;
import com.cb.adventures.view.ui.InventoryView;

/**
 * 装备
 * Created by jenics on 2015/12/28.
 */
public class Equipment implements IEquipment{
    private EquipmentPropetry equipmentPropetry;
    private Player mPlayer;
    public Equipment(Player player, PropPropetry propetry) {
        equipmentPropetry = (EquipmentPropetry) propetry;
        mPlayer = player;
    }

    public EquipmentPropetry getEquipmentPropetry() {
        return equipmentPropetry;
    }

    @Override
    public long getPropId() {
        return equipmentPropetry.getPropId();
    }

    @Override
    public long getObjId() {
        return equipmentPropetry.getObjId();
    }

    @Override
    public String[] getDescription() {
        return new String[] {
               String.format("攻击力: %d",equipmentPropetry.getAttackPower()),
                String.format("防御力: %d",equipmentPropetry.getDefensivePower()),
                String.format("血量增幅: %d",equipmentPropetry.getBloodVolume()),
                String.format("魔量增幅: %d",equipmentPropetry.getMagicVolume()),
                String.format("装备等级: %d", equipmentPropetry.getRank()),
        };
    }

    @Override
    public void use() {
        equip();
    }

    @Override
    public void equip() {
        if (mPlayer.getRank() >= getRank()) {
            IEquipment iEquipment = EquipmentBar.getInstance().equipment(this);
            int index = InventoryView.getInstance().removeProp(this);
            if (iEquipment != null) {
                InventoryView.getInstance().addProp(index,iEquipment);
            }
        } else {
            Toast.makeText(AdventureApplication.getContextObj(),"装备等级过高",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void unEquip() {
        EquipmentBar.getInstance().unEquipment(this);
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
    public String getExtra() {
        return equipmentPropetry.getExtra();
    }

    @Override
    public String getIcon() {
        return equipmentPropetry.getIcon();
    }

    @Override
    public int getAttackPower() {
        return equipmentPropetry.getAttackPower();
    }

    @Override
    public int getDefensivePower() {
        return equipmentPropetry.getDefensivePower();
    }

    @Override
    public int getRank() {
        return equipmentPropetry.getRank();
    }

    @Override
    public int getMagicTotalVolume() {
        return equipmentPropetry.getMagicVolume();
    }

    @Override
    public int getBloodTotalVolume() {
        return equipmentPropetry.getBloodVolume();
    }
}
