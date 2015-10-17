package com.cb.adventures.state;

/**
 * Created by jenics on 2015/10/12.
 */
public class BaseState {
    protected int stateId;
    public BaseState(int id){
        stateId = id;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public void entry(){

    }

    public void leave(){

    }
}
