package com.cb.adventures.factory;

import com.cb.adventures.view.Sprite;

/**
 * Created by jenics on 2015/10/21.
 */
public interface IMonsterFactory {
    public Sprite create(int id);
}
