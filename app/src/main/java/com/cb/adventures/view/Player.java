package com.cb.adventures.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.state.BaseState;
import com.cb.adventures.state.IStateMgr;
import com.cb.adventures.state.playerstate.AttackState;
import com.cb.adventures.state.playerstate.MoveState;
import com.cb.adventures.state.playerstate.PlayerBaseState;
import com.cb.adventures.state.playerstate.StopState;

import java.util.HashMap;

/**
 * Created by jenics on 2015/10/21.
 */
public class Player extends BaseView implements IStateMgr {
    protected PlayerBaseState curState;
    protected HashMap<Integer, PlayerBaseState> stateHashMap;
    protected int frameCount; ///一个方向的帧总数
    protected int frameIndex; ///当前帧数
    protected int perWidth; ///每一帧的宽度
    protected int perHeight;    ///每一帧的高度

    protected int attackPerWidth;           ///每一个攻击帧宽度
    protected int attackPerHeight;          ///每一个攻击帧的高度

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setIsAttacking(boolean isAttacking) {
        this.isAttacking = isAttacking;
    }

    protected boolean isAttacking;          ///是否在攻击中
    protected boolean isNeedRepeatAttack;   ///是否需要重复攻击
    protected int attackFrameCount;         ///攻击总帧数
    protected int mDirection;  ///当前方向
    private long lastTime;
    private int leftRowIndex = 0;       ///方向左在第几行
    private int rightRowIndex = 1;      ///方向右在第几行

    private Bitmap bmp;
    private Bitmap accackBmp;

    public Player() {
        frameIndex = 0;
        isAttacking = false;
        isNeedRepeatAttack = false;
        stateHashMap = new HashMap<>();
        mDirection = GameConstants.STATE_MOVE_RIGHT;
    }

    public int getDirection() {
        return mDirection;
    }

    public void attack() {
        if (isAttacking == false) {
            changeState(GameConstants.STATE_ATTACK_LEFT);
            isAttacking = true;
            frameIndex = 0;
        } else {
            isNeedRepeatAttack = true;
        }
    }



    /**
     * 是否成功移动
     * @param direction
     * @return
     */
    public boolean move(int direction) {
        lastTime = System.currentTimeMillis();
        if(isAttacking) {
            return false;
        }
        if(direction == GameConstants.STATE_MOVE_LEFT
                || direction == GameConstants.STATE_MOVE_RIGHT) {
            if(curState.getStateId() == direction) {
                return true;
            }
            mDirection = direction;
            return changeState(direction);
        }
        return false;
    }

    public void stop() {
        frameIndex = 0;
        changeState(GameConstants.STATE_STOP);
    }

    public void init(Bitmap bitmap, int frameCount, int perWidth, int perHeght, int leftRowIndex, int rightRowIndex
            , Bitmap attack, int attackWidth, int attackHeight, int attackCount) {
        mDirection = GameConstants.STATE_MOVE_RIGHT;
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

        stop();
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
    }

    @Override
    public boolean changeState(int stateId, boolean bForce) {
        PlayerBaseState disState = stateHashMap.get(stateId);
        if (curState == null || disState == null) {
            disState = (PlayerBaseState) createState(stateId);
            stateHashMap.put(stateId, disState);
            disState.entry();
            curState = disState;
            return true;
        } else {
            ///自己状态进入自己没有意义
            if (disState.getStateId() == curState.getStateId())
                return true;        ///还在该状态，也是说明进入状态成功
            if (bForce) {
                curState.leave();
                disState.entry();
                curState = disState;
                return true;
            } else {
                ///攻击状态只能由自己停止
                if (curState.getStateId() == GameConstants.STATE_ATTACK_LEFT
                        || curState.getStateId() == GameConstants.STATE_ATTACK_RIGHT) {
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
            case GameConstants.STATE_MOVE_LEFT:
                state = new MoveState(stateId, this,frameCount,leftRowIndex,bmp,perWidth,perHeight);
                break;
            case GameConstants.STATE_MOVE_RIGHT:
                state = new MoveState(stateId, this,frameCount,rightRowIndex,bmp,perWidth,perHeight);
                break;
            case GameConstants.STATE_ATTACK_LEFT:
                state = new AttackState(stateId, this,attackFrameCount,0,accackBmp,attackPerWidth,attackPerHeight);
                break;
            case GameConstants.STATE_ATTACK_RIGHT:
                state = new AttackState(stateId, this,attackFrameCount,0,accackBmp,attackPerWidth,attackPerHeight);
                break;
            case GameConstants.STATE_STOP:
                state = new StopState(stateId, this,leftRowIndex,rightRowIndex,bmp,perWidth,perHeight);
                break;
        }
        return state;
    }

}
