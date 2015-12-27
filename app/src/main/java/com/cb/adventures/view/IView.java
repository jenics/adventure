package com.cb.adventures.view;

import android.graphics.Canvas;

/**
 * Created by jenics on 2015/10/7.
 */
public interface IView extends IDrawable , IEntity{
    boolean isClickable();
    boolean isVisiable();
    void onClick();
}

