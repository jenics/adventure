package com.cb.adventures.view;

import android.graphics.Canvas;
import android.graphics.Path;
import android.util.Log;
import android.view.View;

import com.cb.adventures.constants.GameConstants;

/**
 * Created by jenics on 2015/10/22.
 */
public class GameController implements IView {
    private DirectionController mDirectionController;
    private DirectionCenterCircle mDirectionCenterCircle;
    private AttackController mAttackController;

    public GameController() {
    }


    public void setmOnControllerListener(OnControllerListener mOnControllerListener) {
        this.mOnControllerListener = mOnControllerListener;
    }

    private OnControllerListener mOnControllerListener;
    @Override
    public boolean isClickable() {
        return true;
    }

    @Override
    public boolean isVisiable() {
        return true;
    }

    @Override
    public void onClick() {

    }

    public interface OnControllerListener {
        void onDirectionChange(int direction);
        void onAttack();
        void onStop();
    }

    private boolean isDirectionControllerDown;
    public void onMouseDown(int x, int y) {
        int state = onMouseEvent(x,y);

        if(GameConstants.isAttack(state)) {
            mOnControllerListener.onAttack();
        }else if(GameConstants.isMove(state)) {
            mOnControllerListener.onDirectionChange(GameConstants.getDirection(state));
        }
    }

    public void onMouseMove(int x,int y) {
        int state = onMouseEvent(x,y);
        if(GameConstants.isMove(state)) {
            mOnControllerListener.onDirectionChange(state);
        }
    }

    public void onMouseUp(int x,int y) {
        resetCenterCircle();
        if(isDirectionControllerDown) {
            mOnControllerListener.onStop();
        }
        isDirectionControllerDown = false;
    }

    public void resetCenterCircle() {
        mDirectionCenterCircle.reset();
    }

    /**
     * 返回GameConstants指定的方向，如果没有命中，返回DIRECTION_NONE
     * @param x x坐标
     * @param y y坐标
     */
    public int onMouseEvent(int x ,int y) {
        if((x >= (mDirectionController.getPt().x))
                &&  (x <=  (mDirectionController.getPt().x + mDirectionController.getWidth()/2))
                && y >= mDirectionController.getPt().y - mDirectionController.getHeight()/2
                && y <= mDirectionController.getPt().y + mDirectionController.getHeight()/2) {
            mDirectionCenterCircle.setPt(x,y);
            isDirectionControllerDown = true;
            return GameConstants.STATE_MOVE_RIGHT;
        }else if(x >= mDirectionController.getPt().x - mDirectionController.getWidth()/2
                && x <  mDirectionController.getPt().x
                && y >= mDirectionController.getPt().y - mDirectionController.getHeight()/2
                && y <= mDirectionController.getPt().y + mDirectionController.getHeight()/2) {
            mDirectionCenterCircle.setPt(x,y);
            isDirectionControllerDown = true;
            return GameConstants.STATE_MOVE_LEFT;
        }else if(x >= mAttackController.getPt().x-mAttackController.getWidth()/2
                && x <= mAttackController.getPt().x + mAttackController.getWidth()/2
                && y >= mAttackController.getPt().y - mAttackController.getHeight()/2
                && y <= mAttackController.getPt().y + mAttackController.getHeight()/2) {
            return GameConstants.STATE_ATTACK_LEFT;
        }
        else {
            return GameConstants.STATE_NONE;
        }
    }

    private boolean ptInRegion(int x ,int y) {
        if(x >= mDirectionController.getPt().x-mDirectionController.getWidth()/2
                && x <= mDirectionController.getPt().x+mDirectionController.getWidth()/2
                && y >= mDirectionController.getPt().y - mDirectionController.getHeight()/2
                && y <= mDirectionController.getPt().y - mDirectionController.getHeight()/2) {
            return true;
        }
        return false;
    }

    public void init() {
        if(mDirectionController == null) {
            mDirectionController = new DirectionController();
        }
        if(mDirectionCenterCircle == null) {
            mDirectionCenterCircle = new DirectionCenterCircle();
        }
        if(mAttackController == null) {
            mAttackController = new AttackController();
        }
        mDirectionController.init();
        mDirectionCenterCircle.init(mDirectionController.getWidth());
        mAttackController.init();
    }

    @Override
    public void draw(Canvas canvas) {
        mDirectionController.draw(canvas);
        mDirectionCenterCircle.draw(canvas);
        mAttackController.draw(canvas);
    }
}
