package com.cb.adventures.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cb.adventures.application.AdventureApplication;

import java.io.InputStream;
import java.util.HashMap;

/**
 * 图元载入器
 * Created by jenics on 2015/10/22.
 */
public class ImageLoader {
    private static ImageLoader mInstance;
    /**
     * 享元模式
     */
    private HashMap<String,Bitmap> mMap;
    private ImageLoader() {
        mMap = new HashMap<>();
    }

    public synchronized static ImageLoader getmInstance() {
        if(mInstance == null) {
            mInstance = new ImageLoader();
        }
        return mInstance;
    }

    /**
     * 自己回收bmp
     * @param name 资源名
     */
    public synchronized void recycleBitmap(String name) {
        Bitmap bitmap = mMap.remove(name);
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    public Bitmap loadBitmap(String name) {

        Bitmap bitmap = mMap.get(name);
        if(bitmap != null) {
            return bitmap;
        }

        AssetManager am = AdventureApplication.getContextObj().getAssets();
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
        mMap.put(name,bmpReturn);
        return bmpReturn;
    }
}
