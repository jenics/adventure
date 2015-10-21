package com.cb.adventures.factory;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.view.Sprite;

import java.io.InputStream;

/**
 * Created by jenics on 2015/10/21.
 */
public class SimpleMonsterFactory implements IMonsterFactory {
    private Context mContext;
    public SimpleMonsterFactory(Context ctx) {
        mContext = ctx.getApplicationContext();
    }
    @Override
    public Sprite create(int id) {
        Sprite sprite = null;
        if(id == GameConstants.BLACK_PIG_ID) {
            sprite = new Sprite(loadBitmap(GameConstants.monsterNames[id]),1,2,4,4,5);
        }
        return sprite;
    }

    private Bitmap loadBitmap(String name) {
        AssetManager am = mContext.getAssets();
        InputStream is = null;
        try {
            is = am.open(name);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error!", e.toString());
        }
        Bitmap bmpReturn = null;
        try {
            bmpReturn = BitmapFactory.decodeStream(is);
        }catch (Exception e){
            Log.e("error",e.toString());
        }

        return bmpReturn;
    }
}
