package com.cb.adventures.view;

/**
 * Created by jenics on 2015/12/20.
 * 控件接口
 */
public interface IControl {
    boolean onMouseDown(int x, int y);
    boolean onMouseMove(int x,int y);
    boolean onMouseUp(int x,int y);
}
