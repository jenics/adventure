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
        private int monsterRank;

        public MonsterPack() {
            monsterRank = 0;
        }

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

        public int getMonsterRank() {
            return monsterRank;
        }

        public void setMonsterRank(int monsterRank) {
            this.monsterRank = monsterRank;
        }
    }
    private int mapId;
    private String srcName;
    private float mapLenRatio;
    /**
     * 地图的名字
     */
    private String name;
    private LinkedList<MonsterPack> monsterPaks;
    public MapPropetry() {
        monsterPaks = new LinkedList<>();
        preGate = -1;
        nextGate = -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 上一关地图ID
     */
    private int preGate;
    /**
     * 下一关地图ID
     */
    private int nextGate;

    public int getPreGate() {
        return preGate;
    }

    public void setPreGate(int preGate) {
        this.preGate = preGate;
    }

    public int getNextGate() {
        return nextGate;
    }

    public void setNextGate(int nextGate) {
        this.nextGate = nextGate;
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
