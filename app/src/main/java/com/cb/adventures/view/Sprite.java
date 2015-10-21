package com.cb.adventures.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.constants.GameConstants;

/**
 * Created by jenics on 2015/10/7.
 * Sprite类最简单，只管理怪物左右跑。
 */
public class Sprite extends BaseView{

    public interface OnSpriteListener {
        public void OnRestEnd(int id);
        public void OnWorkEnd(int id);
    }

    public void setmSpriteListener(OnSpriteListener mSpriteListener) {
        this.mSpriteListener = mSpriteListener;
    }

    protected OnSpriteListener mSpriteListener;

    protected Bitmap mBitmap;
    protected int mDirection;
    private static int sIdNum = 0;

    public int getId() {
        return mId;
    }

    protected int mId;          ///该怪物唯一标识符，自动生成，自动递增

    protected int mPerWidth;    ///每一帧宽
    protected int mPerHeight;   ///每一帧高
    protected int mFrameIndex;  ///当前帧
    protected int mLeftRowIndex;    ///左方向是第几行
    protected int mRightRowIndex;   ///右方向是第几行
    protected int mFrameCount;      ///横向帧总数

    protected long lastTime;        ///时间间隔控制帧的切换

    protected boolean mIsRest;      ///是否休息
    protected boolean mIsStop;      ///是否停止，和休息有区别

    protected int mFrameInterval;        ///切换帧的时间间隔

    protected int mMoveStep;         ///移动时的步长

    protected long restBeginTime;       ///开始休息时间
    protected long workBeginTime;       ///干活开始时间
    protected long mRestTime;           ///休息时间
    protected long mWorkTime;           ///工作时间

    public Sprite(Bitmap bitmap,int leftRowIndex,int rightRowIndex,
                  int frameCount,int rowCount,int moveStep ,int frameInterval) {
        Sprite.sIdNum++;
        mId = sIdNum;
        mBitmap = bitmap;
        mFrameInterval = frameInterval;            ///100ms换一帧
        mPerWidth = bitmap.getWidth()/9;
        mPerHeight = bitmap.getHeight()/rowCount;
        mLeftRowIndex = leftRowIndex;
        mRightRowIndex = rightRowIndex;
        mFrameCount = frameCount;
        mMoveStep = moveStep;
        mIsStop = false;
    }

    public Sprite(Bitmap bitmap,int leftRowIndex,int rightRowIndex,
                  int frameCount,int rowCount,int moveStep) {
        Sprite.sIdNum++;
        mId = sIdNum;
        mBitmap = bitmap;
        mFrameInterval = 100;            ///100ms换一帧
        mPerWidth = bitmap.getWidth()/9;
        mPerHeight = bitmap.getHeight()/rowCount;
        mLeftRowIndex = leftRowIndex;
        mRightRowIndex = rightRowIndex;
        mFrameCount = frameCount;
        mMoveStep = moveStep;
        mIsStop = false;
    }

    @Override
    public Sprite clone() {
        try {
            return (Sprite) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("Clone failed.");
            return null;
        }
    }

    public long getRestBeginTime() {
        return restBeginTime;
    }

    public long getWorkBeginTime() {
        return workBeginTime;
    }

    /**
     * 休息
     */
    public void rest(long time) {
        mIsRest = true;
        mIsStop = false;
        mDirection = GameConstants.DIRECTION_NONE;
        mFrameIndex = 0;
        restBeginTime = System.currentTimeMillis();
        mRestTime = time;
    }

    /**
     * 干活
     */
    public void work(int direction,long time) {
        if(direction == GameConstants.DIRECTION_NONE) {
            //rest();
        }else {
            mIsStop = false;
            mIsRest = false;
            mDirection = direction;
            mFrameIndex = 0;
            workBeginTime = System.currentTimeMillis();
            mWorkTime = time;
        }
    }

    private void nextFrame() {
        if(!mIsRest && !mIsStop) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - lastTime > mFrameInterval) {
                mFrameIndex++;
                if (mFrameIndex >= mFrameCount) {
                    mFrameIndex = 0;
                }
            }
        }
    }


    public void move() {
        if (mIsStop) {
            return;
        }
        if(!mIsRest){
            if(mDirection == GameConstants.DIRECTION_LEFT) {
                pt.x -= mMoveStep;
                if(pt.x <= GameConstants.sLeftBoundary) {
                    mDirection = GameConstants.DIRECTION_RIGHT;
                }
                long nowTime = System.currentTimeMillis();
                if (nowTime - workBeginTime >= mWorkTime) {
                    stop();
                    workEnd();
                }
            }else if(mDirection == GameConstants.DIRECTION_RIGHT) {
                pt.x += mMoveStep;
                if(pt.x >= GameConstants.sRightBoundary) {
                    mDirection = GameConstants.DIRECTION_LEFT;
                }
                long nowTime = System.currentTimeMillis();
                if (nowTime - workBeginTime >= mWorkTime) {
                    stop();
                    workEnd();
                }
            }
        }else {
            long nowTime = System.currentTimeMillis();
            if (nowTime - restBeginTime >= mRestTime) {
                stop();
                restEnd();
            }
        }
    }

    private void stop() {
        mIsStop = true;
        mFrameIndex = 0;
    }

    private void restEnd() {
        if(mSpriteListener != null) {
            mSpriteListener.OnRestEnd(mId);
        }
    }

    private void workEnd() {
        if(mSpriteListener != null) {
            mSpriteListener.OnWorkEnd(mId);
        }
    }

    public void draw(Canvas canvas) {
        nextFrame();

        int rowIndex = mDirection == GameConstants.DIRECTION_LEFT ? mLeftRowIndex : mRightRowIndex;

        float x = getPt().x - mPerWidth/2;
        float y = getPt().y - mPerHeight/2;

        canvas.drawBitmap(mBitmap,
                new Rect(   ///src rect
                        mPerWidth * mFrameIndex,
                        rowIndex * mPerHeight,
                        mPerWidth * mFrameIndex + mPerWidth,
                        rowIndex * mPerHeight + mPerHeight),
                new RectF(x,
                        y,
                        x + mPerWidth,
                        y + mPerHeight), null);
    }
}
