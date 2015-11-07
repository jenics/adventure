package com.cb.adventures.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.Image;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.controller.MonsterController;
import com.cb.adventures.data.MapPropetry;
import com.cb.adventures.utils.ImageLoader;

/**
 * Created by jenics on 2015/10/7.
 */
public class ScrollBackground extends BaseView{
    private Bitmap bmp1;
    private Bitmap bmp2;

    private RectF rt1;
    private RectF rt2;

    private int screemWidth;
    private int mDirection;

    private int cursor;         ///地图游标
    private float mapLenRatio;
    private int mapWidth;
    private Player mPlayer;
    private MapPropetry mapPropetry;

    public MapPropetry getMapPropetry() {
        return mapPropetry;
    }

    public ScrollBackground(){
        isClickable = false;
        rt1 = new RectF();
        rt2 = new RectF();
        mDirection = GameConstants.STATE_NONE;
    }

    public void init(MapPropetry propetry,int screemWidth,int screemHeight,Player player){
        mPlayer = player;
        mapPropetry = propetry;
        this.bmp1 = ImageLoader.getmInstance().loadBitmap(mapPropetry.getSrcName());
        this.bmp2 = ImageLoader.getmInstance().loadBitmap(mapPropetry.getSrcName());
        this.screemWidth = screemWidth;
        rt1.left = 0.0f;
        rt1.top = 0.0f;
        rt1.right = screemWidth;
        rt1.bottom = screemHeight;
        rt2.left = screemWidth;
        rt2.top = 0.0f;
        rt2.right = screemWidth+screemWidth;
        rt2.bottom = screemHeight;
        cursor = screemWidth/2;
        mapWidth = (int) (screemWidth*1.5f);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        scroll();
        canvas.drawBitmap(bmp1,
                new Rect(   ///src rect
                        0,
                        0,
                        bmp1.getWidth(),
                        bmp1.getHeight()),
                rt1, null);

        canvas.drawBitmap(bmp2,
                new Rect(   ///src rect
                        0,
                        0,
                        bmp2.getWidth(),
                        bmp2.getHeight()),
                rt2, null);

    }

    public void scrollTo(int direction){
        mDirection = direction;
    }

    public void stopScroll() {
        mDirection = GameConstants.DIRECT_NONE;
    }

    private void scroll() {
        if(mDirection == GameConstants.DIRECT_RIGHT) {
            if(cursor >= mapWidth -screemWidth/2) {
                PointF pointF = mPlayer.getPt();
                if (pointF.x <= screemWidth-mPlayer.getmPropetry().getSpeed()) {
                    pointF.x += mPlayer.getmPropetry().getSpeed();
                }
                return;
            }else {
                PointF pointF = mPlayer.getPt();
                if (pointF.x < screemWidth/2) {
                    pointF.x += mPlayer.getmPropetry().getSpeed();
                    if(pointF.x > screemWidth/2) {
                        pointF.x = screemWidth/2;
                    }
                    return;
                }
            }
            cursor += mPlayer.getmPropetry().getSpeed();
            rt1.left -= mPlayer.getmPropetry().getSpeed();
            rt1.right -= mPlayer.getmPropetry().getSpeed();
            rt2.left -= mPlayer.getmPropetry().getSpeed();
            rt2.right -= mPlayer.getmPropetry().getSpeed();

            for(BaseView view :
                MonsterController.getInstance().getMonters()) {
                PointF pt = view.getPt();
                pt.x -= mPlayer.getmPropetry().getSpeed();
            }




            if (rt1.left < -screemWidth) {
                rt1.left = screemWidth;
                rt1.right = screemWidth + screemWidth;
                rt2.left = 0;
                rt2.right = screemWidth;
            } else if (rt2.left < -screemWidth) {
                rt1.left = 0;
                rt1.right = screemWidth;
                rt2.left = screemWidth;
                rt2.right = screemWidth + screemWidth;
            }
        }else if(mDirection == GameConstants.DIRECT_LEFT){
            if(cursor <= screemWidth/2) {
                PointF pointF = mPlayer.getPt();
                if (pointF.x >= mPlayer.getmPropetry().getSpeed()) {
                   pointF.x -= mPlayer.getmPropetry().getSpeed();
                }
                return;
            }else {
                PointF pointF = mPlayer.getPt();
                if (pointF.x > screemWidth/2) {
                    pointF.x -= mPlayer.getmPropetry().getSpeed();
                    if(pointF.x < screemWidth/2) {
                        pointF.x = screemWidth/2;
                    }
                    return;
                }
            }
            cursor -= mPlayer.getmPropetry().getSpeed();
            rt1.left += mPlayer.getmPropetry().getSpeed();
            rt1.right += mPlayer.getmPropetry().getSpeed();
            rt2.left += mPlayer.getmPropetry().getSpeed();
            rt2.right += mPlayer.getmPropetry().getSpeed();

            for(BaseView view :
                    MonsterController.getInstance().getMonters()) {
                PointF pt = view.getPt();
                pt.x += mPlayer.getmPropetry().getSpeed();
            }

            if (rt1.left > screemWidth) {
                rt1.left = -screemWidth;
                rt1.right = 0;
                rt2.left = 0;
                rt2.right = screemWidth;
            } else if (rt2.left > screemWidth) {
                rt1.left = -screemWidth;
                rt1.right = 0;
                rt2.left = 0;
                rt2.right = screemWidth;
            }
        }
    }
}
