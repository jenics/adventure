package com.cb.adventures.state.playerstate;

import android.graphics.Canvas;

import com.cb.adventures.state.BaseState;
import com.cb.adventures.view.Player;

/**
 * Created by jenics on 2015/10/12.
 */
public class PlayerBaseState extends BaseState{
    protected Player player;
    public PlayerBaseState(int id, Player player) {
        super(id);
        this.player = player;
    }

    public boolean nextFrame(){
        return true;
    }

    public void draw(Canvas canvas){

    }
}
