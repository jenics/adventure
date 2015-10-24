package com.cb.adventures.state;


/**
 * 状态机接口
 * Created by jenics on 2015/10/12.
 */
public interface IStateMgr {
    void changeState(int stateId);
    BaseState createState(int stateId);
}
