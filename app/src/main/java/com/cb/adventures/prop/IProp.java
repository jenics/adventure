package com.cb.adventures.prop;


/**
 * 道具接口
 * Created by jenics on 2015/12/28.
 */
public interface IProp extends IUsable  {
    /**
     * @return 道具ID，同一种道具一个ID
     */
    long getPropId();

    /**
     * @return 对象ID
     */
    long getObjId();

    /**
     * @return 返回道具描述
     */
    String[] getDescription();

    /**
     * @return 返回道具名
     */
    String getName();

    String getExtra();
}
