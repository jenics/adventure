package com.cb.adventures.data;

/**
 * Created by chengbo01 on 2016/1/11.
 * email : jenics@live.com
 */
public class MoneyPropetry extends PropPropetry {
    private long money;

    public MoneyPropetry() {
        setPropType(PROP_TYPE_MONEY);
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }
}
