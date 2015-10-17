package com.cb.adventures.state;


/**
 * Created by jenics on 2015/10/12.
 */
public interface IStateMgr {
    public boolean changeState(int stateId);

    /**
     *
     * @param stateId
     * @param bForce 是否强制进入状态
     * @return
     */
    public boolean changeState(int stateId,boolean bForce);

    public BaseState createState(int stateId);
}
