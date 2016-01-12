package com.cb.adventures.data;

/**
 * Created by chengbo01 on 2016/1/11.
 * email : jenics@live.com
 */
public class PlayerPropetry extends Propetry {
    private long money;

    /**
     * 当前经验
     */
    private long curExp;
    /**
     * 升级所需经验
     */
    private long levelupExp;

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getCurExp() {
        return curExp;
    }

    public void setCurExp(long curExp) {
        this.curExp = curExp;
    }

    public long getLevelupExp() {
        return levelupExp;
    }

    public void setLevelupExp(long levelupExp) {
        this.levelupExp = levelupExp;
    }
}
