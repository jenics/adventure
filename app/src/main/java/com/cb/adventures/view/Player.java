package com.cb.adventures.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
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
    protected boolean isAttacking;          ///是否在攻击中
    protected boolean isNeedRepeatAttack;   ///是否需要重复攻击
    protected int attackFrameCount;         ///攻击总帧数
    protected int direction;  ///当前方向

    private long lastTime;

    public static final int STATE_MOVE_LEFT = 0;
    public static final int STATE_MOVE_RIGHT = 1;
    public static final int STATE_STOP = 2;
    public static final int STATE_ATTACK_LEFT = 3;
    public static final int STATE_ATTACK_RIGHT = 4;

    private int leftRowIndex = 0;       ///方向左在第几行
    private int rightRowIndex = 1;      ///方向右在第几行

    private Bitmap bmp;
    private Bitmap accackBmp;

    public Player() {
        frameIndex = 0;
        isAttacking = false;
        isNeedRepeatAttack = false;
        direction = GameConstants.DIRECTION_LEFT;
        stateHashMap = new HashMap<>();
    }

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
            if (direction == GameConstants.DIRECTION_LEFT) {
                changeState(STATE_MOVE_LEFT);
            } else if (direction == GameConstants.DIRECTION_RIGHT) {
                changeState(STATE_MOVE_RIGHT);
            }
        }
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
        if(direction == GameConstants.DIRECTION_LEFT) {
            changeState(STATE_MOVE_LEFT);
        }else {
            changeState(STATE_MOVE_RIGHT);
        }
    }

    public void stop() {
        frameIndex = 0;
        changeState(STATE_STOP);
    }

    public void init(Bitmap bitmap, int frameCount, int perWidth, int perHeght, int leftRowIndex, int rightRowIndex
            , Bitmap attack, int attackWidth, int attackHeight, int attackCount) {
        direction = GameConstants.DIRECTION_LEFT;
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
                state = new MoveState(stateId, this, GameConstants.DIRECTION_LEFT,frameCount,leftRowIndex,bmp,perWidth,perHeight);
                break;
            case STATE_MOVE_RIGHT:
                state = new MoveState(stateId, this, GameConstants.DIRECTION_RIGHT,frameCount,rightRowIndex,bmp,perWidth,perHeight);
                break;
            case STATE_ATTACK_LEFT:
                state = new AttackState(stateId, this, GameConstants.DIRECTION_LEFT,attackFrameCount,0,accackBmp,attackPerWidth,attackPerHeight);
                break;
            case STATE_ATTACK_RIGHT:
                state = new AttackState(stateId, this, GameConstants.DIRECTION_RIGHT,attackFrameCount,0,accackBmp,attackPerWidth,attackPerHeight);
                break;
            case STATE_STOP:
                state = new StopState(stateId, this,leftRowIndex,rightRowIndex,bmp,perWidth,perHeight);
                break;
        }
        return state;
    }

}
