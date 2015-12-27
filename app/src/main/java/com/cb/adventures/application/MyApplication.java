package com.cb.adventures.application;

import android.app.Application;
import android.content.Context;

import com.cb.adventures.utils.CLog;
import com.cb.adventures.utils.FontFace;
import com.cb.adventures.utils.ImageLoader;

/**
 * Created by AI on 2015/10/23.
 */
public class MyApplication extends Application {
    private static Context mContext ;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        CLog.setLogEnable(true);
        ImageLoader.getmInstance().init(mContext);
        FontFace.getInstance().init(mContext);
    }

    public static Context getContextObj() {
        return mContext;
    }
}
