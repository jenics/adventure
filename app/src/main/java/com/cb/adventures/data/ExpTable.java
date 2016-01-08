package com.cb.adventures.data;

/**
 * Created by chengbo01 on 2016/1/8.
 * email : jenics@live.com
 * 经验表，描述每级所需要的经验才能升到下一级
 */
public class ExpTable {
    private int level;
    private long exp;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }
}
