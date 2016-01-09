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
import com.cb.adventures.data.IPropetry;
import com.cb.adventures.data.Propetry;
import com.cb.adventures.prop.IEquipment;
import com.cb.adventures.view.PlayerMediator;
import com.cb.adventures.utils.FontFace;
import com.cb.adventures.utils.ImageLoader;
import com.cb.adventures.view.BaseView;
import com.cb.adventures.view.IControl;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by jenics on 2016/1/2.
 */
public class EquipmentBar extends BaseView implements IControl , IPropetry {
    private Bitmap mSelectBitmap;
    private int mSelectIndex;
    private Paint.FontMetricsInt mFontMetricsInt;
    private TextPaint mTextPaint;
    private Propetry propetry;
    private PlayerMediator mPlayerMediator;
    /**
     * 读写锁
     */
    private final ReentrantReadWriteLock mReentrantReadWriteLock = new ReentrantReadWriteLock();
    public static final int INVALIDATE_SELECT_INDEX = -1;

    /**
     * 身体
     */
    public static final int EQUIP_BODY = 0;
    /**
     * 武器
     */
    public static final int EQUIP_WEQPON = 1;
    /**
     * 手部
     */
    public static final int EQUIP_HAND = 2;
    /**
     * 头部
     */
    public static final int EQUIP_HEAD = 3;
    /**
     * 腰部
     */
    public static final int EQUIP_WAIST = 4;
    /**
     * 脚部
     */
    public static final int EQUIP_FEET = 5;
    /**
     * 戒指
     */
    public static final int EQUIP_RING = 6;
    /**
     * 项链
     */
    public static final int EQUIP_NECKLACE = 7;

    /**
     * 属性面板
     */
    public static final int PROPETRY_PANEL = 8;


    public EquipmentBar(PlayerMediator playerMediator) {
        propetry = new Propetry();
        mSelectIndex = INVALIDATE_SELECT_INDEX;
        mPlayerMediator = playerMediator;
    }


    @Override
    public int getAttackPower() {
        return propetry.getAttackPower();
    }

    @Override
    public int getDefensivePower() {
        return propetry.getDefensivePower();
    }

    @Override
    public int getRank() {
        return 0;
    }

    @Override
    public int getMagicTotalVolume() {
        return propetry.getMagicTotalVolume();
    }

    @Override
    public int getBloodTotalVolume() {
        return propetry.getBloodTotalVolume();
    }

    @Override
    public float getSpeed() {
        return propetry.getSpeed();
    }

    @Override
    public float getCriticalRate() {
        return propetry.getCriticalRate();
    }

    @Override
    public float getCriticalDamage() {
        return propetry.getCriticalDamage();
    }

    /**
     * 装备物品
     *
     * @param equipment 装备
     * @return 写下的装备
     */
    public IEquipment equipment(IEquipment equipment) {
        IEquipment iEquipment = controlParams[equipment.getEquipLocation()].iEquipment;

        if (iEquipment != null) {
            unEquipment(iEquipment);
        }
        controlParams[equipment.getEquipLocation()].iEquipment = equipment;
        propetry.setMagicTotalVolume(propetry.getMagicTotalVolume() + equipment.getMagicTotalVolume());
        propetry.setBloodTotalVolume(propetry.getBloodTotalVolume() + equipment.getBloodTotalVolume());
        propetry.setAttackPower(propetry.getAttackPower() + equipment.getAttackPower());
        propetry.setDefensivePower(propetry.getDefensivePower() + equipment.getDefensivePower());
        propetry.setSpeed(propetry.getSpeed() + equipment.getSpeed());
        propetry.setCriticalDamage(propetry.getCriticalDamage() + equipment.getCriticalDamage());
        propetry.setCriticalRate(propetry.getCriticalRate()+equipment.getCriticalRate());
        return iEquipment;
    }

    /**
     * 卸下物品
     *
     * @param equipment 装备
     */
    public void unEquipment(IEquipment equipment) {
        propetry.setMagicTotalVolume(propetry.getMagicTotalVolume()-equipment.getMagicTotalVolume());
        propetry.setBloodTotalVolume(propetry.getBloodTotalVolume()-equipment.getBloodTotalVolume());
        propetry.setAttackPower(propetry.getAttackPower()-equipment.getAttackPower());
        propetry.setDefensivePower(propetry.getDefensivePower() - equipment.getDefensivePower());
        propetry.setSpeed(propetry.getSpeed() - equipment.getSpeed());
        propetry.setCriticalDamage(propetry.getCriticalDamage() - equipment.getCriticalDamage());
        propetry.setCriticalRate(propetry.getCriticalRate() - equipment.getCriticalRate());
    }

    private String[] getPropetryString() {
        return new String[] {
                String.format("人物等级: %d", mPlayerMediator.getPlayerRank()),
                String.format("攻击力: %d", mPlayerMediator.getPlayerAttackPower()),
                String.format("防御力: %d", mPlayerMediator.getPlayerDefensivePower()),
                String.format("血量: %d", mPlayerMediator.getPlayerBloodTotalVolume()),
                String.format("魔量: %d", mPlayerMediator.getPlayerMagicTotalVolume()),
                String.format("速度: %.1f", mPlayerMediator.getPlayerSpeed()),
                String.format("暴击率: %.1f", mPlayerMediator.getPlayerCriticalRate()),
                String.format("暴击伤害: %d", (int) mPlayerMediator.getPlayerCriticalDamage()),
        };
    }

    public static class ControlParam {
        public ControlParam(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            iEquipment = null;
        }

        public float x;
        public float y;
        public float width;
        public float height;
        public IEquipment iEquipment;
    }

    private ArrayList<RectF> controls;
    private static ControlParam[] controlParams = new ControlParam[]{
            ///844 255
            ///110 115
            new ControlParam(0.0651f, 0.2725f, 0.13f, 0.45f),      //55,69.5
            new ControlParam(0.2132f, 0.2725f, 0.13f, 0.45f),      //180,69.5
            new ControlParam(0.7914f, 0.2725f, 0.13f, 0.45f),      //668,69.5
            new ControlParam(0.9372f, 0.2725f, 0.13f, 0.45f),      //791,69.5

            new ControlParam(0.0651f, 0.7745f, 0.13f, 0.45f),      //55,197.5
            new ControlParam(0.2132f, 0.7745f, 0.13f, 0.45f),      //180,197.5
            new ControlParam(0.7914f, 0.7745f, 0.13f, 0.45f),      //668,197.5
            new ControlParam(0.9372f, 0.7745f, 0.13f, 0.45f),      //791,197.5
            new ControlParam(0.5059f, 0.50588f, 0.372f, 0.87f),      //427,129,314,222     中间信息框
    };

    public void init() {
        mBitmap = ImageLoader.getInstance().loadBitmap(GameConstants.EQUITMENT_BAR_NAME);
        mSelectBitmap = ImageLoader.getInstance().loadBitmap(GameConstants.INVENTORY_SELECTED_NAME);
        ///宽度是屏幕宽度的0.15倍
        width = (int) (GameConstants.sGameWidth * 0.6f);
        height = (int) (GameConstants.sGameHeight * 0.3f);
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
            mTextPaint = new TextPaint();
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
        if (control >= EQUIP_BODY && control <= EQUIP_NECKLACE) {
            //Toast.makeText(AdventureApplication.getContextObj(),String.format("u pressed %d", control),Toast.LENGTH_SHORT).show();
            if (mSelectIndex == control) {
                mSelectIndex = INVALIDATE_SELECT_INDEX;
            } else {
                mSelectIndex = control;
            }
        } else if (control == PROPETRY_PANEL) {
            mSelectIndex = INVALIDATE_SELECT_INDEX;
        }
        return true;
    }

    private int ptInRegion(int x, int y) {
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
        //super.draw(canvas);
        if (!isVisiable()) {
            return;
        }

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

        ///画选中器
        if (mSelectIndex != INVALIDATE_SELECT_INDEX) {
            canvas.drawBitmap(mSelectBitmap,
                    new Rect(   ///src rect
                            0,
                            0,
                            mSelectBitmap.getWidth(),
                            mSelectBitmap.getHeight()),
                    controls.get(mSelectIndex), null);
        }

        ///画装备
        mReentrantReadWriteLock.readLock().lock();
        for (ControlParam controlParam : controlParams) {
            if (controlParam.iEquipment != null) {
                Bitmap bitmap = ImageLoader.getInstance().loadBitmap(controlParam.iEquipment.getIcon());
                canvas.drawBitmap(bitmap,
                        new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), controls.get(controlParam.iEquipment.getEquipLocation())
                        , null);
            }
        }
        mReentrantReadWriteLock.readLock().unlock();

        ///画属性
        if (mSelectIndex != INVALIDATE_SELECT_INDEX) {
            ///画武器属性
            IEquipment iEquipment = controlParams[mSelectIndex].iEquipment;
            if (iEquipment != null) {
                //String[] strings = iEquipment.getDescription();

                drawPropetry(canvas, iEquipment.getDescription());
            }
        } else {
            ///画总属性
            drawPropetry(canvas,getPropetryString());
        }
    }

    private void drawPropetry(Canvas canvas,String[] strings) {
        float topY = controls.get(PROPETRY_PANEL).top;
        for (String string : strings) {
            if (string == null) {
                continue;
            }
            StaticLayout mTextLayout = new StaticLayout(string,
                    mTextPaint, (int) controls.get(PROPETRY_PANEL).width(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            canvas.save();
            canvas.translate(controls.get(PROPETRY_PANEL).left, topY);
            mTextLayout.draw(canvas);
            canvas.restore();
            topY += (mFontMetricsInt.bottom - mFontMetricsInt.top);
        }
    }
}
