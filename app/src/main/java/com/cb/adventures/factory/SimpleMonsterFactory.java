package com.cb.adventures.factory;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.utils.ImageLoader;
import com.cb.adventures.view.Sprite;

import java.io.InputStream;

/**
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
