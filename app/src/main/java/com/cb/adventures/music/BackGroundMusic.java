package com.cb.adventures.music;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.cb.adventures.R;
/**
 * Created by AI on 2015/10/21.
 */
public class BackGroundMusic {
    
    private final static String TAG = "BackGroundMusic";
    private final static boolean IS_DEBUG = false;
    
    private boolean mOpenMusic = true;
    private MediaPlayer mMediaPlayer;
    
    public BackGroundMusic(Context context){
        mMediaPlayer = MediaPlayer.create(context, R.raw.backgroundmusic);
        mMediaPlayer.setLooping(true);
    }
    
    public void setBackMusicOpen(boolean state){
        mOpenMusic = state;
    }
    public void resume(){
        if(mMediaPlayer != null && mOpenMusic){
            if(IS_DEBUG){
                Log.e(TAG, "resume");
            }
            if(!mMediaPlayer.isPlaying()) {
                Log.e(TAG, "isPlaying");
                mMediaPlayer.start();
            }
        }
    }
    public void pause(){
        if(mMediaPlayer != null){
            if(IS_DEBUG){
                Log.e(TAG, "pause");
            }
            mMediaPlayer.pause();
        }
    }
    public void release(){
        if(mMediaPlayer != null){
            if(IS_DEBUG){
                Log.e(TAG, "release");
            }
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
    private void start(){
        if(mMediaPlayer != null){
            if(IS_DEBUG){
                Log.e(TAG, "start");
            }
            mMediaPlayer.start();
        }
    }
}
