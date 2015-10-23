package com.cb.adventures.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;

/**
 * Created by jenics on 2015/10/22.
 */
public class ImageLoader {
    private static ImageLoader mInstance;
    private Context mContext;
    private ImageLoader() {

    }

    public void init(Context ctx) {
        mContext = ctx;
    }

    public synchronized static ImageLoader getmInstance() {
        if(mInstance == null) {
            mInstance = new ImageLoader();
        }
        return mInstance;
    }

    public Bitmap loadBitmap(String name) {
        if(mContext == null) {
            throw new IllegalStateException("ImageLoader,context is null!!!");
        }
        AssetManager am = mContext.getAssets();
        InputStream is = null;
        try {
            is = am.open(name);
        } catch (Exception e) {
            e.printStackTrace();
            CLog.e("error!", e.toString());
        }
        Bitmap bmpReturn = null;
        try {
            bmpReturn = BitmapFactory.decodeStream(is);
        }catch (Exception e){
            CLog.e("error",e.toString());
        }

        return bmpReturn;
    }
}
