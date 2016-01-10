package com.cb.adventures.view;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import com.cb.adventures.animation.InjuredValueAnimation;
import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.data.DropItem;
import com.cb.adventures.data.GameData;
import com.cb.adventures.data.MonsterPropetry;
import com.cb.adventures.factory.SkillFactory;
import com.cb.adventures.skill.Skill;
import com.cb.adventures.utils.CLog;
import com.cb.adventures.utils.ImageLoader;
import com.cb.adventures.utils.Randomer;

import java.util.LinkedList;

/**
 * Created by jenics on 2015/10/7.
 * Sprite类最简单，只管理怪物左右跑。
 */
public class Sprite extends FrameView implements IHurtable {

    protected OnSpriteListener mSpriteListener;
    protected int mDirection;
    private static int sIdNum = 0;
    protected int mId;          ///该怪物唯一标识符，自动生成，自动递增
    protected int mPerWidth;    ///每一帧宽
    protected int mPerHeight;   ///每一帧高
    protected int mFrameIndex;  ///当前帧
    protected long lastTime;        ///时间间隔控制帧的切换
    protected boolean mIsRest;      ///是否休息
    protected boolean mIsStop;      ///是否停止，和休息有区别
    protected int mFrameInterval;        ///切换帧的时间间隔
    protected float mMoveStep;         ///移动时的步长
    protected long restBeginTime;       ///开始休息时间
    protected long workBeginTime;       ///干活开始时间
    protected long mRestTime;           ///休息时间
    protected long mWorkTime;           ///工作时间
    protected Skill mDeadEffect;        ///死亡效果
    protected MonsterPropetry mMonsterPropetry;
    /**
     * 怪物死亡监听
     */
    private OnDeadListener mDeadListener;
    public interface OnDeadListener {
        void onDead(Sprite sprite);
    }

    @Override
    public void onHurted(Skill skill) {
        /**
         * 扣除怪物血量
         */
        boolean isCritical = false;
        int random = Randomer.getInstance().getRandom(1000);
        int rate = (int) (skill.getCriticalRate()*10);
        if (random <= rate) {
            isCritical = true;
        }
        int hurt = skill.getAttackPower() - mMonsterPropetry.getDefensivePower();
        if (isCritical) {
            hurt = (int) (hurt * skill.getCriticalDamage() / 100.0f);
        }
        if (hurt <= 0) {
            ///防御力大于等于攻击力，强制扣除1血
            hurt = 1;
        }

        int mCurrentBlood = mMonsterPropetry.getBloodVolume();
        mCurrentBlood = mCurrentBlood - hurt;
        if (mCurrentBlood <= 0) {
            mCurrentBlood = 0;
            mMonsterPropetry.setBloodVolume(0);
            /**
             * dead,播放死亡动画，掉落物品
             */
            mDeadEffect = new SkillFactory().create(GameConstants.SKILL_ID_DEAD);
            mDeadEffect.setAttachView(this);
            mDeadEffect.startSkill();



            if (mDeadListener != null) {
                mDeadListener.onDead(this);
            }
        }
        mMonsterPropetry.setBloodVolume(mCurrentBlood);

        skill.setIsHurted(true);
        Skill skillEffect = new SkillFactory().create(skill.getSkillPropetry().getHitEffectId());
        skillEffect.setAttachView(this);
        skillEffect.startSkill();

        InjuredValueAnimation injuredValueAnimation = new InjuredValueAnimation(this, -hurt, isCritical);
        injuredValueAnimation.startAnimation();

        rest(400);
    }

    public interface OnSpriteListener {
        void OnRestEnd(int id);

        void OnWorkEnd(int id);
    }

    public void setDeadListener(OnDeadListener mDeadListener) {
        this.mDeadListener = mDeadListener;
    }

    /**
     * 根据等级计算出基础属性
     *
     * @param rank 目标怪物等级
     */
    public void caclBasePropetry(int rank) {
        if ( rank == 0 || rank == mMonsterPropetry.getRank()) {
            return;
        }

        MonsterPropetry level1Propetry = GameData.getInstance().getMonsterPropetry(0);
        int blood = (int) ((mMonsterPropetry.getBloodTotalVolume() - level1Propetry.getBloodTotalVolume())/mMonsterPropetry.getRank() * rank);
        int magic = (int) ((mMonsterPropetry.getMagicTotalVolume()-level1Propetry.getMagicTotalVolume())/mMonsterPropetry.getRank() * rank);
        mMonsterPropetry.setBloodTotalVolume(blood);
        mMonsterPropetry.setBloodVolume(blood);
        mMonsterPropetry.setMagicTotalVolume(magic);
        mMonsterPropetry.setMagicVolume(magic);
        mMonsterPropetry.setAttackPower((int) ((mMonsterPropetry.getAttackPower() - level1Propetry.getAttackPower())) / mMonsterPropetry.getRank() * rank);
        mMonsterPropetry.setDefensivePower((int) ((mMonsterPropetry.getDefensivePower() - level1Propetry.getDefensivePower())) / mMonsterPropetry.getRank() * rank);
        mMonsterPropetry.setExp((mMonsterPropetry.getExp() - level1Propetry.getExp()) / mMonsterPropetry.getRank() * rank);

        ///设置目标怪物等级
        mMonsterPropetry.setRank(rank);
    }



    public MonsterPropetry getMonsterPropetry() {
        return mMonsterPropetry;
    }

    public void setMonsterPropetry(MonsterPropetry mMonsterPropetry) {
        this.mMonsterPropetry = mMonsterPropetry;
    }

    public void onDestory() {
        if (mDeadEffect != null) {
            mDeadEffect.stopSkill();
            mDeadEffect = null;
        }
    }

    public Sprite(MonsterPropetry monsterPropetry) {
        Sprite.sIdNum++;
        mId = sIdNum;
        mBitmap = ImageLoader.getInstance().loadBitmap(monsterPropetry.getSrcInfo().getSrcName());
        mFrameInterval = 100;            ///100ms换一帧
        mPerWidth = mBitmap.getWidth() / monsterPropetry.getSrcInfo().getColFramCont();
        mPerHeight = mBitmap.getHeight() / monsterPropetry.getSrcInfo().getRowFramCount();
        setMonsterPropetry(monsterPropetry);
        mMoveStep = monsterPropetry.getSpeed();
        mIsStop = false;

        width = (int) (mPerWidth * GameConstants.zoomRatio);
        height = (int) (mPerHeight * GameConstants.zoomRatio);
    }

    public void setSpriteListener(OnSpriteListener mSpriteListener) {
        this.mSpriteListener = mSpriteListener;
    }

    @Override
    public Sprite clone() {
        try {
            return (Sprite) super.clone();
        } catch (CloneNotSupportedException e) {
            CLog.e("Sprite", "error in clone");
            System.out.println("Clone failed.");
            return null;
        }
    }

    public int getId() {
        return mId;
    }

    public boolean isDead() {
        return mMonsterPropetry.getBloodVolume() <= 0;
    }

    public long getRestBeginTime() {
        return restBeginTime;
    }

    public long getWorkBeginTime() {
        return workBeginTime;
    }

    /**
     * 休息
     */
    public void rest(long time) {
        mIsRest = true;
        mIsStop = false;
        if (GameConstants.getDirection(mDirection) == GameConstants.DIRECT_LEFT) {
            mDirection = GameConstants.STATE_STOP_LEFT;
        } else {
            mDirection = GameConstants.STATE_STOP_RIGHT;
        }

        mFrameIndex = 0;
        restBeginTime = System.currentTimeMillis();
        mRestTime = time;
    }

    public int getDirection() {
        return GameConstants.getDirection(mDirection);
    }

    /**
     * 干活
     */
    public void work(int direction, long time) {
        if (direction == GameConstants.DIRECT_NONE) {
            //rest();
        } else {
            mIsStop = false;
            mIsRest = false;
            mDirection = direction;
            mFrameIndex = 0;
            workBeginTime = System.currentTimeMillis();
            mWorkTime = time;
        }
    }

    @Override
    public boolean nextFrame() {
        if (!mIsRest && !mIsStop) {
            long nowTime = System.currentTimeMillis();
            if (nowTime - lastTime > mFrameInterval) {
                mFrameIndex++;
                if (mFrameIndex >= mMonsterPropetry.getLeftFrames().size()) {
                    mFrameIndex = 0;
                }
                lastTime = nowTime;
            }
        }
        return true;
    }

    public void move() {
        if (mIsStop) {
            return;
        }
        if (!mIsRest) {
            if (mDirection == GameConstants.STATE_MOVE_LEFT) {
                pt.x -= mMoveStep;
                if (pt.x <= 0) {
                    mDirection = GameConstants.STATE_MOVE_RIGHT;
                }
                long nowTime = System.currentTimeMillis();
                if (nowTime - workBeginTime >= mWorkTime) {
                    stop();
                    workEnd();
                }
            } else if (mDirection == GameConstants.STATE_MOVE_RIGHT) {
                pt.x += mMoveStep;
                if (pt.x >= Map.sMapWidth) {
                    mDirection = GameConstants.STATE_MOVE_LEFT;
                }
                long nowTime = System.currentTimeMillis();
                if (nowTime - workBeginTime >= mWorkTime) {
                    stop();
                    workEnd();
                }
            }
        } else {
            long nowTime = System.currentTimeMillis();
            if (nowTime - restBeginTime >= mRestTime) {
                stop();
                restEnd();
            }
        }
    }

    private void stop() {
        mIsStop = true;
        mFrameIndex = 0;
    }

    private void restEnd() {
        if (mSpriteListener != null) {
            mSpriteListener.OnRestEnd(mId);
        }
    }

    private void workEnd() {
        if (mSpriteListener != null) {
            mSpriteListener.OnWorkEnd(mId);
        }
    }

    public void draw(Canvas canvas) {
        nextFrame();
        float x = getPt().x - width / 2;
        float y = getPt().y - height / 2;

        PointF ptScreem = Map.toScreemPt(new PointF(x,y));
        x = ptScreem.x;
        y = ptScreem.y;


        if (GameConstants.getDirection(mDirection) == GameConstants.DIRECT_LEFT) {
            if (isDead()) {
                /**
                 * 渐隐
                 */
            } else {
                canvas.drawBitmap(mBitmap,
                        new Rect(   ///src rect
                                mPerWidth * mFrameIndex,
                                mMonsterPropetry.getLeftFrames().get(mFrameIndex).getRow() * mPerHeight,
                                mPerWidth * mFrameIndex + mPerWidth,
                                mMonsterPropetry.getLeftFrames().get(mFrameIndex).getRow() * mPerHeight + mPerHeight),
                        new RectF(x,
                                y,
                                x + width,
                                y + height), null);
            }
        } else {
            if (isDead()) {
                /**
                 * 渐隐
                 */
            } else {
                canvas.drawBitmap(mBitmap,
                        new Rect(   ///src rect
                                mPerWidth * mFrameIndex,
                                mMonsterPropetry.getRightFrames().get(mFrameIndex).getRow() * mPerHeight,
                                mPerWidth * mFrameIndex + mPerWidth,
                                mMonsterPropetry.getRightFrames().get(mFrameIndex).getRow() * mPerHeight + mPerHeight),
                        new RectF(x,
                                y,
                                x + width,
                                y + height), null);
            }
        }
    }
}
