package com.cb.adventures.controller;

import android.graphics.Canvas;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.factory.IMonsterFactory;
import com.cb.adventures.view.IView;
import com.cb.adventures.view.Sprite;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created by jenics on 2015/10/21.
 */
public class MonsterController implements Sprite.OnSpriteListener {

    private static MonsterController mInstance;
    private LinkedList<Sprite> mMonters;
    private IMonsterFactory mMonsterFactory;

    public void setmMonsterFactory(IMonsterFactory monsterFactory) {
        this.mMonsterFactory = monsterFactory;
    }


    private MonsterController() {
        if(mMonters == null) {
            mMonters = new LinkedList<>();
        }
    }

    public static synchronized MonsterController getInstance() {
        if(mInstance == null){
            mInstance = new MonsterController();
        }
        return mInstance;
    }

    /**
     * 产生怪物
     * @param monsterId 怪物种类
     * @param num   要产生的数量
     */
    public synchronized void generateMonster(int monsterId,int num) {
        if(mMonsterFactory == null) {
            throw new IllegalStateException("mMonsterFactory is null");
        }
        for(int i=0; i<num; i++) {
            Sprite sprite = mMonsterFactory.create(monsterId);
            if(sprite != null) {
                sprite.setmSpriteListener(this);
                sprite.setPt(getRandom(GameConstants.sRightBoundary), 700);
                sprite.work(GameConstants.DIRECTION_LEFT,3000);
                mMonters.add(sprite);
            }
        }
    }

    /**
     * 取得[1,range]随机数
     * @param range
     * @return
     */
    private int getRandom(int range) {
        Random rdm = new Random(System.currentTimeMillis());
        return rdm.nextInt()%range + 1;
    }

    /**
     * 获取[left,right]随机数
     * @param left
     * @param right
     * @return
     */
    private int getRandom(int left,int right) {
        Random rdm = new Random(System.currentTimeMillis());
        return rdm.nextInt()%(right-left+1) + left;
    }

    public synchronized void animate() {
        for(Sprite sprite : mMonters) {
            sprite.move();
        }
    }

    /**
     * 画怪物
     * @param canvas 画布
     */
    public synchronized void draw(Canvas canvas) {
        for(IView view : mMonters) {
            view.draw(canvas);
        }
    }

    @Override
    public void OnRestEnd(int id) {
        for (Sprite sprite : mMonters) {
            if (sprite.getId() == id) {
                sprite.work(GameConstants.DIRECTION_LEFT,getRandom(5,10)*1000);  ///跑2-3秒
                return;
            }
        }
    }

    @Override
    public void OnWorkEnd(int id) {
        for (Sprite sprite : mMonters) {
            if (sprite.getId() == id) {
                sprite.rest(getRandom(2)*1000);  ///休息1-2秒
                return;
            }
        }
    }
}
