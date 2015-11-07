package com.cb.adventures.data;

import com.cb.adventures.view.Sprite;

import java.util.LinkedList;

/**
 * Created by jenics on 2015/11/7.
 */
public class MapPropetry {
    public class MonsterPack {
        private int monsterId;
        private int monsterNum;

        public int getMonsterId() {
            return monsterId;
        }

        public void setMonsterId(int monsterId) {
            this.monsterId = monsterId;
        }

        public int getMonsterNum() {
            return monsterNum;
        }

        public void setMonsterNum(int monsterNum) {
            this.monsterNum = monsterNum;
        }
    }
    private int mapId;
    private String srcName;
    private float mapLenRatio;
    private LinkedList<MonsterPack> monsterPaks;
    public MapPropetry() {
        monsterPaks = new LinkedList<>();
    }

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public float getMapLenRatio() {
        return mapLenRatio;
    }

    public void setMapLenRatio(float mapLenRatio) {
        this.mapLenRatio = mapLenRatio;
    }

    public LinkedList<MonsterPack> getMonsterPaks() {
        return monsterPaks;
    }

    public void setMonsterPaks(LinkedList<MonsterPack> monsterPaks) {
        this.monsterPaks = monsterPaks;
    }
}
