package com.cb.adventures.data;

/**
 * Created by jenics on 2015/10/25.
 * 帧结构，描述该帧在大图中第几行，第几列的位置（基于0）
 */
public class Frame {
    private int row;
    private int col;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
