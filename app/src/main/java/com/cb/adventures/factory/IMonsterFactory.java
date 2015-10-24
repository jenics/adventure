package com.cb.adventures.factory;

import com.cb.adventures.view.Sprite;

/**
 * 使用抽象工厂，可以在替换游戏难度的时候将耦合度降到最低
 * Created by jenics on 2015/10/21.
 */
public interface IMonsterFactory {
    public Sprite create(int id);
}
