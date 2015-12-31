package com.cb.adventures.prop;

import com.cb.adventures.data.ConsumePropetry;
import com.cb.adventures.data.PropPropetry;
import com.cb.adventures.view.Player;

import java.util.LinkedList;

/**
 * 消耗品
 * Created by jenics on 2015/12/28.
 */
public class Consume implements IProp{
    private ConsumePropetry consumePropetry;
    /**
     * 堆叠数量
     */
    private int currentStackSize;
    private Player mPlayer;
    public Consume(Player player , PropPropetry propPropetry) {
        consumePropetry = (ConsumePropetry) propPropetry;
        mPlayer = player;
        currentStackSize = 1;
    }

    /**
     * 增加当前堆叠数量
     * @param size 欲增加的堆叠数量
     * @return 当前堆叠数量
     */
    public int addProp(int size) {
        if (currentStackSize + size > consumePropetry.getMaxStackSize()) {
            throw new IllegalStateException("overflow prop stack size");
        }
        currentStackSize = currentStackSize + size;
        return currentStackSize;
    }

    /**
     * 减少当前堆叠数量
     * @param size 欲减少的堆叠数量
     * @return 当前堆叠数量
     */
    public int reduceProp(int size) {
        if (currentStackSize - size < 0) {
            throw new IllegalStateException("cur stacksize less than 0");
        }
        currentStackSize = currentStackSize - size;
        return currentStackSize;
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
    public int getCurrentStackSize() {
        return currentStackSize;
    }
}
