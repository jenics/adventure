package com.cb.adventures.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.cb.adventures.state.BaseState;
import com.cb.adventures.state.IStateMgr;
import com.cb.adventures.state.spriteState.AttackState;
import com.cb.adventures.state.spriteState.MoveState;
import com.cb.adventures.state.spriteState.SpriteBaseState;
import com.cb.adventures.state.spriteState.StopState;

import java.util.HashMap;

/**
 * Created by jenics on 2015/10/7.
 */
public class Sprite extends BaseView implements IStateMgr {
    protected SpriteBaseState curState;
    protected HashMap<Integer, SpriteBaseState> stateHashMap;

    private int frameCount; ///一个方向的帧总数

    public int getFrameIndex() {
        return frameIndex;
    }

    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }

    private int frameIndex; ///当前帧数

    protected int perWidth;
    protected int perHeight;

    protected int attackPerWidth;
    protected int attackPerHeight;
    protected boolean isAttacking;          ///是否在攻击中
    protected boolean isNeedRepeatAttack;   ///是否需要重复攻击
    protected int attackFrameCount;

    private int direction;  ///当前方向

    //private boolean isStop = false;
    private long lastTime;

    public static final int DIRECTION_LEFT = 0;
    public static final int DIRECTION_RIGHT = 1;

    public static final int STATE_MOVE_LEFT = 0;
    public static final int STATE_MOVE_RIGHT = 1;
    public static final int STATE_STOP = 2;
    public static final int STATE_ATTACK_LEFT = 3;
    public static final int STATE_ATTACK_RIGHT = 4;

    private int leftRowIndex = 0;       ///方向左在第几行
    private int rightRowIndex = 1;      ///方向右在第几行

    private Bitmap bmp;
    private Bitmap accackBmp;

    public int getDirection() {
        return direction;
    }

    public void attack() {
        changeState(STATE_ATTACK_LEFT);
        if (isAttacking == false) {
            isAttacking = true;
            frameIndex = 0;
        } else {
            isNeedRepeatAttack = true;
        }
    }

    public void setDirection(int direction) {
        if (direction != this.direction) {
            this.direction = direction;
            if (direction == DIRECTION_LEFT) {
                changeState(STATE_MOVE_LEFT);
            } else if (direction == DIRECTION_RIGHT) {
                changeState(STATE_MOVE_RIGHT);
            }
        }
    }


    public Sprite() {
        frameIndex = 0;
        isAttacking = false;
        isNeedRepeatAttack = false;
        direction = DIRECTION_LEFT;
        stateHashMap = new HashMap<>();
    }

    public void nextFrame() {
        //if (isStop) {
        //    return;
        //}
        long nowTime = System.currentTimeMillis();
        if (nowTime - lastTime < 100)
            return;
        lastTime = nowTime;

        frameIndex++;
        if (frameIndex >= frameCount) {
            frameIndex = 1;
        }

    }

    public void move(int direction) {
        setDirection(direction);

        lastTime = System.currentTimeMillis();
        //isStop = false;
        if(direction == DIRECTION_LEFT) {
            changeState(STATE_MOVE_LEFT);
        }else {
            changeState(STATE_MOVE_RIGHT);
        }
    }

    public void stop() {
        //isStop = true;
        frameIndex = 0;
        changeState(STATE_STOP);
    }

    public void init(Bitmap bitmap, int frameCount, int perWidth, int perHeght, int leftRowIndex, int rightRowIndex
            , Bitmap attack, int attackWidth, int attackHeight, int attackCount) {
        direction = DIRECTION_LEFT;
        bmp = bitmap;

        this.frameCount = frameCount;
        this.perWidth = perWidth;
        this.perHeight = perHeght;
        this.leftRowIndex = leftRowIndex;
        this.rightRowIndex = rightRowIndex;

        accackBmp = attack;
        this.attackPerWidth = attackWidth;
        this.attackPerHeight = attackHeight;
        this.attackFrameCount = attackCount;


        frameIndex = 0;

        pt.x = 800;
        pt.y = 700;
    }

    @Override
    public void draw(Canvas canvas) {

        if(curState == null)
            return;

        super.draw(canvas);

        long nowTime = System.currentTimeMillis();
        if (nowTime - lastTime < 100) {
            curState.draw(canvas);
            return;
        }
        else {
            curState.nextFrame();
            curState.draw(canvas);
        }
        lastTime = nowTime;




        /*if (direction == DIRECTION_LEFT) {
            canvas.drawBitmap(bmp,
                    new Rect(   ///src rect
                            perWidth * frameIndex,
                            leftRowIndex * perHeight,
                            perWidth * frameIndex + perWidth,
                            leftRowIndex * perHeight + perHeight),
                    getRt(), null);
        } else {
            canvas.drawBitmap(bmp,
                    new Rect(   ///src rect
                            perWidth * frameIndex,
                            rightRowIndex * perHeight,
                            perWidth * frameIndex + perWidth,
                            rightRowIndex * perHeight + perHeight),
                    getRt(), null);
        }*/
    }

    @Override
    public boolean changeState(int stateId, boolean bForce) {
        SpriteBaseState disState = stateHashMap.get(stateId);
        if (curState == null || disState == null) {
            disState = (SpriteBaseState) createState(stateId);
            stateHashMap.put(stateId, disState);
            disState.entry();
            curState = disState;
            return true;
        } else {
            ///自己状态进入自己没有意义
            if (disState.getStateId() == curState.getStateId())
                return false;
            if (bForce) {
                curState.leave();
                disState.entry();
                curState = disState;
                return true;
            } else {
                ///攻击状态只能由自己停止
                if (curState.getStateId() == STATE_ATTACK_LEFT || curState.getStateId() == STATE_ATTACK_RIGHT) {
                    return false;
                }
                curState.leave();
                disState.entry();
                curState = disState;
                return true;
            }
        }
    }

    @Override
    public boolean changeState(int stateId) {
        return changeState(stateId, false);
    }

    @Override
    public BaseState createState(int stateId) {
        BaseState state = null;
        switch (stateId) {
            case STATE_MOVE_LEFT:
                state = new MoveState(stateId, this, DIRECTION_LEFT,frameCount,leftRowIndex,bmp,perWidth,perHeight);
                break;
            case STATE_MOVE_RIGHT:
                state = new MoveState(stateId, this, DIRECTION_RIGHT,frameCount,rightRowIndex,bmp,perWidth,perHeight);
                break;
            case STATE_ATTACK_LEFT:
                state = new AttackState(stateId, this, DIRECTION_LEFT,attackFrameCount,0,accackBmp,attackPerWidth,attackPerHeight);
                break;
            case STATE_ATTACK_RIGHT:
                state = new AttackState(stateId, this, DIRECTION_RIGHT,attackFrameCount,0,accackBmp,attackPerWidth,attackPerHeight);
                break;
            case STATE_STOP:
                state = new StopState(stateId, this,leftRowIndex,rightRowIndex,bmp,perWidth,perHeight);
                break;
        }
        return state;
    }
}
