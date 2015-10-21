package com.cb.adventures.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cb.adventures.R;
import com.cb.adventures.music.BackGroundMusic;

public class GameActivity extends ActionBarActivity {
    
    private final static String TAG = "GameActivity";
    private final static boolean IS_DEBUG = false;
    
    private BackGroundMusic mBackGroundMusic = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(IS_DEBUG){
            Log.e(TAG, "onCreate");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mBackGroundMusic = new BackGroundMusic(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if(IS_DEBUG){
            Log.e(TAG, "onPause");
        }
        super.onPause();
        mBackGroundMusic.pause();
    }
    
    @Override
    protected void onDestroy() {
        if(IS_DEBUG){
            Log.e(TAG, "onDestroy");
        }
        super.onDestroy();
        mBackGroundMusic.release();
        mBackGroundMusic = null;
    }

    @Override
    protected void onPostResume() {
        if (IS_DEBUG) {
            Log.e(TAG, "onPostResume");
        }
        super.onPostResume();
        mBackGroundMusic.resume();
    }
}
