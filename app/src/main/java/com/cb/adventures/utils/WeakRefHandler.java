package com.cb.adventures.utils;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by chengbo01 on 2015/12/29.
 * 一个handler类，防止泄漏。使用弱引用
 * email : jenics@live.com
 */

public class WeakRefHandler<T> extends Handler {
    private WeakReference<T> mReference;
    public WeakRefHandler(T t) {
        mReference = new WeakReference<T>(t);
    }

    public  WeakReference<T> getReference() {
        return mReference;
    }
}
