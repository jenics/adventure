package com.cb.adventures.data;

/**
 * Created by jenics on 2016/1/1.
 * 掉落物品类
 */
public class DropItem {
    /**
     * 物品ID
     */
    int itemId;
    /**
     * 掉落概率
     */
    int probability;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }
}
