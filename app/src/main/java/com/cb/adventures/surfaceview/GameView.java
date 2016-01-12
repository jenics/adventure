package com.cb.adventures.surfaceview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.cb.adventures.animation.IAnimation;
import com.cb.adventures.animation.AnimationControl;
import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.controller.MonsterController;
import com.cb.adventures.data.GameData;
import com.cb.adventures.engine.GameEngine;
import com.cb.adventures.factory.SkillFactory;
import com.cb.adventures.prop.IUsable;
import com.cb.adventures.view.PlayerMediator;
import com.cb.adventures.skill.Skill;
import com.cb.adventures.skill.SkillLancher;
import com.cb.adventures.utils.CLog;
import com.cb.adventures.utils.ImageLoader;
import com.cb.adventures.utils.WeakRefHandler;
import com.cb.adventures.view.ui.BloodReservoir;
import com.cb.adventures.view.DropPropMgr;
import com.cb.adventures.view.ui.EquipmentBar;
import com.cb.adventures.view.ui.GameController;
import com.cb.adventures.view.ui.GameMenuView;
import com.cb.adventures.view.ui.InventoryView;
import com.cb.adventures.view.Player;
import com.cb.adventures.view.Map;
import com.cb.adventures.view.Sprite;

import java.lang.ref.WeakReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 游戏主view
 * Created by jenics on 2015/10/7.
 * 这个view的生命周期与他以来的activity一样长，写进xml里的
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable, GameController.OnControllerListener, GameMenuView.OnMenuItemClickListener, GameData.OnLoadDataListener, Sprite.OnDeadListener {
    private boolean mIsRunning;
    private boolean mIsLoadFinish;
    private Thread mDrawThread;
    private Thread mWorkThread;

    private SurfaceHolder mSurfaceHolder;

    private GameHandler mGameHandler;

    private GameController mGameController;
    private GameMenuView gameMenuView;
    private BloodReservoir bloodReservoir;
    private EquipmentBar mEquipmentBar;
    private InventoryView mInventoryView;
    private Map mMap;

    private DropPropMgr mDropPropMgr;
    private MonsterController mMonsterController;

    private Bitmap mBitmapLoading;
    private Player mPlayer;
    public static final String TAG = "GameView";
    public static final int LOAD_FINISH = 1;


    @Override
    public void onDead(Sprite sprite) {
        /**
         * 查询掉落列表，根据掉率随机掉出
         */
        mDropPropMgr.drop(sprite.getMonsterPropetry().getDropItems(), sprite.getPt());
        mPlayer.addExp(sprite.getMonsterPropetry().getExp());
    }

    static class GameHandler<GameView> extends WeakRefHandler<GameView> {
        public GameHandler(GameView t) {
            super(t);
        }

        @Override
        public void handleMessage(Message msg) {
            final com.cb.adventures.surfaceview.GameView gameView = (com.cb.adventures.surfaceview.GameView) getReference().get();
            if (gameView != null) {
                switch (msg.what) {
                    case LOAD_FINISH:
                        gameView.initGameData();
                        gameView.mIsLoadFinish = true;
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public GameView(Context context) {
        super(context);
        initSurfaceHolder();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSurfaceHolder();
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSurfaceHolder();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initSurfaceHolder();
    }

    private void initSurfaceHolder() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            init();
            GameEngine.getInstance().resumeGame();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
        GameConstants.sGameWidth = getWidth();
        GameConstants.sGameHeight = getHeight();
        GameConstants.zoomRatio = getWidth() * 0.15f / 105.0f;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        uninit();
        GameEngine.getInstance().pauseGame();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        GameEngine.getInstance().exitGame();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        GameEngine.getInstance().startGame();
    }

    /**
     * 初始化surface view
     */
    private void init() {
        mIsLoadFinish = false;

        if (mGameHandler == null) {
            mGameHandler = new GameHandler(this);
        }
        mBitmapLoading = ImageLoader.getInstance().loadBitmap(GameConstants.LOADING_NAME);

        GameData.getInstance().asyParsAll(this);

        ///开启绘图线程
        mIsRunning = true;
        mDrawThread = new Thread(this);
        mDrawThread.start();
    }

    /**
     * 反初始化surface view
     */
    private void uninit() {
        mIsRunning = false;
        Log.d(TAG,"uninit");
        try {
            if (mDrawThread != null) {
                mDrawThread.join();
            }
            if (mWorkThread != null) {
                mWorkThread.join();
            }
        } catch (InterruptedException e) {
            Log.e(TAG, e.toString());
        }

        mDropPropMgr.clearProps();
    }

    private void initGameData() {
        PlayerMediator playerMediator = new PlayerMediator();
        if (mPlayer == null) {
            mPlayer = new Player(playerMediator);
            mPlayer.init(ImageLoader.getInstance().loadBitmap(GameConstants.PLAYER1_NAME),
                    9, 946 / 9, 420 / 4, 1, 2,
                    ImageLoader.getInstance().loadBitmap(GameConstants.PLAYER1_ATTACK_NAME),
                    1151 / 6, 103, 6);
            ///数据库中读取等级，得到对应的基础属性
            mPlayer.setRank(1);
        }
        if (mInventoryView == null) {
            mInventoryView = new InventoryView(playerMediator);
            mInventoryView.init();
            mInventoryView.setIsVisiable(false);
        }

        if (mEquipmentBar == null) {
            mEquipmentBar = new EquipmentBar(playerMediator);
            mEquipmentBar.init();
            mEquipmentBar.setIsVisiable(false);
        }

        /**
         * 载入关卡数据
         */
        if (mMap == null) {
            mMap = new Map(playerMediator);
        }

        playerMediator.setEquipmentBar(mEquipmentBar);
        playerMediator.setInventoryView(mInventoryView);
        playerMediator.setPlayer(mPlayer);
        playerMediator.setMap(mMap);


        if (mMonsterController == null) {
            mMonsterController = new MonsterController();
            mMonsterController.setDeadListener(this);
        }


        if (mGameController == null) {
            mGameController = new GameController();
            mGameController.init();
            mGameController.setOnControllerListener(this);

            mGameController.getFunctionController(0).setUseable(new SkillLancher(mPlayer, GameData.getInstance().getSkillPropetry(GameConstants.SKILL_ID_BINGHJIAN)));
            mGameController.getFunctionController(1).setUseable(new SkillLancher(mPlayer, GameData.getInstance().getSkillPropetry(GameConstants.SKILL_ID_BUFF_1)));
            mGameController.getFunctionController(2).setUseable(new SkillLancher(mPlayer, GameData.getInstance().getSkillPropetry(GameConstants.SKILL_ID_HUOJIAN)));
            mGameController.getFunctionController(3).setUseable(new SkillLancher(mPlayer, GameData.getInstance().getSkillPropetry(GameConstants.SKILL_ID_RENDAOFEIBIAO)));
            mPlayer.setOnAttackListener(mGameController);
        }


        if (bloodReservoir == null) {
            bloodReservoir = new BloodReservoir(mPlayer);
            bloodReservoir.init();
        }

        if (mDropPropMgr == null) {
            mDropPropMgr = new DropPropMgr(mInventoryView);
        }

        if (gameMenuView == null) {
            gameMenuView = new GameMenuView();
            gameMenuView.init();
            gameMenuView.setMenuListener(this);
        }

        /**
         * 加入观察者到map类中
         */
        mMap.addObserver(mDropPropMgr);
        mMap.addObserver(mMonsterController);
        mMap.nextGate(2);

        ///开启工作线程
        mWorkThread = new Thread(new WorkRunnable(this));
        mWorkThread.start();
    }


    @Override
    public void run() {
        while (mIsRunning) {
            //在这里加上线程安全锁
            long startTime = System.currentTimeMillis();

            /**拿到当前画布 然后锁定**/
            synchronized (mSurfaceHolder) {
                Canvas canvas = mSurfaceHolder.lockCanvas();
                if (canvas == null)
                    continue;
                if (!mIsLoadFinish) {
                    drawLoading(canvas);
                } else {
                    logicAnimate();
                    drawGame(canvas);
                }
                /**绘制结束后解锁显示在屏幕上**/
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
            long endTime = System.currentTimeMillis();
            /**计算出游戏一次更新的毫秒数**/
            int diffTime = (int) (endTime - startTime);

            /**确保每次更新时间为30帧**/
            while (diffTime <= 20) {
                diffTime = (int) (System.currentTimeMillis() - startTime);
                /**线程等待**/
                Thread.yield();
            }
        }
    }

    private void logicAnimate() {
        AnimationControl.getInstance().animate();
        mMonsterController.animate();
        mMap.scroll();
    }

    /**
     * 碰撞检测
     */
    private void collisionDetection() {
        /**
         * 碰撞检测，涉及到玩家与技能的碰撞，怪物与玩家释放技能的碰撞。
         */
        ReentrantReadWriteLock animationLock = AnimationControl.getInstance().getReentrantReadWriteLock();
        animationLock.writeLock().lock();
        for (IAnimation viewAnimation : AnimationControl.getInstance().getQueueAnimaion()) {
            /**
             * 查询是否是技能
             */
            if (viewAnimation instanceof Skill) {
                //if(viewAnimation.getView() instanceof Skill) {
                Skill skill = (Skill) viewAnimation;
                if (skill.getSkillPropetry().getSkillType() == GameConstants.SKILL_TYPE_ACTIVE_ATTACK) {

                    /**
                     * 该技能已作用过伤害，不再继续产生伤害
                     */
                    if (skill.isHurted()) {
                        continue;
                    }

                    if (skill.getCast() == GameConstants.CAST_PLAYER) {
                        /**
                         * 遍历怪物，看击中了哪个怪物
                         * 怪物顺便与玩家做碰撞测试，看是否撞到了玩家
                         */
                        ReentrantReadWriteLock monsterLock = mMonsterController.getReentrantReadWriteLock();
                        monsterLock.readLock().lock();
                        for (Sprite sprite : mMonsterController.getMonters()) {
                            if (judgeIntersect(
                                    new RectF(skill.getPt().x - skill.getWidth() / 2,
                                            skill.getPt().y - skill.getHeight() / 2,
                                            skill.getPt().x + skill.getWidth() / 2,
                                            skill.getPt().y + skill.getHeight() / 2),
                                    new RectF(sprite.getPt().x - sprite.getWidth() / 2,
                                            sprite.getPt().y - sprite.getHeight() / 2,
                                            sprite.getPt().x + sprite.getWidth() / 2,
                                            sprite.getPt().y + sprite.getHeight() / 2)) && !sprite.isDead()) {

                                sprite.onHurted(skill);
                                if (skill.getSkillPropetry().isInterruptWhileHit()) {
                                    skill.stopSkill();
                                    break;
                                }
                            }
                        }
                        monsterLock.readLock().unlock();
                    } else if (skill.getCast() == GameConstants.CAST_MONSTER) {
                        /**
                         * 与玩家作碰撞检测
                         */
                    } else {
                        CLog.w("logicAnimate", "no definition cast");
                    }
                }
            }
        }
        animationLock.writeLock().unlock();

        /**
         * 玩家与怪物的碰撞检测
         */
        ReentrantReadWriteLock reentrantReadWriteLock = mMonsterController.getReentrantReadWriteLock();
        reentrantReadWriteLock.readLock().lock();
        for (Sprite sprite : mMonsterController.getMonters()) {

            if (judgeIntersect(
                    new RectF(mPlayer.getPt().x - mPlayer.getWidth() / 2,
                            mPlayer.getPt().y - mPlayer.getHeight() / 2,
                            mPlayer.getPt().x + mPlayer.getWidth() / 2,
                            mPlayer.getPt().y + mPlayer.getHeight() / 2),
                    new RectF(sprite.getPt().x - sprite.getWidth() / 2,
                            sprite.getPt().y - sprite.getHeight() / 2,
                            sprite.getPt().x + sprite.getWidth() / 2,
                            sprite.getPt().y + sprite.getHeight() / 2)) && !sprite.isDead()) {
                Skill skill = new SkillFactory().create(GameConstants.SKILL_ID_MONSTER_NORMAL);
                skill.getSkillPropetry().setExtraAttack(sprite.getMonsterPropetry().getAttackPower());
                mPlayer.onHurted(skill);
                break;
            }
        }
        reentrantReadWriteLock.readLock().unlock();

        /**
         * 捡起检测
         */
        mDropPropMgr.pickUp(mPlayer.getPt());
    }

    private boolean judgeIntersect(RectF rect1, RectF rect2) {
        float x1 = rect1.left;
        float y1 = rect1.top;
        float x2 = rect1.right;
        float y2 = rect1.bottom;
        float x3 = rect2.left;
        float y3 = rect2.top;
        float x4 = rect2.right;
        float y4 = rect2.bottom;
        if (Math.abs((x1 + x2) / 2 - (x3 + x4) / 2) >= (Math.abs(x1 - x2) + Math.abs(x3 - x4)) / 2) {
            return false;
        } else if (Math.abs((y1 + y2) / 2 - (y3 + y4) / 2) >= (Math.abs(y1 - y2) + Math.abs(y3 - y4)) / 2) {
            return false;
        }
        return true;
    }

    private void drawLoading(Canvas canvas) {
        float disX = (GameConstants.sGameWidth - mBitmapLoading.getWidth()) / 2;
        float disY = (GameConstants.sGameHeight - mBitmapLoading.getHeight()) / 2;
        canvas.drawBitmap(mBitmapLoading,
                new Rect(
                        0,
                        0,
                        mBitmapLoading.getWidth(),
                        mBitmapLoading.getHeight()
                ), new RectF(
                        disX,
                        disY,
                        disX + mBitmapLoading.getWidth(),
                        disY + mBitmapLoading.getHeight()
                ), null);
    }

    private void drawGame(Canvas canvas) {
        mMap.draw(canvas);
        mMonsterController.draw(canvas);
        mPlayer.draw(canvas);
        AnimationControl.getInstance().draw(canvas);
        ///画掉落的物品
        mDropPropMgr.draw(canvas);
        mGameController.draw(canvas);
        bloodReservoir.draw(canvas);
        mInventoryView.draw(canvas);
        mEquipmentBar.draw(canvas);
        if (gameMenuView.isVisiable()) {
            gameMenuView.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsLoadFinish) {
            return touchDetection(event);
        }
        return true;
    }

    private boolean touchDetection(MotionEvent event) {

        for (int i = 0; i < event.getPointerCount(); ++i) {
            /**
             * 不管是不是第一次按下，都去判断下mose down。
             */
            if (gameMenuView.isVisiable()) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (gameMenuView.onMouseUp((int) event.getX(i), (int) event.getY(i)))
                        break;
                }
            }
            if (mInventoryView.isVisiable()) {
                /**
                 * 只要物品栏出现，就接管所有消息。
                 */
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mInventoryView.onMouseUp((int) event.getX(i), (int) event.getY(i));
                }
            } else if (mEquipmentBar.isVisiable()) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mEquipmentBar.onMouseUp((int) event.getX(i), (int) event.getY(i));
                }
            } else {
                if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN ||
                        (event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_POINTER_DOWN) {
                    mGameController.onMouseDown((int) event.getX(i), (int) event.getY(i));
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    mGameController.onMouseMove((int) event.getX(i), (int) event.getY(i));
                } else if (event.getAction() == MotionEvent.ACTION_UP
                        || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    mGameController.onMouseUp((int) event.getX(i), (int) event.getY(i));
                }
            }
        }
        return true;
    }

    @Override
    public void onDirectionChange(int direction) {
        if (mPlayer.move(direction)) {
            mMap.scrollTo(direction);
        }
    }


    @Override
    public void onAttack() {
        ///判断下是否到了地图入口与出口


        if (mMap.getPreGate() != null && mPlayer.getPt().x > mMap.getPreGate().getPt().x - mMap.getPreGate().getWidth() / 2
                && mPlayer.getPt().x < mMap.getPreGate().getPt().x + mMap.getPreGate().getWidth() / 2) {
            mMap.preGate();
        } else if (mMap.getNextGate() != null && mPlayer.getPt().x > mMap.getNextGate().getPt().x - mMap.getNextGate().getWidth() / 2
                && mPlayer.getPt().x < mMap.getNextGate().getPt().x + mMap.getNextGate().getWidth() / 2) {
            mMap.nextGate();
        } else {
            mPlayer.attack(GameConstants.SKILL_ID_NORMAL);
        }
    }

    @Override
    public void onStop() {
        mPlayer.stop();
        mMap.stopScroll();
    }

    @Override
    public void onFunction(int index) {
        IUsable useable = mGameController.getFunctionController(index).getUseable();
        if (useable != null) {
            useable.use();
        }
    }

    @Override
    public void onItemClick(int itemIndex) {
        if (itemIndex == GameMenuView.MENU_ITEM_INVENTORY) {
            boolean isVisiable = mInventoryView.isVisiable();
            mInventoryView.setIsVisiable(!isVisiable);
            mEquipmentBar.setIsVisiable(false);
        } else if (itemIndex == GameMenuView.MENU_ITEM_PROPETRY) {
            boolean isVisiable = mEquipmentBar.isVisiable();
            mEquipmentBar.setIsVisiable(!isVisiable);
            mInventoryView.setIsVisiable(false);
        }
    }

    @Override
    public void onLoadFinish(GameData.LoadStepEnum step) {
        if (step == GameData.LoadStepEnum.STEP_END) {
            mGameHandler.sendEmptyMessage(LOAD_FINISH);
        }
    }

    @Override
    public void onLoadFailed(GameData.LoadStepEnum step) {

    }

    private static class WorkRunnable implements Runnable {
        WeakReference<GameView> mReference;

        public WorkRunnable(GameView view) {
            mReference = new WeakReference<>(view);
        }

        @Override
        public void run() {
            GameView gameView = mReference.get();
            if (gameView == null) {
                CLog.e("GameView", "work thread error,gameview is null");
                return;
            }
            while (gameView.mIsRunning) {
                long startTime = System.currentTimeMillis();

                gameView.collisionDetection();

                long endTime = System.currentTimeMillis();
                /**计算出游戏一次更新的毫秒数**/
                int diffTime = (int) (endTime - startTime);

                /**确保每次更新时间为30帧**/
                while (diffTime <= 20) {
                    diffTime = (int) (System.currentTimeMillis() - startTime);
                    /**线程等待**/
                    Thread.yield();
                }
            }
        }
    }
}
