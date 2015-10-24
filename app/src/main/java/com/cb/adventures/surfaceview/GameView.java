package com.cb.adventures.surfaceview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.cb.adventures.constants.GameConstants;
import com.cb.adventures.controller.MonsterController;
import com.cb.adventures.factory.SimpleMonsterFactory;
import com.cb.adventures.utils.ImageLoader;
import com.cb.adventures.view.GameController;
import com.cb.adventures.view.Player;
import com.cb.adventures.view.ScrollBackground;
import com.cb.adventures.view.Sprite;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Stack;

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

            MonsterController.getInstance().setmMonsterFactory(new SimpleMonsterFactory());
            MonsterController.getInstance().generateMonster(GameConstants.BLACK_PIG_ID, 5);

            if (mGameController == null) {
                mGameController = new GameController();
                mGameController.init();
                mGameController.setmOnControllerListener(this);
            }

            if (scrollBackground == null) {
                scrollBackground = new ScrollBackground();
                Bitmap bitmap1 = ImageLoader.getmInstance().loadBitmap("back3.jpg");
                Bitmap bitmap2 = ImageLoader.getmInstance().loadBitmap("back3.jpg");
                scrollBackground.init(bitmap1, bitmap2, getWidth(), getHeight());
            }
            if (player == null) {
                player = new Player();
                Bitmap bitmap = ImageLoader.getmInstance().loadBitmap("xunlei.png");
                Bitmap attackBitmap = ImageLoader.getmInstance().loadBitmap("attack.png");
                player.init(bitmap, 9, 946 / 9, 420 / 4, 1, 2,
                        attackBitmap, 1152 / 6, 1344 / 7, 6);
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
    }

    private void drawGame(Canvas canvas) {
        scrollBackground.draw(canvas);
        player.draw(canvas);
        MonsterController.getInstance().draw(canvas);
        mGameController.draw(canvas);
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
        player.attack();
        scrollBackground.stopScroll();
    }

    @Override
    public void onStop() {
        player.stop();
        scrollBackground.stopScroll();
    }
}
