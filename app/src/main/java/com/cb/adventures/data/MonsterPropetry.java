package com.cb.adventures.data;


import com.cb.adventures.utils.CLog;

import java.util.LinkedList;

/**
 * Created by jenics on 2015/11/7.
 */
public class MonsterPropetry extends Propetry implements Cloneable{
    private int monsterId;
    private String monsterName;
    private SrcInfo srcInfo;
    private LinkedList<Frame> leftFrames;
    private LinkedList<Frame> rightFrames;
    private LinkedList<DropItem> dropItems;

    public MonsterPropetry(){
        monsterId = 0;
    }

    public LinkedList<DropItem> getDropItems() {
        return dropItems;
    }

    public void setDropItems(LinkedList<DropItem> dropItems) {
        this.dropItems = dropItems;
    }

    public int getMonsterId() {
        return monsterId;
    }

    public void setMonsterId(int monsterId) {
        this.monsterId = monsterId;
    }

    public String getMonsterName() {
        return monsterName;
    }

    public void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    public SrcInfo getSrcInfo() {
        return srcInfo;
    }

    public void setSrcInfo(SrcInfo srcInfo) {
        this.srcInfo = srcInfo;
    }

    public LinkedList<Frame> getLeftFrames() {
        return leftFrames;
    }

    public void setLeftFrames(LinkedList<Frame> leftFrames) {
        this.leftFrames = leftFrames;
    }

    public LinkedList<Frame> getRightFrames() {
        return rightFrames;
    }

    public void setRightFrames(LinkedList<Frame> rightFrames) {
        this.rightFrames = rightFrames;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public Object clone() {
        MonsterPropetry o = null;
        try {
            o = (MonsterPropetry) super.clone();
        } catch (CloneNotSupportedException e) {
            CLog.e("MonsterPropetry","error in clone");
            e.printStackTrace();
        }
        return o;
    }
}
