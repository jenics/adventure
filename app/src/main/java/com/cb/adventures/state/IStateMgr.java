package com.cb.adventures.state;


/**
 * Created by jenics on 2015/10/12.
 */
public interface IStateMgr {
    public boolean changeState(int stateId);
    public BaseState createState(int stateId);
}
