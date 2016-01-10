package com.cb.adventures.view;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;

import com.cb.adventures.animation.IAnimation;
import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.data.DropItem;
import com.cb.adventures.data.GameData;
import com.cb.adventures.data.MapPropetry;
import com.cb.adventures.factory.IFactory;
import com.cb.adventures.utils.Randomer;
import com.cb.adventures.view.ui.InventoryView;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by chengbo01 on 2015/12/30.
 * email : jenics@live.com
 * 掉落物品管理器
 */
public class DropPropMgr implements IDrawable ,IFactory ,Map.MapObserver, IAnimation.OnAniamtionListener{
    private LinkedList<PropView> propViews;

    private final ReentrantReadWriteLock mReentrantReadWriteLock = new ReentrantReadWriteLock();
    private InventoryView inventoryView;



    public DropPropMgr(InventoryView inventoryView) {
        propViews = new LinkedList<>();
        this.inventoryView = inventoryView;
    }

    private PropView.PickUpPropListener mListener;

    public void setPickUpPropListener(PropView.PickUpPropListener l) {
        mListener = l;
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
        PropView propView = new PropView(GameData.getInstance().getProp(id));
        return propView;
    }

    /**
     * 掉落
     * @param pt 掉落坐标原点
     */
    private void drop(PointF pt,int id) {
        PropView propView = (PropView) create(id);
        propView.drop(pt);
        addDrop(propView);
    }

    /**
     * @param dropItems 掉落列表
     * @param pt 掉落坐标原点
     */
    public void drop(LinkedList<DropItem> dropItems, PointF pt) {
        if (dropItems == null) {
            return;
        }
        int random = Randomer.getInstance().getRandom(1000);
        int x = 0;
        for(DropItem dropItem : dropItems) {
            PointF pointF;
            if (x % 2 == 0) {
                pointF = new PointF(
                        pt.x - 50 * (x/2),
                        pt.y
                );
            } else {
                pointF = new PointF(
                        pt.x + 50 * (x/2),
                        pt.y
                );
            }
            if (random <= dropItem.getProbability() ) {
                Log.d("random :",String.format("%d",random));
                drop(pointF, dropItem.getItemId());
            }
            random = Randomer.getInstance().getRandom(1000);
            x ++;
        }
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
                if (propView.canPickedUp() && inventoryView.canPickUp(propView.getProp())) {
                    setPickUpPropListener(inventoryView);
                    propView.pickUp(pt, this);
                }
            }
        }
        mReentrantReadWriteLock.readLock().unlock();
    }

    @Override
    public void onAnimationEnd(BaseView view, boolean isForce) {
        if (view instanceof PropView) {
            PropView prop = (PropView) view;
            if (!prop.canPickedUp()) {
                if (mListener != null) {
                    mListener.onPickUpOver(prop.getProp());
                }
                removeDrop(prop);
            }
        }
    }

    @Override
    public void onAnimationBegin() {

    }

    @Override
    public void onNewGate(MapPropetry mapPropetry) {
        clearProps();
    }
}
