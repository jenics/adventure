package com.cb.adventures.engine;

import android.content.Context;

/**
 * Created by jenics on 2015/12/27.
 */
public class Engine implements IEngine {
    private static Engine mInstance;
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
}
