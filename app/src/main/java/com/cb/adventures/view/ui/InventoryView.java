package com.cb.adventures.view.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.data.MoneyPropetry;
import com.cb.adventures.data.PropPropetry;
import com.cb.adventures.prop.Consume;
import com.cb.adventures.prop.Equipment;
import com.cb.adventures.prop.IProp;
import com.cb.adventures.prop.IStackable;
import com.cb.adventures.prop.IUsable;
import com.cb.adventures.view.PlayerMediator;
import com.cb.adventures.utils.CLog;
import com.cb.adventures.utils.FontFace;
import com.cb.adventures.utils.ImageLoader;
import com.cb.adventures.view.BaseView;
import com.cb.adventures.view.IControl;
import com.cb.adventures.view.Player;
import com.cb.adventures.view.PropView;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by jenics on 2015/12/20.
 * 物品栏控件
 */
public class InventoryView extends BaseView implements IControl, PropView.PickUpPropListener {
    private PlayerMediator mPlayerMediator;
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
    public static final int CONTROL_INVENTROY_TITLE = 18;
    public static final int CONTROL_INVENTROY_ICON = 19;
    public static final int CONTROL_INVENTROY_CONTENT = 20;
    public static final int CONTROL_INVENTROY_EXTRA = 21;
    public static final int CONTROL_INVENTROY_MONEY = 22;
    private Bitmap mSelectBitmap;
    private int mSelectIndex;
    //private LinkedList<IProp> mProps;
    private static final int MAX_KARD_NUM = 15;
    private Paint.FontMetricsInt mFontMetricsInt;
    private Player mPlayer;
    private TextPaint mTextPaint;

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
        public ControlParam(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            state = KardEnum.KARD_EMPTY;
            iProp = null;
            willAdd = 0;
            proId = -1;
        }

        public float x;
        public float y;
        public float width;
        public float height;

        public KardEnum state;
        public int proId;
        public IProp iProp;
        /**
         * 将要加入进去，state == ready时关心
         */
        public int willAdd;
    }

    private ArrayList<RectF> controls;
    private static ControlParam[] controlParams = new ControlParam[]{
            ///487,282
            //40,51
            new ControlParam(0.084f, 0.266f, 0.082f, 0.181f),      //41,75
            new ControlParam(0.197f, 0.266f, 0.082f, 0.181f),      //96,75
            new ControlParam(0.306f, 0.266f, 0.082f, 0.181f),      //149,75
            new ControlParam(0.417f, 0.266f, 0.082f, 0.181f),      //203,75
            new ControlParam(0.528f, 0.266f, 0.082f, 0.181f),      //257,75

            new ControlParam(0.084f, 0.496f, 0.082f, 0.181f),      //41,140
            new ControlParam(0.197f, 0.496f, 0.082f, 0.181f),      //96,140
            new ControlParam(0.306f, 0.496f, 0.082f, 0.181f),      //149,140
            new ControlParam(0.417f, 0.496f, 0.082f, 0.181f),      //203,140
            new ControlParam(0.528f, 0.496f, 0.082f, 0.181f),      //257,140

            new ControlParam(0.084f, 0.727f, 0.082f, 0.181f),      //41,205
            new ControlParam(0.197f, 0.727f, 0.082f, 0.181f),      //96,205
            new ControlParam(0.306f, 0.727f, 0.082f, 0.181f),      //149,205
            new ControlParam(0.417f, 0.727f, 0.082f, 0.181f),      //203,205
            new ControlParam(0.528f, 0.727f, 0.082f, 0.181f),      //257,205

            new ControlParam(0.934f, 0.067f, 0.1f, 0.085f),    ///455,19 ,49,24  丢弃
            new ControlParam(0.68f, 0.713f, 0.0986f, 0.11f),    ///331,201,48,31 快捷键
            new ControlParam(0.856f, 0.7057f, 0.228f, 0.11f),    ///417,199 使用

            //////487,282
            new ControlParam(0.807f, 0.1667f, 0.247f, 0.07447f),    ///title  393  47 169 21
            new ControlParam(0.924f, 0.305f, 0.0821f, 0.1773f),    ///icon  450 86 40 50
            new ControlParam(0.7577f, 0.4113f, 0.2464f, 0.4f),    ///content 369 116 120 124
            new ControlParam(0.809f, 0.8865f, 0.38f, 0.2127f),  ///desc 394,250,185,60
            new ControlParam(0.4589f, 0.0532f, 0.2772f, 0.10638f),  ///money,223.5,15,135,30
    };


    /**
     * key,  对象ID
     * value, 位置
     */
    private HashMap<Long, Integer> locMap;

    public InventoryView(PlayerMediator playerMediator) {
        locMap = new HashMap<>();
        this.mPlayerMediator = playerMediator;
    }

    /**
     * @param selectIndex 移除的位置
     * @return 移除的位置
     */
    public int removeProp(int selectIndex) {
        mReentrantReadWriteLock.writeLock().lock();
        if (selectIndex<= CONTROL_INVENTROY_14 && selectIndex>= CONTROL_INVENTROY_0) {
            //mProps.remove(prop);
            controlParams[selectIndex].state = KardEnum.KARD_EMPTY;
            controlParams[selectIndex].willAdd = 0;
            controlParams[selectIndex].iProp = null;
            controlParams[selectIndex].proId = -1;
        }
        mReentrantReadWriteLock.writeLock().unlock();
        return selectIndex;
    }

    /**
     * @param iProp 移除的物品
     * @return 移除的位置
     */
    public int removeProp(IProp iProp) {
        mReentrantReadWriteLock.writeLock().lock();
        int selectIndex = 0;
        for (ControlParam controlParam : controlParams) {

            if (controlParam.iProp != null && controlParam.iProp.getObjId() == iProp.getObjId()) {
                break;
            }
            selectIndex++;
        }

        if (selectIndex<= CONTROL_INVENTROY_14 && selectIndex>= CONTROL_INVENTROY_0) {
            //mProps.remove(prop);
            controlParams[selectIndex].state = KardEnum.KARD_EMPTY;
            controlParams[selectIndex].willAdd = 0;
            controlParams[selectIndex].iProp = null;
            controlParams[selectIndex].proId = -1;
        }
        mReentrantReadWriteLock.writeLock().unlock();
        return selectIndex;
    }

    public void addProp(int index, IProp prop) {
        mReentrantReadWriteLock.writeLock().lock();
        if (index<= CONTROL_INVENTROY_14 && index>= CONTROL_INVENTROY_0) {
            controlParams[index].state = KardEnum.KARD_OCCUPY;
            controlParams[index].willAdd = 0;
            controlParams[index].iProp = prop;
            controlParams[index].proId = (int) prop.getPropId();
        }
        mReentrantReadWriteLock.writeLock().unlock();
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
        if (prop.getPropType() == PropPropetry.PROP_TYPE_MONEY) {
            ///直接加入到钱里面
            mPlayerMediator.addMoney(((MoneyPropetry)prop).getMoney());
        } else {
            Integer integer = locMap.remove(prop.getObjId());
            if (integer == null) {
                CLog.e("InventoryView", "error in onPickUpOver,the index is unindefition");
            }
            int index = integer;

            controlParams[index].state = KardEnum.KARD_OCCUPY;
            controlParams[index].proId = prop.getPropId();
            controlParams[index].willAdd--;
            if (controlParams[index].iProp == null) {
                ///新建
                if (prop.getPropType() == PropPropetry.PROP_TYPE_EQUIP) {
                    Equipment equipment = new Equipment(mPlayerMediator, prop);
                    controlParams[index].iProp = equipment;
                } else if (prop.getPropType() == PropPropetry.PROP_TYPE_CONSUME) {
                    Consume consume = new Consume(mPlayerMediator, prop);
                    controlParams[index].iProp = consume;
                }
            } else {
                if (controlParams[index].iProp instanceof IStackable)
                    ((IStackable) controlParams[index].iProp).addStack(1);
            }
        }
        mReentrantReadWriteLock.writeLock().unlock();
    }


    public boolean canPickUp(PropPropetry propetry) {
        mReentrantReadWriteLock.writeLock().lock();
        boolean canPickUp = false;
        if (propetry.getPropType() == PropPropetry.PROP_TYPE_MONEY) {
            canPickUp = true;
        } else if (propetry.getMaxStackSize() == 1) {
            ///不可堆叠的
            for (int i = 0; i < MAX_KARD_NUM; ++i) {
                if (controlParams[i].state == KardEnum.KARD_EMPTY) {
                    canPickUp = true;
                    controlParams[i].state = KardEnum.KARD_READY;
                    controlParams[i].proId = propetry.getPropId();
                    controlParams[i].willAdd++;
                    locMap.put(propetry.getObjId(), i);
                    break;
                }
            }
        } else {
            ///可堆叠的
            int empty = -1;
            int canStackLoc = -1;
            for (int i = 0; i < MAX_KARD_NUM; ++i) {
                if (controlParams[i].state == KardEnum.KARD_EMPTY) {
                    if (empty == -1) {
                        empty = i;
                    }
                } else {
                    ///查看最先可以堆叠的
                    if (controlParams[i].proId == propetry.getPropId()) {
                        IProp iProp = controlParams[i].iProp;
                        if (null == iProp) {
                            if (controlParams[i].willAdd < propetry.getMaxStackSize()) {
                                canStackLoc = i;
                                break;
                            }
                        } else {
                            IStackable iStackable = (IStackable) controlParams[i].iProp;
                            if (controlParams[i].willAdd + iStackable.getCurrentStackSize() < propetry.getMaxStackSize()) {
                                canStackLoc = i;
                                break;
                            }
                        }
                    }
                }
            }
            if (canStackLoc != -1) {
                canPickUp = true;
                if (controlParams[canStackLoc].state == KardEnum.KARD_EMPTY)
                    controlParams[canStackLoc].state = KardEnum.KARD_READY;
                controlParams[canStackLoc].proId = propetry.getPropId();
                controlParams[canStackLoc].willAdd++;
                locMap.put(propetry.getObjId(), canStackLoc);
            } else if (empty != -1) {
                canPickUp = true;
                if (controlParams[empty].state == KardEnum.KARD_EMPTY)
                    controlParams[empty].state = KardEnum.KARD_READY;
                controlParams[empty].proId = propetry.getPropId();
                controlParams[empty].willAdd++;
                locMap.put(propetry.getObjId(), empty);
            }
        }
        mReentrantReadWriteLock.writeLock().unlock();
        return canPickUp;
    }

    @Override
    public void onClick() {

    }

    public void init() {
        mBitmap = ImageLoader.getInstance().loadBitmap(GameConstants.INVENTORY_NAME);
        mSelectBitmap = ImageLoader.getInstance().loadBitmap(GameConstants.INVENTORY_SELECTED_NAME);
        ///宽度是屏幕宽度的0.15倍
        width = (int) (GameConstants.sGameWidth * 0.7f);
        height = (int) (GameConstants.sGameHeight * 0.7f);
        ///
        pt.x = GameConstants.sGameWidth / 2;
        pt.y = GameConstants.sGameHeight / 2;

        if (mPaint == null)
            mPaint = new Paint();
        mPaint.setAntiAlias(true);  ///抗锯齿
        mPaint.setTypeface(FontFace.getInstance().getFontFace(FontFace.E_Font_Face.COMIXHEAVY));
        mPaint.setTextSize(30);
        mPaint.setColor(Color.YELLOW);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mFontMetricsInt = mPaint.getFontMetricsInt();

        if (mTextPaint == null)
            mTextPaint=new TextPaint();
        //mTextPaint.setTypeface(FontFace.getInstance().getFontFace(FontFace.E_Font_Face.COMIXHEAVY));
        mTextPaint.setTextSize(30);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLUE);

        controls = new ArrayList<>();

        float xBase = pt.x - width / 2;
        float yBase = pt.y - height / 2;
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
    public boolean onMouseDown(int x, int y) {
        return false;
    }

    @Override
    public boolean onMouseMove(int x, int y) {
        return false;
    }

    @Override
    public boolean onMouseUp(int x, int y) {
        ///在这里判断是哪个物品被点击  或者哪个命令按钮被点击
        if (x < getPt().x - width / 2
                || x > getPt().x + width / 2
                || y < getPt().y - height / 2
                || y > getPt().y + height / 2) {
            setIsVisiable(false);
            return true;
        }
        int control = ptInRegion(x, y);
        if (control >= CONTROL_INVENTROY_0 && control <= CONTROL_INVENTROY_14) {
            mSelectIndex = control;
        } else if (control == CONTROL_INVENTROY_ABANDON) {
            removeProp(mSelectIndex);
        } else if (control == CONTROL_INVENTROY_FAST) {

        } else if (control == CONTROL_INVENTROY_USE) {
            use(mSelectIndex);
        }
        return true;
    }

    public void use(int selectIndex) {
        mReentrantReadWriteLock.writeLock().lock();
        if (selectIndex <= CONTROL_INVENTROY_14 && selectIndex >= CONTROL_INVENTROY_0) {
            IProp iProp = controlParams[selectIndex].iProp;
            if (iProp != null) {
                if (iProp instanceof IUsable)
                    ((IUsable)iProp).use();
            }
        }
        mReentrantReadWriteLock.writeLock().unlock();
    }

    /**
     * 智能使用
     *
     * @param iProp
     */
    public void use(IProp iProp) {
        mReentrantReadWriteLock.readLock().lock();
        int index = -1;
        for (int i = 0; i < MAX_KARD_NUM; ++i) {
            if (controlParams[i].proId == iProp.getPropId()) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            use(index);
        }
        mReentrantReadWriteLock.readLock().unlock();
    }

    public int ptInRegion(int x, int y) {
        int regionTouch = -1;
        for (int i = 0; i < controls.size(); ++i) {
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

        float x = getPt().x - width / 2;
        float y = getPt().y - height / 2;

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
            mReentrantReadWriteLock.readLock().lock();
            IProp iProp = controlParams[mSelectIndex].iProp;
            if (iProp != null) {
                ///title
                mPaint.setColor(Color.RED);
                String strTitle = iProp.getName();
                canvas.drawText(strTitle, controls.get(CONTROL_INVENTROY_TITLE).left, controls.get(CONTROL_INVENTROY_TITLE).bottom - mFontMetricsInt.descent, mPaint);
                ///restore the paint
                mPaint.setColor(Color.YELLOW);
                ///icon
                Bitmap bitmap = ImageLoader.getInstance().loadBitmap(iProp.getIcon());
                canvas.drawBitmap(bitmap,
                        new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), controls.get(CONTROL_INVENTROY_ICON)
                        , null);
                ///desc
                String[] strings = iProp.getDescription();
                float topY = controls.get(CONTROL_INVENTROY_CONTENT).top;
                for(String string : strings) {
                    if (string == null) {
                        continue;
                    }
                    StaticLayout mTextLayout = new StaticLayout(string,
                            mTextPaint, (int) controls.get(CONTROL_INVENTROY_CONTENT).width(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    canvas.save();
                    canvas.translate(controls.get(CONTROL_INVENTROY_CONTENT).left, topY);
                    mTextLayout.draw(canvas);
                    canvas.restore();
                    topY += (mFontMetricsInt.bottom-mFontMetricsInt.top);
                }

                ///extra
                StaticLayout mTextLayout = new StaticLayout(iProp.getExtra(),
                        mTextPaint, (int) controls.get(CONTROL_INVENTROY_EXTRA).width(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                canvas.save();
                canvas.translate(controls.get(CONTROL_INVENTROY_EXTRA).left, controls.get(CONTROL_INVENTROY_EXTRA).top);
                mTextLayout.draw(canvas);
                canvas.restore();

            }
            mReentrantReadWriteLock.readLock().unlock();
        }

        mReentrantReadWriteLock.readLock().lock();
        ///画物品
        for (int i = 0; i < controlParams.length; ++i) {
            IProp iProp = controlParams[i].iProp;

            if (controlParams[i].state == KardEnum.KARD_OCCUPY) {
                Bitmap bitmap = ImageLoader.getInstance().loadBitmap(iProp.getIcon());
                canvas.drawBitmap(bitmap,
                        new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), controls.get(i)
                        , null);

                ///画叠加数量
                if (iProp instanceof IStackable) {
                    String text = Integer.valueOf(((IStackable) iProp).getCurrentStackSize()).toString();
                    float textWidth = mPaint.measureText(text);
                    canvas.drawText(text, controls.get(i).right - textWidth, controls.get(i).bottom - mFontMetricsInt.descent, mPaint);
                }
            }
        }
        mReentrantReadWriteLock.readLock().unlock();

        ///画钱
        RectF targetRect = controls.get(CONTROL_INVENTROY_MONEY);
        int baseline = (int) ((targetRect.bottom + targetRect.top - mFontMetricsInt.bottom - mFontMetricsInt.top) / 2);
        canvas.drawText(Long.toString(mPlayerMediator.getMoney()), targetRect.centerX(), baseline, mPaint);
    }
}
