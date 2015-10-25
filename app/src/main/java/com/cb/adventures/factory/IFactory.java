package com.cb.adventures.factory;


/**
 * 使用抽象工厂，可以在替换游戏难度的时候将耦合度降到最低
 * Created by jenics on 2015/10/21.
 */
public interface IFactory {
    Object create(int id);
}
