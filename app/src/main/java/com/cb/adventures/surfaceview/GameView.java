package com.cb.adventures.surfaceview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ActionMenuView;

import com.cb.adventures.animation.Animation;
import com.cb.adventures.animation.AnimationControl;
import com.cb.adventures.animation.AnimationProxy;
import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.controller.MonsterController;
import com.cb.adventures.data.GameData;
import com.cb.adventures.data.SkillPropetry;
import com.cb.adventures.factory.SkillFactory;
import com.cb.adventures.skill.Skill;
import com.cb.adventures.utils.CLog;
import com.cb.adventures.utils.ImageLoader;
import com.cb.adventures.view.BloodReservoir;
import com.cb.adventures.view.GameController;
import com.cb.adventures.view.GameMenuView;
import com.cb.adventures.view.InventoryView;
import com.cb.adventures.view.Player;
import com.cb.adventures.view.Map;
import com.cb.adventures.view.Sprite;

/**
 * 游戏主view
 * Created by jenics on 2015/10/7.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable, GameController.OnControllerListener ,GameMenuView.OnMenuItemClickListener {
    private boolean mIsRunning;
    private Thread mThread;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;
    private Map map;
    private Player player;
    private GameController mGameController;
    /**
     * 物品栏控件
     */
    private InventoryView inventoryView;
    private GameMenuView gameMenuView;
    private BloodReservoir bloodReservoir;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mIsRunning = false;
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);

        if (mPaint == null)
            mPaint = new Paint();
        mPaint.setAntiAlias(true);  ///抗锯齿
        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mPaint.setTypeface(font);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Configuration mConfiguration = this.getResources().getConfiguration(); //获取设置的配置信息
        int ori = mConfiguration.orientation; //获取屏幕方向
        if (ori == mConfiguration.ORIENTATION_LANDSCAPE) {

            GameConstants.sLeftBoundary = 0;
            GameConstants.sRightBoundary = getWidth();
            GameConstants.sGameWidth = getWidth();
            GameConstants.sGameHeight = getHeight();
            GameConstants.zoomRatio = getWidth() * 0.15f / 105.0f;

            GameData.getInstance().synParseAnimations();
            GameData.getInstance().synParseSkills();
            GameData.getInstance().synParseMonsters();
            GameData.getInstance().synParseMaps();


            if (player == null) {
                player = new Player();
                Bitmap bitmap = ImageLoader.getmInstance().loadBitmap(GameConstants.PLAYER1_NAME);
                Bitmap attackBitmap = ImageLoader.getmInstance().loadBitmap(GameConstants.PLAYER1_ATTACK_NAME);
                player.init(bitmap, 9, 946 / 9, 420 / 4, 1, 2,
                        attackBitmap, 1151 / 6, 103, 6);
                player.getmPropetry().setBloodTotalVolume(100);
                player.getmPropetry().setBloodVolume(70);
                player.getmPropetry().setMagicTotalVolume(100);
                player.getmPropetry().setMagicVolume(25);
                player.getmPropetry().setSpeed(16);
            }


            if (mGameController == null) {
                mGameController = new GameController();
                mGameController.init();
                mGameController.setmOnControllerListener(this);

                mGameController.getFunctionController(0).setSkillPropetry(GameData.getInstance().getSkillPropetry(GameConstants.SKILL_ID_BINGHJIAN));
                mGameController.getFunctionController(1).setSkillPropetry(GameData.getInstance().getSkillPropetry(GameConstants.SKILL_ID_BUFF_1));
                mGameController.getFunctionController(2).setSkillPropetry(GameData.getInstance().getSkillPropetry(GameConstants.SKILL_ID_HUOJIAN));
                mGameController.getFunctionController(3).setSkillPropetry(GameData.getInstance().getSkillPropetry(GameConstants.SKILL_ID_RENDAOFEIBIAO));
            }


            if (bloodReservoir == null) {
                bloodReservoir = new BloodReservoir();
                bloodReservoir.init();
                bloodReservoir.setPropetry(player.getmPropetry());
            }

            if (inventoryView == null) {
                inventoryView = new InventoryView();
                inventoryView.init();
                inventoryView.setIsVisiable(false);
            }

            if (gameMenuView == null) {
                gameMenuView = new GameMenuView();
                gameMenuView.init();
                gameMenuView.setIsVisiable(true);
                gameMenuView.setListener(this);
            }

            /**
             * 载入关卡数据
             */
            if (map == null) {
                map = new Map();
                map.init(2, getWidth(), getHeight(), player);
            }


            mIsRunning = true;
            mThread = new Thread(this);
            mThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

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
                logicAnimate();
                collisionDetection();
                drawGame(canvas);
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
        MonsterController.getInstance().animate();
        AnimationControl.getInstance().animate();

    }

    /**
     * 碰撞检测
     */
    private void collisionDetection() {
        /**
         * 碰撞检测，涉及到玩家与技能的碰撞，怪物与玩家释放技能的碰撞。
         */
        for (Animation animation : AnimationControl.getInstance().getQueueAnimaion()) {
            /**
             * 查询是否是技能
             */
            if (animation instanceof AnimationProxy) {
                if(animation.getView() instanceof Skill) {
                    Skill skill = (Skill) animation.getView();
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
                            for (Sprite sprite : MonsterController.getInstance().getMonters()) {
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
        }

        /**
         * 玩家与怪物的碰撞检测
         */
        for (Sprite sprite : MonsterController.getInstance().getMonters()) {
            if (judgeIntersect(
                    new RectF(player.getPt().x - player.getWidth() / 2,
                            player.getPt().y - player.getHeight() / 2,
                            player.getPt().x + player.getWidth() / 2,
                            player.getPt().y + player.getHeight() / 2),
                    new RectF(sprite.getPt().x - sprite.getWidth() / 2,
                            sprite.getPt().y - sprite.getHeight() / 2,
                            sprite.getPt().x + sprite.getWidth() / 2,
                            sprite.getPt().y + sprite.getHeight() / 2)) && !sprite.isDead()) {
                Skill skill = new SkillFactory().create(GameConstants.SKILL_ID_MONSTER_NORMAL);
                skill.getSkillPropetry().setExtraAttack(sprite.getMonsterPropetry().getAttackPower());
                player.onHurted(skill);
            }
        }
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


    private void drawGame(Canvas canvas) {
        map.draw(canvas);
        MonsterController.getInstance().draw(canvas);
        player.draw(canvas);
        AnimationControl.getInstance().draw(canvas);
        mGameController.draw(canvas);
        bloodReservoir.draw(canvas);
        if (inventoryView.isVisiable()) {
            inventoryView.draw(canvas);
        }

        if (gameMenuView.isVisiable()) {
            gameMenuView.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return touchDetection(event);
    }

    private boolean touchDetection(MotionEvent event) {

        for (int i = 0; i < event.getPointerCount(); ++i) {
            /**
             * 不管是不是第一次按下，都去判断下mose down。
             */
            if (inventoryView.isVisiable()) {
                /**
                 * 只要物品栏出现，就接管所有消息。
                 */
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    inventoryView.onMouseUp((int) event.getX(i), (int) event.getY(i));
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

            if (gameMenuView.isVisiable()) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    gameMenuView.onMouseUp((int) event.getX(i), (int) event.getY(i));
                }
            }

        }
        return true;
    }

    @Override
    public void onDirectionChange(int direction) {
        if (player.move(direction)) {
            map.scrollTo(direction);
        }
    }

    @Override
    public void onAttack() {
        ///判断下是否到了地图入口与出口
        if(map.getPreGate() != null && player.getPt().x > map.getPreGate().getPt().x-map.getPreGate().getWidth()/2
                && player.getPt().x < map.getPreGate().getPt().x+map.getPreGate().getWidth()/2) {
            map.preGate();
        }else if (map.getNextGate() != null && player.getPt().x > map.getNextGate().getPt().x-map.getNextGate().getWidth()/2
                && player.getPt().x < map.getNextGate().getPt().x+map.getNextGate().getWidth()/2) {
            map.nextGate();
        }else {
            player.attack(GameConstants.SKILL_ID_NORMAL);
            map.stopScroll();
        }
    }

    @Override
    public void onStop() {
        player.stop();
        map.stopScroll();
    }

    @Override
    public void onFunction(int index) {
        if (mGameController.getFunctionController(index).getType() == GameConstants.FUNCTION_TYPE_CONSUMABLE) {
            ///使用消耗品
        } else if (mGameController.getFunctionController(index).getType() == GameConstants.FUNCTION_TYPE_SKILL) {
            ///使用技能
            SkillPropetry skillPropetry = mGameController.getFunctionController(index).getSkillPropetry();
            if (skillPropetry != null) {
                player.attack(skillPropetry.getSkillId());
                map.stopScroll();
            }
        }
    }

    @Override
    public void onItemClick(int itemIndex) {
        if (itemIndex == GameMenuView.MENU_ITEM_INVENTORY) {
            inventoryView.setIsVisiable(!inventoryView.isVisiable());
        }
    }
}
