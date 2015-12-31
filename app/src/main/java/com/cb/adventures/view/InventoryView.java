package com.cb.adventures.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.data.GameData;
import com.cb.adventures.data.PropPropetry;
import com.cb.adventures.prop.Consume;
import com.cb.adventures.prop.Equipment;
import com.cb.adventures.prop.IProp;
import com.cb.adventures.utils.FontFace;
import com.cb.adventures.utils.ImageLoader;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by jenics on 2015/12/20.
 * 物品栏控件
 */
public class InventoryView  extends BaseView implements IControl , PropView.PickUpPropListener {
    private static InventoryView mInstance;
    private Bitmap mSelectBitmap;
    private int mSelectIndex;
    private LinkedList<IProp> mProps;
    private static final int MAX_KARD_NUM = 15;
    private Paint.FontMetricsInt mFontMetricsInt;
    /**
     * key,  属性ID
     * value, 位置
     */
    private HashMap<Integer,Integer> locMap;
    private InventoryView() {
        mProps = new LinkedList<>();
        locMap = new HashMap<>();
    }

    public synchronized static InventoryView getInstance() {
        if (mInstance == null) {
            mInstance = new InventoryView();
        }
        return mInstance;
    }

    /**
     * 读写锁
     */
    private final ReentrantReadWriteLock mReentrantReadWriteLock = new ReentrantReadWriteLock();

    @Override
    public void onPickUpBegin(PropPropetry prop) {

    }

    @Override
    public void onPickUpOver(PropPropetry prop) {
        mReentrantReadWriteLock.writeLock().lock();
        ///做有效性检查
        int index = locMap.get(prop.getPropId());
        controlParams[index].state = KardEnum.KARD_OCCUPY;
        controlParams[index].proId = prop.getPropId();
        if (index <= mProps.size()-1) {
            ///堆叠
            ((Consume)mProps.get(index)).addProp(1);
        } else {
            ///新建
            if (prop.getMaxStackSize() == 1) {
                Equipment equipment = new Equipment(null,prop);
                mProps.add(equipment);
            } else {
                Consume consume = new Consume(null, prop);
                mProps.add(consume);
            }
        }
        mReentrantReadWriteLock.writeLock().unlock();
    }


    public boolean canPickUp(PropPropetry propetry) {
        mReentrantReadWriteLock.readLock().lock();
        boolean canPickUp = false;
        if (propetry.getMaxStackSize() == 1) {
            ///不可堆叠的
            if (mProps.size() >= MAX_KARD_NUM-1) {
                canPickUp = false;
            } else {
                for (int i=mProps.size(); i<MAX_KARD_NUM; ++i) {
                    if (controlParams[i].state == KardEnum.KARD_EMPTY) {
                        canPickUp = true;
                        controlParams[i].state = KardEnum.KARD_READY;
                        controlParams[i].proId = propetry.getPropId();
                        locMap.put(propetry.getPropId(),i);
                        break;
                    }
                }
            }
        } else {
            ///可堆叠的
            IProp findProp = null;
            int proIndex = 0;
            for(IProp iprop : mProps) {
                if (propetry.getPropId() == iprop.getPropId()) {
                    if (canStack(propetry,iprop)) {
                        canPickUp = true;
                        findProp = iprop;
                        locMap.put(propetry.getPropId(),proIndex);
                        break;
                    }
                }
                proIndex ++;
            }
            if (findProp == null &&  (mProps.size() < controlParams.length)) {
                ///不可堆叠，则新找一个卡片标记等待它进入
                for (int i=mProps.size(); i<MAX_KARD_NUM; ++i) {
                    if (controlParams[i].state == KardEnum.KARD_EMPTY) {
                        canPickUp = true;
                        controlParams[i].state = KardEnum.KARD_READY;
                        controlParams[i].proId = propetry.getPropId();
                        locMap.put(propetry.getPropId(),i);
                        break;
                    }
                }
            }
        }
        mReentrantReadWriteLock.readLock().unlock();
        return canPickUp;
    }

    /**
     * @param propetry 物品属性
     * @return 是否可堆叠
     */
    private boolean canStack(PropPropetry propetry,IProp iProp) {
        if (propetry.getMaxStackSize() == 1) {
            return false;
        } else {
            if (iProp.getCurrentStackSize() < propetry.getMaxStackSize()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public enum KardEnum {
        /**
         * 卡片为空
         */
        KARD_EMPTY,
        /**
         * 准备占用
         */
        KARD_READY,
        /**
         * 已占用
         */
        KARD_OCCUPY
    }

    public static class ControlParam {
        public ControlParam(float x,float y, float width,float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            state = KardEnum.KARD_EMPTY;
        }
        public float x;
        public float y;
        public float width;
        public float height;

        public KardEnum state;
        public int proId;
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

    public static final int INVALIDATE_SELECT_INDEX = -1;

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
        mBitmap = ImageLoader.getInstance().loadBitmap(GameConstants.INVENTORY_NAME);
        mSelectBitmap = ImageLoader.getInstance().loadBitmap(GameConstants.INVENTORY_SELECTED_NAME);
        ///宽度是屏幕宽度的0.15倍
        width = (int) (GameConstants.sGameWidth*0.7f);
        height = (int) (GameConstants.sGameHeight*0.7f);
        ///
        pt.x = GameConstants.sGameWidth/2;
        pt.y = GameConstants.sGameHeight/2;

        if (mPaint == null)
            mPaint = new Paint();
        mPaint.setAntiAlias(true);  ///抗锯齿
        mPaint.setTypeface(FontFace.getInstance().getFontFace(FontFace.E_Font_Face.COMIXHEAVY));
        mPaint.setTextSize(30);
        mPaint.setColor(Color.YELLOW);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mFontMetricsInt = mPaint.getFontMetricsInt();

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

        mSelectIndex = INVALIDATE_SELECT_INDEX;
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
            //Toast.makeText(AdventureApplication.getContextObj(),String.format("u pressed %d", control),Toast.LENGTH_SHORT).show();
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

        if (!isVisiable)
            return;

        float x = getPt().x - width/2;
        float y = getPt().y - height/2;

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

        mReentrantReadWriteLock.readLock().lock();
        ///画物品
        for (int i=0; i<mProps.size(); ++i) {
            IProp iProp = mProps.get(i);

            Bitmap bitmap = ImageLoader.getInstance().loadBitmap(iProp.getIcon());
            canvas.drawBitmap(bitmap,
                    new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()),controls.get(i)
                            ,null);

            ///画叠加数量
            if (iProp instanceof Consume) {
                String text = Integer.valueOf(iProp.getCurrentStackSize()).toString();
                float textWidth = mPaint.measureText(text);
                canvas.drawText(text, controls.get(i).right-textWidth,controls.get(i).bottom-mFontMetricsInt.descent,mPaint);
            }

        }
        mReentrantReadWriteLock.readLock().unlock();
    }
}
