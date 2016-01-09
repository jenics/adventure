package com.cb.adventures.prop;

import com.cb.adventures.data.ConsumePropetry;
import com.cb.adventures.data.PropPropetry;
import com.cb.adventures.data.Propetry;
import com.cb.adventures.view.PlayerMediator;


/**
 * 消耗品
 * Created by jenics on 2015/12/28.
 */
public class Consume implements IProp, IStackable {
    private ConsumePropetry consumePropetry;
    private PlayerMediator playerMediator;
    /**
     * 堆叠数量
     */
    private int currentStackSize;
    public Consume(PlayerMediator playerMediator, PropPropetry propPropetry) {
        consumePropetry = (ConsumePropetry) propPropetry;
        this.playerMediator = playerMediator;

        currentStackSize = 1;
    }

    /**
     * 增加当前堆叠数量
     *
     * @param size 欲增加的堆叠数量
     * @return 当前堆叠数量
     */
    @Override
    public int addStack(int size) {
        if (currentStackSize + size > consumePropetry.getMaxStackSize()) {
            throw new IllegalStateException("overflow prop stack size");
        }
        currentStackSize = currentStackSize + size;
        return currentStackSize;
    }

    /**
     * 减少当前堆叠数量
     *
     * @param size 欲减少的堆叠数量
     * @return 当前堆叠数量
     */
    @Override
    public int reduceStack(int size) {
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
    public long getObjId() {
        return consumePropetry.getObjId();
    }

    @Override
    public String[] getDescription() {
        return new String[]{consumePropetry.getDesc()};
    }

    @Override
    public String getName() {
        return consumePropetry.getName();
    }

    @Override
    public String getExtra() {
        return consumePropetry.getExtra();
    }

    @Override
    public String getIcon() {
        return consumePropetry.getIcon();
    }

    @Override
    public void use() {
        Propetry propetry = playerMediator.getPlayerPropetry();
        int add = propetry.getBloodVolume() + consumePropetry.getBloodVolume();
        if (add > playerMediator.getPlayerBloodTotalVolume()) {
            add = playerMediator.getPlayerBloodTotalVolume();
        }
        propetry.setBloodVolume(add);

        add = propetry.getMagicVolume() + consumePropetry.getMagicVolume();
        if (add > playerMediator.getPlayerMagicTotalVolume()) {
            add = playerMediator.getPlayerMagicTotalVolume();
        }
        propetry.setMagicVolume(add);
        reduceStack(1);

        IStackable iStackable = (IStackable) this;
        if (iStackable.getCurrentStackSize() == 0) {
            ///使用完了
            playerMediator.removeProp(this);
        }
    }

    @Override
    public int getCurrentStackSize() {
        return currentStackSize;
    }
}
