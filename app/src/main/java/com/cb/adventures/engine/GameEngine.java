package com.cb.adventures.engine;

import android.graphics.Canvas;

import com.cb.adventures.animation.AnimationControl;
import com.cb.adventures.music.MusicManager;
import com.cb.adventures.utils.ImageLoader;
import com.cb.adventures.view.Player;
import com.cb.adventures.view.Sprite;

/**
 * Created by jenics on 2015/12/27.
 */
public class GameEngine implements IEngine{
    private static GameEngine mInstance;
    public synchronized static GameEngine getInstance() {
        if (mInstance == null) {
            mInstance = new GameEngine();
        }
        return mInstance;
    }
    @Override
    public void startGame() {
        MusicManager.getInstance().init();
    }

    @Override
    public void exitGame() {
        AnimationControl.getInstance().clear();
        ImageLoader.getInstance().destoryAll();
        MusicManager.getInstance().release();
    }

    @Override
    public void pauseGame() {
        MusicManager.getInstance().pauseMedia(MusicManager.STATIC_MEDIA_TYPE_BACKGROUND);
    }

    @Override
    public void resumeGame() {
        MusicManager.getInstance().playMedia(MusicManager.STATIC_MEDIA_TYPE_BACKGROUND);
    }
}
