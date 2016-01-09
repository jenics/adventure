package com.cb.adventures.engine;

/**
 * Created by jenics on 2015/12/27.
 */
public interface IEngine {
    /**
     * 启动游戏
     */
    void startGame();

    /**
     * 退出游戏
     */
    void exitGame();

    /**
     * 暂停游戏
     */
    void pauseGame();

    /**
     * 恢复游戏
     */
    void resumeGame();


}
