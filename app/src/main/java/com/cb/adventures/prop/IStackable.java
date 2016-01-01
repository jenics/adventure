package com.cb.adventures.prop;

/**
 * 堆叠能力接口
 * Created by jenics on 2015/12/28.
 */
public interface IStackable {
    /**
     * @return 当前堆叠数量
     */
    int getCurrentStackSize();

    /**
     * 增加当前堆叠数量
     * @param size 欲增加的堆叠数量
     * @return 当前堆叠数量
     */
    int addStack(int size);

    /**
     * 减少当前堆叠数量
     * @param size 欲减少的堆叠数量
     * @return 当前堆叠数量
     */
    int reduceStack(int size) ;
}
