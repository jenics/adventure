package com.cb.adventures.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.utils.ImageLoader;
import java.util.ArrayList;

/**
 * Created by jenics on 2015/12/20.
 * 物品栏控件
 */
public class InventoryView extends BaseView implements IControl{
    private Bitmap mBitmap;
    private Bitmap mSelectBitmap;
    private int mSelectIndex;
    private Paint mPaint;
    public static class ControlParam {
        public ControlParam(float x,float y, float width,float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        public float x;
        public float y;
        public float width;
        public float height;
    }

    private ArrayList<RectF> controls;
    private static ControlParam[] controlParams = new ControlParam[] {
            ///487,282
            //40,51
            new ControlParam(0.084f,0.266f,0.082f,0.181f),      //41,75
            new ControlParam(0.197f,0.266f,0.082f,0.181f),      //96,75
            new ControlParam(0.306f,0.266f,0.082f,0.181f),      //149,75
            new ControlParam(0.417f,0.266f,0.082f,0.181f),      //203,75
            new ControlParam(0.528f,0.266f,0.082f,0.181f),      //257,75

            new ControlParam(0.084f,0.496f,0.082f,0.181f),      //41,140
            new ControlParam(0.197f,0.496f,0.082f,0.181f),      //96,140
            new ControlParam(0.306f,0.496f,0.082f,0.181f),      //149,140
            new ControlParam(0.417f,0.496f,0.082f,0.181f),      //203,140
            new ControlParam(0.528f,0.496f,0.082f,0.181f),      //257,140

            new ControlParam(0.084f,0.727f,0.082f,0.181f),      //41,205
            new ControlParam(0.197f,0.727f,0.082f,0.181f),      //96,205
            new ControlParam(0.306f,0.727f,0.082f,0.181f),      //149,205
            new ControlParam(0.417f,0.727f,0.082f,0.181f),      //203,205
            new ControlParam(0.528f,0.727f,0.082f,0.181f),      //257,205

            new ControlParam(0.934f,0.067f,0.1f,0.085f),    ///455,19 ,49,24  丢弃
            new ControlParam(0.68f,0.713f,0.0986f,0.11f),    ///331,201,48,31 快捷键
            new ControlParam(0.856f,0.7057f,0.228f,0.11f),    ///417,199 使用

    };

    public static final int CONTROL_INVENTROY_0 = 0;
    public static final int CONTROL_INVENTROY_1 = 1;
    public static final int CONTROL_INVENTROY_2 = 2;
    public static final int CONTROL_INVENTROY_3 = 2;
    public static final int CONTROL_INVENTROY_4 = 4;

    public static final int CONTROL_INVENTROY_5 = 5;
    public static final int CONTROL_INVENTROY_6 = 6;
    public static final int CONTROL_INVENTROY_7 = 7;
    public static final int CONTROL_INVENTROY_8 = 8;
    public static final int CONTROL_INVENTROY_9 = 9;

    public static final int CONTROL_INVENTROY_10 = 10;
    public static final int CONTROL_INVENTROY_11 = 11;
    public static final int CONTROL_INVENTROY_12 = 12;
    public static final int CONTROL_INVENTROY_13 = 13;
    public static final int CONTROL_INVENTROY_14 = 14;

    public static final int CONTROL_INVENTROY_ABANDON = 15;
    public static final int CONTROL_INVENTROY_FAST = 16;
    public static final int CONTROL_INVENTROY_USE = 17;

    @Override
    public void onClick() {

    }

    public void init() {
        mBitmap = ImageLoader.getmInstance().loadBitmap(GameConstants.INVENTORY_NAME);
        mSelectBitmap = ImageLoader.getmInstance().loadBitmap(GameConstants.INVENTORY_SELECTED_NAME);
        ///宽度是屏幕宽度的0.15倍
        width = (int) (GameConstants.sGameWidth*0.7f);
        height = (int) (GameConstants.sGameHeight*0.7f);
        ///
        pt.x = GameConstants.sGameWidth/2;
        pt.y = GameConstants.sGameHeight/2;

        if (mPaint == null)
            mPaint = new Paint();
        mPaint.setAntiAlias(true);  ///抗锯齿
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mPaint.setTypeface(font);

        controls = new ArrayList<>();

        float xBase = pt.x - width/2;
        float yBase = pt.y - height/2;
        for (ControlParam controlParam : controlParams) {
            controls.add(new RectF(
                    xBase + controlParam.x * width - width * controlParam.width / 2,
                    yBase + controlParam.y * height - height * controlParam.height / 2,
                    xBase + controlParam.x * width + width * controlParam.width / 2,
                    yBase + controlParam.y * height + height * controlParam.height / 2
            ));
        }

        mSelectIndex = -1;
    }

    @Override
    public void onMouseDown(int x, int y) {

    }

    @Override
    public void onMouseMove(int x, int y) {

    }

    @Override
    public void onMouseUp(int x, int y) {
        ///在这里判断是哪个物品被点击  或者哪个命令按钮被点击
        if (x < getPt().x-width/2
                || x > getPt().x + width/2
                || y < getPt().y - height/2
                || y > getPt().y + height/2) {
            setIsVisiable(false);
            return;
        }
        int control = ptInRegion(x,y);
        if (control >= 0) {
            //Toast.makeText(MyApplication.getContextObj(),String.format("u pressed %d", control),Toast.LENGTH_SHORT).show();
            mSelectIndex = control;
        }
    }


    public int ptInRegion(int x, int y) {
        int regionTouch = -1;
        for(int i=0; i<controls.size(); ++i) {
            RectF rectf = controls.get(i);
            if (x >= rectf.left && x <= rectf.right && y >= rectf.top && y <= rectf.bottom) {
                regionTouch = i;
                break;
            }
        }
        return regionTouch;
    }

    @Override
    public void draw(Canvas canvas) {
        float x = getPt().x - width/2;
        float y = getPt().y - height/2;

        ///画控制器框框
        canvas.drawBitmap(mBitmap,
                new Rect(   ///src rect
                        0,
                        0,
                        mBitmap.getWidth(),
                        mBitmap.getHeight()),
                new RectF(x,
                        y,
                        x + width,
                        y + height), null);

//        for(int i=0; i<controls.size(); ++i) {
//            canvas.drawRect(controls.get(i),mPaint);
//        }

        if (mSelectIndex >= CONTROL_INVENTROY_0 && mSelectIndex <= CONTROL_INVENTROY_14) {
            ///画选中器
            canvas.drawBitmap(mSelectBitmap,
                    new Rect(   ///src rect
                            0,
                            0,
                            mSelectBitmap.getWidth(),
                            mSelectBitmap.getHeight()),
                    controls.get(mSelectIndex), null);
            ///画简介
        }
    }
}
