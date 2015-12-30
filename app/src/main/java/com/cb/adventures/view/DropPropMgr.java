package com.cb.adventures.view;

import android.graphics.Canvas;
import android.graphics.PointF;
import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.data.GameData;
import com.cb.adventures.data.PropPropetry;
import com.cb.adventures.factory.IFactory;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by chengbo01 on 2015/12/30.
 * email : jenics@live.com
 * 掉落物品管理器
 */
public class DropPropMgr implements IDrawable ,IFactory ,Map.MapScrollObserver , PropView.PickUpPropListener{
    private LinkedList<PropView> propViews;
    private static DropPropMgr mInstance;
    private final ReentrantReadWriteLock mReentrantReadWriteLock = new ReentrantReadWriteLock();

    public static synchronized DropPropMgr getInstance() {
        if (mInstance == null) {
            mInstance = new DropPropMgr();
        }
        return mInstance;
    }
    private DropPropMgr() {
        propViews = new LinkedList<>();
    }

    public void addDrop(PropView propView) {
        mReentrantReadWriteLock.writeLock().lock();
        propViews.add(propView);
        mReentrantReadWriteLock.writeLock().unlock();
    }

    public void removeDrop(PropView propView) {
        mReentrantReadWriteLock.writeLock().lock();
        propViews.remove(propView);
        mReentrantReadWriteLock.writeLock().unlock();
    }


    /**
     * 一般发生在下一关，没捡的，就都给丢弃掉
     */
    public void clearProps() {
        mReentrantReadWriteLock.writeLock().lock();
        propViews.clear();
        mReentrantReadWriteLock.writeLock().unlock();
    }

    @Override
    public void draw(Canvas canvas) {
        mReentrantReadWriteLock.readLock().lock();
        for (PropView propView : propViews) {
            propView.draw(canvas);
        }
        mReentrantReadWriteLock.readLock().unlock();
    }

    @Override
    public Object create(int id) {
        PropView propView = new PropView(GameData.getInstance().getConsumePropetry(id));
        return propView;
    }

    /**
     * 掉落
     * @param pt 掉落坐标原点
     */
    public void drop(PointF pt,int id) {
        PropView propView = (PropView) create(id);
        propView.drop(pt);
        addDrop(propView);
    }

    /**
     * 外面在发起这个捡起动作的时候，会做一系列检测，比如背包还有没有空位，能否叠加
     * @param pt 跟随这个坐标。
     */
    public void pickUp(PointF pt) {
        ///选择满足离跟随点一定距离的，然后发起拿起动作
        mReentrantReadWriteLock.readLock().lock();
        for (PropView propView : propViews) {
            if (pt.x >= propView.getPt().x- propView.getWidth()*1.0f/2 && pt.x <= propView.getPt().x+ propView.getWidth()*1.0f/2 ) {
                propView.setPickUpPropListener(this);
                propView.pickUp(pt);
            }
        }
        mReentrantReadWriteLock.readLock().unlock();
    }

    @Override
    public void onScroll(int direction, int speed) {
        mReentrantReadWriteLock.readLock().lock();
        if (direction == GameConstants.DIRECT_LEFT) {
            for (BaseView view : propViews) {
                PointF pt = view.getPt();
                pt.x += speed;
            }
        } else if (direction == GameConstants.DIRECT_RIGHT) {
            for (BaseView view : propViews) {
                PointF pt = view.getPt();
                pt.x -= speed;
            }
        }
        mReentrantReadWriteLock.readLock().unlock();
    }

    @Override
    public void onPickUpBegin(PropView prop) {

    }

    @Override
    public void onPickUpOver(PropView prop) {
        removeDrop(prop);
    }
}
