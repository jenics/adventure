package com.cb.adventures.activity;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.cb.adventures.R;
import com.cb.adventures.music.MusicManager;


public class GameActivity extends ActionBarActivity {
    
    private final static String TAG = "GameActivity";
    private final static boolean IS_DEBUG = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(IS_DEBUG){
            Log.e(TAG, "onCreate");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
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
        MusicManager.getInstance().pauseMedia(MusicManager.STATIC_MEDIA_TYPE_BACKGROUND);
    }
    
    @Override
    protected void onDestroy() {
        if(IS_DEBUG){
            Log.e(TAG, "onDestroy");
        }
        super.onDestroy();
        MusicManager.getInstance().release();
    }

    @Override
    protected void onPostResume() {
        if (IS_DEBUG) {
            Log.e(TAG, "onPostResume");
        }
        super.onPostResume();
        MusicManager.getInstance().playMedia(MusicManager.STATIC_MEDIA_TYPE_BACKGROUND);
    }
}
