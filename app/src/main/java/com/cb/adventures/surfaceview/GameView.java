package com.cb.adventures.surfaceview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.cb.adventures.animation.AnimationControl;
import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.controller.MonsterController;
import com.cb.adventures.data.GameData;
import com.cb.adventures.data.MapPropetry;
import com.cb.adventures.data.SkillPropetry;
import com.cb.adventures.factory.SimpleMonsterFactory;
import com.cb.adventures.utils.ImageLoader;
import com.cb.adventures.view.BloodReservoir;
import com.cb.adventures.view.GameController;
import com.cb.adventures.view.Player;
import com.cb.adventures.view.ScrollBackground;

/**
 * 游戏主view
 * Created by jenics on 2015/10/7.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable, GameController.OnControllerListener {
    private boolean mIsRunning;
    private Thread mThread;
    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;
    private ScrollBackground scrollBackground;
    private Player player;
    private GameController mGameController;
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
            GameConstants.zoomRatio = getWidth()*0.15f/105.0f;

            GameData.getInstance().synParseSkills();
            GameData.getInstance().synParseMonsters();
            GameData.getInstance().synParseMaps();



            if (player == null) {
                player = new Player();
                Bitmap bitmap = ImageLoader.getmInstance().loadBitmap("xunlei.png");
                Bitmap attackBitmap = ImageLoader.getmInstance().loadBitmap("attack.png");
                player.init(bitmap, 9, 946 / 9, 420 / 4, 1, 2,
                        attackBitmap, 1152 / 6, 1344 / 7, 6);
                player.getmPropetry().setBloodTotalVolume(100);
                player.getmPropetry().setBloodVolume(70);
                player.getmPropetry().setMagicTotalVolume(100);
                player.getmPropetry().setMagicVolume(25);
                player.getmPropetry().setSpeed(12);
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

            /**
             * 载入关卡数据
             */
            if (scrollBackground == null) {
                scrollBackground = new ScrollBackground();
                scrollBackground.init(GameData.getInstance().getMapPropetry(1), getWidth(), getHeight(),player);

                MonsterController.getInstance().setmMonsterFactory(new SimpleMonsterFactory());
                MapPropetry mapPropetry = scrollBackground.getMapPropetry();
                for(MapPropetry.MonsterPack pack : mapPropetry.getMonsterPaks()) {
                    MonsterController.getInstance().generateMonster(pack.getMonsterId(),pack.getMonsterNum());
                }
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

    private void drawGame(Canvas canvas) {
        scrollBackground.draw(canvas);
        MonsterController.getInstance().draw(canvas);
        player.draw(canvas);
        AnimationControl.getInstance().draw(canvas);
        mGameController.draw(canvas);
        bloodReservoir.draw(canvas);
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
        return true;
    }

    @Override
    public void onDirectionChange(int direction) {
        if (player.move(direction)) {
            scrollBackground.scrollTo(direction);
        }
    }

    @Override
    public void onAttack() {
        player.attack(GameConstants.SKILL_ID_NORMAL);
        scrollBackground.stopScroll();
    }

    @Override
    public void onStop() {
        player.stop();
        scrollBackground.stopScroll();
    }

    @Override
    public void onFunction(int index) {
        if (mGameController.getFunctionController(index).getType() == GameConstants.FUNCTION_TYPE_CONSUMABLE) {
            ///使用消耗品
        }else if(mGameController.getFunctionController(index).getType() == GameConstants.FUNCTION_TYPE_SKILL) {
            ///使用技能
            SkillPropetry skillPropetry = mGameController.getFunctionController(index).getSkillPropetry();
            if(skillPropetry != null) {
                player.attack(skillPropetry.getSkillId());
            }
        }
    }
}
