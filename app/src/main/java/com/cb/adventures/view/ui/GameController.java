package com.cb.adventures.view.ui;

import android.graphics.Canvas;
import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.view.BaseView;
import com.cb.adventures.view.IControl;

import java.util.LinkedList;


/**
 * Created by jenics on 2015/10/22.
 */
public class GameController extends BaseView implements IControl {
    private DirectionController mDirectionController;
    private DirectionCenterCircle mDirectionCenterCircle;
    private AttackController mAttackController;
    private static final int FUNCTION_NUM = 4;
    private LinkedList<FunctionController> functionControllers;


    public GameController() {
        functionControllers = new LinkedList<>();
        for(int i=0; i<FUNCTION_NUM; ++i) {
            functionControllers.add(new FunctionController());
        }
    }

    public void setOnControllerListener(OnControllerListener mOnControllerListener) {
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

        /**
         * 攻击
         */
        void onAttack();
        void onStop();

        /**
         * 功能键，可以放技能，物品
         * @param index 功能键ID，起于0
         */
        void onFunction(int index);
    }

    private boolean isDirectionControllerDown;
    @Override
    public boolean onMouseDown(int x, int y) {
        int controll = onMouseEvent(x,y);

        if(controll == GameConstants.CONTROL_ATTACK ) {
            mOnControllerListener.onAttack();
        }else if(controll == GameConstants.CONTROL_LEFT || controll == GameConstants.CONTROL_RIGHT) {
            mOnControllerListener.onDirectionChange(controll);
        } else if(controll >= GameConstants.CONTROL_SPECIAL_KEY_0 && controll <= GameConstants.CONTROL_SPECIAL_KEY_3) {
            mOnControllerListener.onFunction(controll-GameConstants.CONTROL_SPECIAL_KEY_0);
        }

        return true;
    }
    @Override
    public boolean onMouseMove(int x,int y) {
        int controll = onMouseEvent(x,y);
        if(controll == GameConstants.CONTROL_LEFT || controll == GameConstants.CONTROL_RIGHT) {
            mOnControllerListener.onDirectionChange(controll);
            return true;
        }
        return false;
    }
    @Override
    public boolean onMouseUp(int x,int y) {
        resetCenterCircle();
        if(isDirectionControllerDown) {
            mOnControllerListener.onStop();
        }
        isDirectionControllerDown = false;
        return true;
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
            return GameConstants.CONTROL_RIGHT;
        }else if(x >= mDirectionController.getPt().x - mDirectionController.getWidth()/2
                && x <  mDirectionController.getPt().x
                && y >= mDirectionController.getPt().y - mDirectionController.getHeight()/2
                && y <= mDirectionController.getPt().y + mDirectionController.getHeight()/2) {
            mDirectionCenterCircle.setPt(x,y);
            isDirectionControllerDown = true;
            return GameConstants.CONTROL_LEFT;
        }else if(x >= mAttackController.getPt().x-mAttackController.getWidth()/2
                && x <= mAttackController.getPt().x + mAttackController.getWidth()/2
                && y >= mAttackController.getPt().y - mAttackController.getHeight()/2
                && y <= mAttackController.getPt().y + mAttackController.getHeight()/2) {
            return GameConstants.CONTROL_ATTACK;
        }else if(x >= functionControllers.get(0).getPt().x- functionControllers.get(0).getWidth()/2
                && x <= functionControllers.get(0).getPt().x + functionControllers.get(0).getWidth()/2
                && y >= functionControllers.get(0).getPt().y - functionControllers.get(0).getHeight()/2
                && y <= functionControllers.get(0).getPt().y + functionControllers.get(0).getHeight()/2) {
            return GameConstants.CONTROL_SPECIAL_KEY_0;
        }else if(x >= functionControllers.get(1).getPt().x- functionControllers.get(1).getWidth()/2
                && x <= functionControllers.get(1).getPt().x + functionControllers.get(1).getWidth()/2
                && y >= functionControllers.get(1).getPt().y - functionControllers.get(1).getHeight()/2
                && y <= functionControllers.get(1).getPt().y + functionControllers.get(1).getHeight()/2) {
            return GameConstants.CONTROL_SPECIAL_KEY_1;
        }else if(x >= functionControllers.get(2).getPt().x- functionControllers.get(2).getWidth()/2
                && x <= functionControllers.get(2).getPt().x + functionControllers.get(2).getWidth()/2
                && y >= functionControllers.get(2).getPt().y - functionControllers.get(2).getHeight()/2
                && y <= functionControllers.get(2).getPt().y + functionControllers.get(2).getHeight()/2) {
            return GameConstants.CONTROL_SPECIAL_KEY_2;
        }else if(x >= functionControllers.get(3).getPt().x- functionControllers.get(3).getWidth()/2
                && x <= functionControllers.get(3).getPt().x + functionControllers.get(3).getWidth()/2
                && y >= functionControllers.get(3).getPt().y - functionControllers.get(3).getHeight()/2
                && y <= functionControllers.get(3).getPt().y + functionControllers.get(3).getHeight()/2) {
            return GameConstants.CONTROL_SPECIAL_KEY_3;
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
        ///假设普通攻击attackController在坐标轴中心，根据x*x + y*y = r*r可以求出x,y
        ///r的长度就是attackController的宽度/2 + skillattackController的宽度/2 + 间距
        float r = GameConstants.sGameWidth
                * ((AttackController.WIDTH_RATIO+ FunctionController.WIDTH_RATIO)/2)  ///直径/2等于半径
                + GameConstants.sGameWidth*0.05f;   ///间距
        float x = 0.0f;
        float y = 0.0f;
        ///control1，当x=0时，y的值就是r
        x = 0.0f;
        y = -r;
        functionControllers.get(0).setPt(mAttackController.getPt().x + x, mAttackController.getPt().y + y);
        functionControllers.get(0).init();

        ///control2,当y等于r/1.5时，x的值
        y = -r/1.3f;
        x = -(float) Math.sqrt(r*r - y*y);
        functionControllers.get(1).setPt(mAttackController.getPt().x + x, mAttackController.getPt().y + y);
        functionControllers.get(1).init();

        ///control3,当y = 0时,x的值
        y = -r*0.2f;
        x = -(float) Math.sqrt(r*r - y*y);
        functionControllers.get(2).setPt(mAttackController.getPt().x + x, mAttackController.getPt().y + y);
        functionControllers.get(2).init();

        y = r*0.45f;
        x = -(float) Math.sqrt(r*r - y*y);
        functionControllers.get(3).setPt(mAttackController.getPt().x + x, mAttackController.getPt().y + y);
        functionControllers.get(3).init();
    }

    @Override
    public void draw(Canvas canvas) {
        mDirectionController.draw(canvas);
        mDirectionCenterCircle.draw(canvas);
        mAttackController.draw(canvas);
        for(int i=0; i<FUNCTION_NUM; ++i) {
            functionControllers.get(i).draw(canvas);
        }
    }

    public FunctionController getFunctionController(int index) {
        return functionControllers.get(index);
    }
}
