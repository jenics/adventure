package com.cb.adventures.controller;

import android.graphics.Canvas;
import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.factory.IFactory;
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
    private IFactory mMonsterFactory;

    public LinkedList<Sprite> getMonters() {
        return mMonters;
    }

    public void setmMonsterFactory(IFactory monsterFactory) {
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
            Sprite sprite = (Sprite) mMonsterFactory.create(monsterId);
            if(sprite != null) {
                sprite.setmSpriteListener(this);
                sprite.setPt(getRandom(GameConstants.sRightBoundary), GameConstants.sGameHeight*GameConstants.sYpointRatio);
                sprite.work(GameConstants.STATE_MOVE_LEFT,3000);
                mMonters.add(sprite);
            }
        }
    }

    private Random mRandom;
    /**
     * 取得[1,range]随机数
     * @param range
     * @return
     */
    private int getRandom(int range) {
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
    private int getRandom(int left,int right) {
        if(mRandom == null) {
            mRandom = new Random(System.currentTimeMillis());
        }
        int random = (Math.abs(mRandom.nextInt()%(right-left+1)) + left);
        return random;
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
                sprite.work(GameConstants.STATE_MOVE_LEFT,getRandom(5,10)*1000);  ///跑2-3秒
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
