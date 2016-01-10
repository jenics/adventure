package com.cb.adventures.application;

import android.app.Application;
import android.content.Context;

import com.cb.adventures.engine.GameEngine;
import com.cb.adventures.utils.CLog;

/**
 * Created by AI on 2015/10/23.
 * 应用程序类
 */
public class AdventureApplication extends Application {
    private static Context mContext ;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        CLog.setLogEnable(true);
    }

    public static Context getContextObj() {
        return mContext;
    }
}
