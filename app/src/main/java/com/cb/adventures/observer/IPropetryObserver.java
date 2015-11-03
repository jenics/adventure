package com.cb.adventures.observer;

import com.cb.adventures.data.Propetry;

/**
 * Created by jenics on 2015/11/3.
 * 观察者模式之人物属性，属性框关心属性，还有血槽关心属性变化，都要引起UI联动
 */
public interface IPropetryObserver {
    void onPropetryChange(Propetry propetry);
}
