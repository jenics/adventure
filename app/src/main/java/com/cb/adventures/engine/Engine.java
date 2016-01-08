package com.cb.adventures.engine;

import android.content.Context;
import android.graphics.Canvas;

import com.cb.adventures.view.Player;
import com.cb.adventures.view.Sprite;

/**
 * Created by jenics on 2015/12/27.
 */
public class Engine implements IEngine ,Sprite.OnDeadListener{
    private static Engine mInstance;
    //private Player mPlayer;
    public synchronized static Engine getInstance() {
        if (mInstance == null) {
            mInstance = new Engine();
        }
        return mInstance;
    }
    @Override
    public void startGame() {
        
    }

    @Override
    public void exitGame() {

    }

    @Override
    public void pauseGame() {

    }

    @Override
    public void resumeGame() {

    }

    @Override
    public void init() {
    }

    @Override
    public void unInit() {

    }

    /**
     * 渲染
     */
    void render(Canvas canvas) {

    }

    @Override
    public void onDead(Sprite sprite) {
        Player.getInstance().addExp(sprite.getMonsterPropetry().getExp());
    }
}
