package com.cb.adventures.common;

import android.app.Application;
import android.content.Context;

/**
 * Created by AI on 2015/10/23.
 */
public class MyApplication extends Application {
    private static Context mContext ;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContextObj() {
        return mContext;
    }
}
