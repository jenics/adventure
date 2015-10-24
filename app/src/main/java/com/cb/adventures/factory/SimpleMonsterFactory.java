package com.cb.adventures.factory;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.utils.ImageLoader;
import com.cb.adventures.view.Sprite;

/**
 * 简单难度怪物工厂
 * Created by jenics on 2015/10/21.
 */
public class SimpleMonsterFactory implements IMonsterFactory {
    public SimpleMonsterFactory() {

    }
    @Override
    public Sprite create(int id) {
        Sprite sprite = null;
        if(id == GameConstants.BLACK_PIG_ID) {
            sprite = new Sprite(ImageLoader.getmInstance().loadBitmap(GameConstants.monsterNames[id]),1,2,4,4,5);
        }
        return sprite;
    }


}
