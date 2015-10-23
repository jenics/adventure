package com.cb.adventures.music;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;


import com.cb.adventures.R;
import com.cb.adventures.activity.MainActivity;
import com.cb.adventures.common.MyApplication;
import com.cb.adventures.utils.CLog;

import java.util.HashMap;

/**
 * Created by AI on 2015/10/21.
 */
public class MusicManager {
    private static final String TAG = "MusicManager";
    private final static boolean IS_DEBUG = false;

    private boolean mOpenBgSound = true;
    private boolean mOpenEffectSound = true;

    /**
     * 添加背景音乐
     */
    public static final int STATIC_MEDIA_TYPE_BACKGROUND = 0;
    public static final int STATIC_MEDIA_TYPE_COUNT = STATIC_MEDIA_TYPE_BACKGROUND + 1;
    private int[] mediaListID = {
            R.raw.backgroundmusic,
    };

    /**
     * 添加特效音乐
     */
    public static final int STATIC_SOUND_TYPE_ATTACK = 0;
    public static final int STATIC_SOUND_TYPE_COUNT = STATIC_SOUND_TYPE_ATTACK + 1;
    private int[] soundListID = {
            R.raw.attack1
    };

    private final int maxStreams = 10; //streamType音频池的最大音频流数目为10    
    private final int srcQuality = 100;
    private final int soundPriority = 1;
    private final float soundSpeed = 1.0f;//播放速度 0.5 -2 之间   


    private SoundPool mSoundPool;
    private HashMap<Integer, Integer> mSoundPoolMap;
    private HashMap<Integer, MediaPlayer> mMdiaMap;

    private static MusicManager mMusicManager;
    private Context mContext;

    private MusicManager() {
        if (IS_DEBUG) {
            CLog.e(TAG, "MusicManager");
        }
        mContext = MyApplication.getContextObj();
        initMediaPlayer();
        initSoundPool();
    }

    public static MusicManager getInstance() {
        if (mMusicManager == null) {
            mMusicManager = new MusicManager();
        }
        return mMusicManager;
    }

    /**
     * 设置开关
     */
    public void setOpenBgState(boolean bgSound) {
        mOpenBgSound = bgSound;
        if (!bgSound && mMdiaMap != null) {
            for (int i = 0; i < mMdiaMap.size(); i++) {
                mMdiaMap.get(i).pause();
            }
        }
    }

    public void setOpenEffectState(boolean effectSound) {
        mOpenEffectSound = effectSound;
        if (!effectSound && mSoundPoolMap != null) {
            for (int i = 0; i < mSoundPoolMap.size(); i++) {
                mSoundPool.pause(mSoundPoolMap.get(i));
            }
        }
    }

    private void initMediaPlayer() {
        if (IS_DEBUG) {
            CLog.e(TAG, "initMediaPlayer");
        }
        mMdiaMap = new HashMap<Integer, MediaPlayer>();
        for (int i = 0; i < STATIC_MEDIA_TYPE_COUNT; i++) {
            MediaPlayer mediaPlayer = MediaPlayer.create(mContext, mediaListID[i]);
            mMdiaMap.put(i, mediaPlayer);
        }
    }

    private void initSoundPool() {
        if (IS_DEBUG) {
            CLog.e(TAG, "initSoundPool");
        }
        mSoundPool = new SoundPool(maxStreams, AudioManager.STREAM_MUSIC, srcQuality);
        mSoundPoolMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < STATIC_SOUND_TYPE_COUNT; i++) {
            mSoundPoolMap.put(i, mSoundPool.load(mContext, soundListID[i], soundPriority));
        }
    }

    public void playMedia(int mediaType) {
        if (IS_DEBUG) {
            CLog.e(TAG, "playMedia:" + mOpenBgSound);
        }
        if (!mOpenBgSound) {
            return;
        }

        MediaPlayer mediaPlayer = mMdiaMap.get(mediaType);
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void pauseMedia(int mediaType) {
        if (IS_DEBUG) {
            CLog.e(TAG, "pauseMedia");
        }
        MediaPlayer mediaPlayer = mMdiaMap.get(mediaType);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void playSound(int soundID, int loop) {
        if (IS_DEBUG) {
            CLog.e(TAG, "playSound:" + mOpenEffectSound);
        }
        if (!mOpenEffectSound) {
            return;
        }
        AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        float streamVolumeCurrent = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float streamVolumeMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = streamVolumeCurrent / streamVolumeMax;
        mSoundPool.play(mSoundPoolMap.get(soundID), volume, volume, soundPriority, loop, soundSpeed);
    }

    public void pauseSound(int soundID) {
        mSoundPool.pause(mSoundPoolMap.get(soundID));
    }

    public void release() {
        if (IS_DEBUG) {
            CLog.e(TAG, "release");
        }
        for (int i = 0; i < mMdiaMap.size(); i++) {
            MediaPlayer mp = mMdiaMap.get(i);
            mp.reset();
            mp.release();
        }
        mMdiaMap.clear();
        if (mSoundPool != null) {
            mSoundPool.release();
        }
        mSoundPoolMap.clear();
        mMusicManager = null;
    }
}

