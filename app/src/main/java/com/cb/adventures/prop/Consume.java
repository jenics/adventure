package com.cb.adventures.prop;

import com.cb.adventures.data.ConsumePropetry;
import com.cb.adventures.view.Player;

/**
 * 消耗品
 * Created by jenics on 2015/12/28.
 */
public class Consume implements IProp{
    private ConsumePropetry consumePropetry;
    /**
     * 堆叠数量
     */
    private int stackSize;
    private Player mPlayer;
    Consume(Player player) {
        mPlayer = player;
    }

    public ConsumePropetry getConsumePropetry() {
        return consumePropetry;
    }

    public void setConsumePropetry(ConsumePropetry consumePropetry) {
        this.consumePropetry = consumePropetry;
    }

    @Override
    public long getPropId() {
        return consumePropetry.getPropId();
    }

    @Override
    public String getDescription() {
        return consumePropetry.getDesc();
    }

    @Override
    public String getName() {
        return consumePropetry.getName();
    }

    @Override
    public String getIcon() {
        return consumePropetry.getIcon();
    }

    @Override
    public void use() {

    }

    @Override
    public int getMaxStackSize() {
        return consumePropetry.getMaxStackSize();
    }
}
