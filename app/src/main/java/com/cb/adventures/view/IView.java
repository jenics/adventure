package com.cb.adventures.view;

import android.graphics.Canvas;

/**
 * Created by jenics on 2015/10/7.
 */
public interface IView {
    public boolean isClickable();

    public boolean isVisiable();

    public void onClick();

    public void draw(Canvas canvas);
}

