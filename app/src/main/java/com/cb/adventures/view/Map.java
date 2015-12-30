package com.cb.adventures.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.animation.FrameAnimation;
import com.cb.adventures.animation.ScrollAnimation;
import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.controller.MonsterController;
import com.cb.adventures.data.GameData;
import com.cb.adventures.data.MapPropetry;
import com.cb.adventures.factory.SimpleMonsterFactory;
import com.cb.adventures.utils.CLog;
import com.cb.adventures.utils.ImageLoader;

import java.util.LinkedList;

/**
 * Created by jenics on 2015/10/7.
 */
public class Map extends BaseView {
    private Bitmap bmpTop;
    private Bitmap bmpBottom;

    private RectF rt1;
    private RectF rt2;

    private int mScreemWidth;
    private int mScreemHeight;
    private int mDirection;

    private int cursor;         ///地图游标
    private int mapWidth;
    private Player mPlayer;
    private MapPropetry mapPropetry;

    private FrameAnimation preGate;          ///上一关
    private FrameAnimation nextGate;         ///下一关

    public MapPropetry getMapPropetry() {
        return mapPropetry;
    }

    /**
     * 观察者模式，看谁都关心这个滚动事件，在地图滚动的时候，
     * 有些图元是需要跟着移动的，于是设置了这个观察者
     */
    private LinkedList<MapScrollObserver> mapScrollObservers;
    public interface MapScrollObserver {
        void onScroll(int direction,int speed);
    }

    private synchronized void notifyAll(int dir,int speed) {
        for (MapScrollObserver observer : mapScrollObservers) {
            observer.onScroll(dir,speed);
        }
    }

    public synchronized void addObserver(MapScrollObserver observer) {
        mapScrollObservers.add(observer);
    }

    public synchronized void removeObserver(MapScrollObserver observer) {
        mapScrollObservers.remove(observer);
    }

    public Map() {
        isClickable = false;
        rt1 = new RectF();
        rt2 = new RectF();
        mDirection = GameConstants.STATE_NONE;
        mapScrollObservers = new LinkedList<>();
    }

    /**
     * 获取上一关的View，用来判断玩家是否在该传送阵区域
     */
    public BaseView getPreGate() {
        return preGate;
    }

    /**
     * 获取下一关的View，用来判断玩家是否在该传送阵区域
     */
    public BaseView getNextGate() {
        return nextGate;
    }

    /**
     * @param mapId        初始关卡
     * @param screemWidth
     * @param screemHeight
     * @param player
     */
    public void init(int mapId, int screemWidth, int screemHeight, Player player) {
        mPlayer = player;

        this.mScreemWidth = screemWidth;
        this.mScreemHeight = screemHeight;

        nextGate(mapId);
    }

    public void nextGate() {
        nextGate(mapPropetry.getNextGate());
    }

    public void nextGate(int mapId) {
        if (!gotoGate(mapId))
            return;

        rt1.left = 0.0f;
        rt1.top = 0.0f;
        rt1.right = mScreemWidth;
        rt1.bottom = mScreemHeight * GameConstants.sBottomRatio;
        rt2.left = mScreemWidth;
        rt2.top = 0.0f;
        rt2.right = mScreemWidth + mScreemWidth;
        rt2.bottom = mScreemHeight * GameConstants.sBottomRatio;
        cursor = mScreemWidth / 2;


        if (getMapPropetry().getPreGate() != -1) {
            preGate = new FrameAnimation();
            preGate.setAnimationPropetry(GameData.getInstance().getAnimationPropetry(GameConstants.SKILL_ID_TRANSFER_MATRIX));
            preGate.setPt(1532 / 9 / 2 * GameConstants.zoomRatio, GameConstants.sGameHeight * (GameConstants.sYpointRatio-0.05f));
            preGate.startAnimation();
        }

        int x = (int) (1532 / 9 / 2 * GameConstants.zoomRatio);
        if (getMapPropetry().getNextGate() != -1) {
            nextGate = new FrameAnimation();
            nextGate.setAnimationPropetry(GameData.getInstance().getAnimationPropetry(GameConstants.SKILL_ID_TRANSFER_MATRIX));
            nextGate.setPt(mapWidth - x, GameConstants.sGameHeight * (GameConstants.sYpointRatio-0.05f));
            nextGate.startAnimation();
        }

        mPlayer.setPt(x, GameConstants.sGameHeight * GameConstants.sYpointRatio);
    }

    public void preGate() {
        preGate(mapPropetry.getPreGate());
    }

    public void preGate(int mapId) {
        if (!gotoGate(mapId))
            return;

        rt1.left = 0.0f;
        rt1.top = 0.0f;
        rt1.right = mScreemWidth;
        rt1.bottom = mScreemHeight * GameConstants.sBottomRatio;
        rt2.left = mScreemWidth;
        rt2.top = 0.0f;
        rt2.right = mScreemWidth + mScreemWidth;
        rt2.bottom = mScreemHeight * GameConstants.sBottomRatio;
        cursor = mapWidth - mScreemWidth / 2;

        int x = (int) (1532 / 9 / 2 * GameConstants.zoomRatio);
        if (getMapPropetry().getNextGate() != -1) {

            nextGate = new FrameAnimation();
            nextGate.setAnimationPropetry(GameData.getInstance().getAnimationPropetry(GameConstants.SKILL_ID_TRANSFER_MATRIX));
            nextGate.setPt(mScreemWidth - x, GameConstants.sGameHeight * (GameConstants.sYpointRatio-0.05f));
            nextGate.startAnimation();
        }

        int y = mapWidth - mScreemWidth;
        if (getMapPropetry().getPreGate() != -1) {
            preGate = new FrameAnimation();
            preGate.setAnimationPropetry(GameData.getInstance().getAnimationPropetry(GameConstants.SKILL_ID_TRANSFER_MATRIX));

            preGate.setPt(-y + x, GameConstants.sGameHeight * (GameConstants.sYpointRatio-0.05f));
            preGate.startAnimation();
        }

        mPlayer.setPt(mScreemWidth - x, GameConstants.sGameHeight * GameConstants.sYpointRatio);
    }

    private boolean gotoGate(int mapId) {
        if (nextGate != null) {
            nextGate.stopAnimation();
            nextGate = null;
        }

        if (preGate != null) {
            preGate.stopAnimation();
            preGate = null;
        }
        String curMapSrcName = null;
        if (mapPropetry != null) {
            curMapSrcName = mapPropetry.getSrcName();
        }

        mapPropetry = GameData.getInstance().getMapPropetry(mapId);
        if (mapPropetry == null) {
            CLog.d(getClass().getSimpleName(), String.format("error in gotoGate,the mapId is %d", mapId));
            return false;
        }

        this.bmpTop = ImageLoader.getInstance().loadBitmap(mapPropetry.getSrcName());
        this.bmpBottom = ImageLoader.getInstance().loadBitmap(GameConstants.MAP_BOTTOM_NAME);

        if (curMapSrcName != null) {
            ImageLoader.getInstance().recycleBitmap(curMapSrcName);
        }

        mapWidth = (int) (mScreemWidth * mapPropetry.getMapLenRatio());

        ScrollAnimation scrollAnimation = new ScrollAnimation();
        scrollAnimation.setPt(mScreemWidth / 2, mScreemHeight*0.25f);
        scrollAnimation.setAnimationPropetry(GameData.getInstance().getAnimationPropetry(GameConstants.SKILL_ID_AUTO_SCROLL));
        scrollAnimation.setmStrTitle(mapPropetry.getName());
        scrollAnimation.startAnimation();


        /**
         * 生成怪物
         */
        MonsterController.getInstance().clearMonster();
        MonsterController.getInstance().setmMonsterFactory(new SimpleMonsterFactory());
        MapPropetry mapPropetry = getMapPropetry();
        for (MapPropetry.MonsterPack pack : mapPropetry.getMonsterPaks()) {
            MonsterController.getInstance().generateMonster(pack.getMonsterId(), pack.getMonsterNum());
        }

        /**
         * 清空地上的物品
         */
        DropPropMgr.getInstance().clearProps();
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        //super.draw(canvas);
        scroll();
        canvas.drawBitmap(bmpTop,
                new Rect(   ///src rect
                        0,
                        0,
                        bmpTop.getWidth(),
                        bmpTop.getHeight()),
                rt1, null);

        canvas.drawBitmap(bmpTop,
                new Rect(   ///src rect
                        0,
                        0,
                        bmpTop.getWidth(),
                        bmpTop.getHeight()),
                rt2, null);

        canvas.drawBitmap(bmpBottom,
                new Rect(   ///src rect
                        0,
                        0,
                        bmpBottom.getWidth(),
                        bmpBottom.getHeight()),
                new RectF(rt1.left, rt1.bottom, rt1.right, mScreemHeight), null);

        canvas.drawBitmap(bmpBottom,
                new Rect(   ///src rect
                        0,
                        0,
                        bmpBottom.getWidth(),
                        bmpBottom.getHeight()),
                new RectF(rt2.left, rt2.bottom, rt2.right, mScreemHeight), null);

    }

    public void scrollTo(int direction) {
        mDirection = direction;
    }

    public void stopScroll() {
        mDirection = GameConstants.DIRECT_NONE;
    }

    public void scroll() {
        if (mDirection == GameConstants.DIRECT_RIGHT) {
            if (cursor >= mapWidth - mScreemWidth / 2) {
                PointF pointF = mPlayer.getPt();
                if (pointF.x <= mScreemWidth - mPlayer.getmPropetry().getSpeed()) {
                    pointF.x += mPlayer.getmPropetry().getSpeed();
                }
                return;
            } else {
                PointF pointF = mPlayer.getPt();
                if (pointF.x < mScreemWidth / 2) {
                    pointF.x += mPlayer.getmPropetry().getSpeed();
                    if (pointF.x > mScreemWidth / 2) {
                        pointF.x = mScreemWidth / 2;
                    }
                    return;
                }
            }
            cursor += mPlayer.getmPropetry().getSpeed();
            rt1.left -= mPlayer.getmPropetry().getSpeed();
            rt1.right -= mPlayer.getmPropetry().getSpeed();
            rt2.left -= mPlayer.getmPropetry().getSpeed();
            rt2.right -= mPlayer.getmPropetry().getSpeed();

            if (preGate != null) {
                PointF pt = preGate.getPt();
                pt.x -= mPlayer.getmPropetry().getSpeed();
            }
            if (nextGate != null) {
                PointF pt = nextGate.getPt();
                pt.x -= mPlayer.getmPropetry().getSpeed();
            }

            ///矫正地图位置
            if (rt1.left < -mScreemWidth) {
                rt1.left = mScreemWidth;
                rt1.right = mScreemWidth + mScreemWidth;
                rt2.left = 0;
                rt2.right = mScreemWidth;
            } else if (rt2.left < -mScreemWidth) {
                rt1.left = 0;
                rt1.right = mScreemWidth;
                rt2.left = mScreemWidth;
                rt2.right = mScreemWidth + mScreemWidth;
            }

            notifyAll(mDirection,mPlayer.getmPropetry().getSpeed());
        } else if (mDirection == GameConstants.DIRECT_LEFT) {
            if (cursor <= mScreemWidth / 2) {
                PointF pointF = mPlayer.getPt();
                if (pointF.x >= mPlayer.getmPropetry().getSpeed()) {
                    pointF.x -= mPlayer.getmPropetry().getSpeed();
                }
                return;
            } else {
                PointF pointF = mPlayer.getPt();
                if (pointF.x > mScreemWidth / 2) {
                    pointF.x -= mPlayer.getmPropetry().getSpeed();
                    if (pointF.x < mScreemWidth / 2) {
                        pointF.x = mScreemWidth / 2;
                    }
                    return;
                }
            }
            cursor -= mPlayer.getmPropetry().getSpeed();
            rt1.left += mPlayer.getmPropetry().getSpeed();
            rt1.right += mPlayer.getmPropetry().getSpeed();
            rt2.left += mPlayer.getmPropetry().getSpeed();
            rt2.right += mPlayer.getmPropetry().getSpeed();

            ///移动关卡
            if (preGate != null) {
                PointF pt = preGate.getPt();
                pt.x += mPlayer.getmPropetry().getSpeed();
            }
            if (nextGate != null) {
                PointF pt = nextGate.getPt();
                pt.x += mPlayer.getmPropetry().getSpeed();
            }

            ///矫正地图位置
            if (rt1.left > mScreemWidth) {
                rt1.left = -mScreemWidth;
                rt1.right = 0;
                rt2.left = 0;
                rt2.right = mScreemWidth;
            } else if (rt2.left > mScreemWidth) {
                rt1.left = -mScreemWidth;
                rt1.right = 0;
                rt2.left = 0;
                rt2.right = mScreemWidth;
            }

            notifyAll(mDirection,mPlayer.getmPropetry().getSpeed());
        }
    }
}
