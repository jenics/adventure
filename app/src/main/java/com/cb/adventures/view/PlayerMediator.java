package com.cb.adventures.view;

import android.graphics.PointF;
import com.cb.adventures.data.Propetry;
import com.cb.adventures.prop.IEquipment;
import com.cb.adventures.prop.IProp;
import com.cb.adventures.view.ui.EquipmentBar;
import com.cb.adventures.view.ui.InventoryView;

/**
 * Created by jenics on 2016/1/9.
 * 玩家中介者模式，解除玩家与装备栏和物品栏的相互引用，解除玩家和地图之间的相互引用，解耦
 */
public class PlayerMediator {
    private EquipmentBar mEquipmentBar;
    private InventoryView mInventoryView;
    private Player mPlayer;
    private Map mMap;
    public PlayerMediator() {

    }

    public void setEquipmentBar(EquipmentBar EquipmentBar) {
        this.mEquipmentBar = EquipmentBar;
    }

    public void setInventoryView(InventoryView InventoryView) {
        this.mInventoryView = InventoryView;
    }

    public void setPlayer(Player player) {
        mPlayer = player;
    }

    public void setMap(Map map) {
        mMap = map;
    }

    public IEquipment equipment(IEquipment equipment) {
        return mEquipmentBar.equipment(equipment);
    }

    public int removeProp(IProp iProp) {
        return mInventoryView.removeProp(iProp);
    }

    public void addProp(int index, IProp prop) {
        mInventoryView.addProp(index, prop);
    }

    public void unEquipment(IEquipment equipment) {
        mEquipmentBar.unEquipment(equipment);
    }

    public int getEquipAttackPower() {
        return mEquipmentBar.getAttackPower();
    }

    public int getEquipDefensivePower() {
        return mEquipmentBar.getDefensivePower();
    }

    public int getEquipMagicTotalVolume() {
        return mEquipmentBar.getMagicTotalVolume();
    }

    public int getEquipBloodTotalVolume() {
        return mEquipmentBar.getBloodTotalVolume();
    }

    public float getEquipSpeed() {
        return mEquipmentBar.getSpeed();
    }

    public float getEquipCriticalRate() {
        return mEquipmentBar.getCriticalRate();
    }

    public float getEquipCriticalDamage() {
        return mEquipmentBar.getCriticalDamage();
    }

    public Propetry getPlayerPropetry() {
        return mPlayer.getPropetry();
    }

    public int getPlayerAttackPower() {
        return mPlayer.getAttackPower();
    }

    public int getPlayerDefensivePower() {
        return mPlayer.getDefensivePower();
    }

    public int getPlayerRank() {
        return mPlayer.getRank();
    }

    public int getPlayerMagicTotalVolume() {
        return mPlayer.getMagicTotalVolume();
    }

    public int getPlayerBloodTotalVolume() {
        return mPlayer.getBloodTotalVolume();
    }

    public float getPlayerSpeed() {
        return mPlayer.getSpeed();
    }

    public float getPlayerCriticalRate() {
        return mPlayer.getCriticalRate();
    }

    public float getPlayerCriticalDamage() {
        return mPlayer.getCriticalDamage();
    }

    public void setPlayerPt(float x,float y) {
        mPlayer.setPt(x,y);
    }

    public PointF getPlayerPt() {
        return mPlayer.getPt();
    }

    public void stopScroll() {
        mMap.stopScroll();
    }
}
