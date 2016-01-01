package com.cb.adventures.utils;

import java.util.Random;

/**
 * Created by jenics on 2016/1/1.
 * 随机数产生器
 */
public class Randomer {
    private static Randomer mInstance;
    private Random mRandom;
    public static synchronized Randomer getInstance() {
        if (mInstance == null) {
            mInstance = new Randomer();
        }
        return mInstance;
    }
    private Randomer() {

    }


    /**
     * 取得[1,range]随机数
     * @param range
     * @return
     */
    public int getRandom(int range) {
        if(mRandom == null) {
            mRandom = new Random(System.currentTimeMillis());
        }
        return (Math.abs(mRandom.nextInt()%range) + 1);
    }

    /**
     * 获取[left,right]随机数
     * @param left
     * @param right
     * @return
     */
    public int getRandom(int left,int right) {
        if(mRandom == null) {
            mRandom = new Random(System.currentTimeMillis());
        }
        int random = (Math.abs(mRandom.nextInt()%(right-left+1)) + left);
        return random;
    }
}
