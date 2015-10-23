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

    public OnControllerListener getmOnControllerListener() {
        return mOnControllerListener;
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
        public void onDirectionChange(int direction);
        public void onAttack();
        public void onStop();
    }

    public void onMouseDown(float x,float y) {
        int dir = onMouseEvent(x,y);
        if(dir == GameConstants.STATE_NONE) {
            return;
        }

        if(dir == GameConstants.STATE_ATTACK_LEFT) {
            mOnControllerListener.onAttack();
        }else {
            mOnControllerListener.onDirectionChange(dir);
        }
    }

    public void onMouseMove(float x,float y) {
        int dir = onMouseEvent(x,y);
        if(dir == GameConstants.STATE_NONE) {
            return;
        }
        if(dir == GameConstants.STATE_MOVE_LEFT
                || dir == GameConstants.STATE_MOVE_RIGHT) {
            mOnControllerListener.onDirectionChange(dir);
        }
    }

    public void onMouseUp(float x,float y) {
        resetCenterCircle();
        mOnControllerListener.onStop();
    }

    public void resetCenterCircle() {
        mDirectionCenterCircle.reset();
    }

    /**
     * 返回GameConstants指定的方向，如果没有命中，返回DIRECTION_NONE
     * @param x x坐标
     * @param y y坐标
     */
    public int onMouseEvent(float x ,float y) {
        if((x >= (mDirectionController.getPt().x*1.0f))
                &&  (x <=  (mDirectionController.getPt().x*1.0f + mDirectionController.getWidth()/2.0f))
                && y >= mDirectionController.getPt().y - mDirectionController.getHeight()/2
                && y <= mDirectionController.getPt().y + mDirectionController.getHeight()/2) {
            mDirectionCenterCircle.setPt(x,y);
            return GameConstants.STATE_MOVE_RIGHT;
        }else if(x >= mDirectionController.getPt().x*1.0f - mDirectionController.getWidth()/2.0f
                && x <  mDirectionController.getPt().x*1.0f
                && y >= mDirectionController.getPt().y - mDirectionController.getHeight()/2
                && y <= mDirectionController.getPt().y + mDirectionController.getHeight()/2) {
            mDirectionCenterCircle.setPt(x,y);
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

    private boolean ptInRegion(float x ,float y) {
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
