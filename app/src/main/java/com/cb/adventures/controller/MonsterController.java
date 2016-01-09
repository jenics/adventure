package com.cb.adventures.controller;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.data.MapPropetry;
import com.cb.adventures.factory.IFactory;
import com.cb.adventures.factory.SimpleMonsterFactory;
import com.cb.adventures.utils.Randomer;
import com.cb.adventures.view.BaseView;
import com.cb.adventures.view.IView;
import com.cb.adventures.view.Map;
import com.cb.adventures.view.Sprite;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by jenics on 2015/10/21.
 */
public class MonsterController implements Sprite.OnSpriteListener ,Map.MapObserver{
    private LinkedList<Sprite> mMonters;
    private IFactory mMonsterFactory;
    private final ReentrantReadWriteLock mReentrantReadWriteLock = new ReentrantReadWriteLock();
    public LinkedList<Sprite> getMonters() {
        return mMonters;
    }
    private Sprite.OnDeadListener mDeadListener;


    public void setDeadListener(Sprite.OnDeadListener mDeadListener) {
        this.mDeadListener = mDeadListener;
    }

    public void setMonsterFactory(IFactory monsterFactory) {
        this.mMonsterFactory = monsterFactory;
    }

    public ReentrantReadWriteLock getReentrantReadWriteLock() {
        return mReentrantReadWriteLock;
    }


    public MonsterController() {
        if(mMonters == null) {
            mMonters = new LinkedList<>();
        }
    }


    /**
     * 产生怪物
     * @param monsterId 怪物种类
     * @param num   要产生的数量
     */
    public void generateMonster(int monsterId,int rank,int num) {
        if(mMonsterFactory == null) {
            throw new IllegalStateException("mMonsterFactory is null");
        }
        mReentrantReadWriteLock.writeLock().lock();
        for(int i=0; i<num; i++) {
            Sprite sprite = (Sprite) mMonsterFactory.create(monsterId);
            sprite.caclBasePropetry(rank);
            sprite.setDeadListener(mDeadListener);
            if(sprite != null) {
                sprite.setSpriteListener(this);
                sprite.setPt(Randomer.getInstance().getRandom(GameConstants.sRightBoundary), GameConstants.sGameHeight*GameConstants.sYpointRatio);
                sprite.work(GameConstants.STATE_MOVE_LEFT,3000);
                mMonters.add(sprite);
            }
        }
        mReentrantReadWriteLock.writeLock().unlock();
    }

    /**
     * 清空所有怪物
     */
    public void clearMonster() {
        mReentrantReadWriteLock.writeLock().lock();
        for(Sprite sprite : mMonters) {
            sprite.onDestory();
        }
        mMonters.clear();
        mReentrantReadWriteLock.writeLock().unlock();
    }

    public void animate() {
        mReentrantReadWriteLock.readLock().lock();
        for(Sprite sprite : mMonters) {
            sprite.move();
        }
        mReentrantReadWriteLock.readLock().unlock();
    }

    /**
     * 画怪物
     * @param canvas 画布
     */
    public void draw(Canvas canvas) {
        mReentrantReadWriteLock.readLock().lock();
        for(IView view : mMonters) {
            view.draw(canvas);
        }
        mReentrantReadWriteLock.readLock().unlock();
    }

    @Override
    public void OnRestEnd(int id) {
        mReentrantReadWriteLock.readLock().lock();
        for (Sprite sprite : mMonters) {
            if (sprite.getId() == id && !sprite.isDead()) {
                sprite.work(sprite.getDirection(), Randomer.getInstance().getRandom(5, 10) * 1000);  ///跑2-3秒
                mReentrantReadWriteLock.readLock().unlock();
                return;
            }
        }
        mReentrantReadWriteLock.readLock().unlock();
    }

    @Override
    public void OnWorkEnd(int id) {
        mReentrantReadWriteLock.readLock().lock();
        for (Sprite sprite : mMonters) {
            if (sprite.getId() == id) {
                sprite.rest(Randomer.getInstance().getRandom(2)*1000);  ///休息1-2秒
                mReentrantReadWriteLock.readLock().unlock();
                return;
            }
        }
        mReentrantReadWriteLock.readLock().unlock();
    }

    @Override
    public void onScroll(int direction, float speed) {
        mReentrantReadWriteLock.readLock().lock();
        if (direction == GameConstants.DIRECT_LEFT) {
            for (BaseView view : mMonters) {
                PointF pt = view.getPt();
                pt.x += speed;
            }
        } else if (direction == GameConstants.DIRECT_RIGHT) {
            for (BaseView view : mMonters) {
                PointF pt = view.getPt();
                pt.x -= speed;
            }
        }
        mReentrantReadWriteLock.readLock().unlock();
    }

    @Override
    public void onNewGate(MapPropetry mapPropetry) {
        /**
         * 生成怪物
         */
        clearMonster();
        setMonsterFactory(new SimpleMonsterFactory());

        for (MapPropetry.MonsterPack pack : mapPropetry.getMonsterPaks()) {
            generateMonster(pack.getMonsterId(), pack.getMonsterRank(), pack.getMonsterNum());
        }
    }
}
