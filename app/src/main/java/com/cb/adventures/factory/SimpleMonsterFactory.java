package com.cb.adventures.factory;

import com.cb.adventures.data.GameData;
import com.cb.adventures.data.MonsterPropetry;
import com.cb.adventures.view.Sprite;

/**
 * 简单难度怪物工厂
 * Created by jenics on 2015/10/21.
 */
public class SimpleMonsterFactory implements IFactory {
    public SimpleMonsterFactory() {
    }

    @Override
    public Sprite create(int id) {
        Sprite sprite = null;
        MonsterPropetry monsterPropetry = GameData.getInstance().getMonsterPropetry(id);
        sprite = new Sprite((MonsterPropetry) monsterPropetry.clone());
        return sprite;
    }
}
