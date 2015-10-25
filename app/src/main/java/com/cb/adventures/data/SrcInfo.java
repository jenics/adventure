package com.cb.adventures.data;

/**
 * 图片资源的信息
 * Created by jenics on 2015/10/25.
 */
public class SrcInfo {
    /**
     * 资源的名字
     */
    private String srcName;
    /**
     * 行帧总数
     */
    private int rowFramCount;
    /**
     * 列帧总数
     */
    private int colFramCont;

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public int getRowFramCount() {
        return rowFramCount;
    }

    public void setRowFramCount(int rowFramCount) {
        this.rowFramCount = rowFramCount;
    }

    public int getColFramCont() {
        return colFramCont;
    }

    public void setColFramCont(int colFramCont) {
        this.colFramCont = colFramCont;
    }
}
