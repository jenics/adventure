package com.cb.adventures.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.LinkedList;

/**
 * Created by chengbo01 on 2015/3/23.
 */
public class NetStatus {
    /**
     * 未知网络类别
     */
    public static final int NETWORK_TYPE_UNKNOWN = 0;
    public static final String NETWORK_TYPE_UNKNOWN_NAME = "UNKNOWN";
    /**
     * 2G网络
     */
    public static final int NETWORK_TYPE_2G = 1;
    public static final String NETWORK_TYPE_2G_NAME = "2G";
    /**
     * 3G网络
     */
    public static final int NETWORK_TYPE_3G = 2;
    public static final String NETWORK_TYPE_3G_NAME = "3G";
    /**
     * 4G网络
     */
    public static final int NETWORK_TYPE_4G = 3;
    public static final String NETWORK_TYPE_4G_NAME = "4G";
    /**
     * WIFI网络
     */
    public static final int NETWORK_TYPE_WIFI = 999;
    public static final String NETWORK_TYPE_WIFI_NAME = "WIFI";

    private static final String TAG = "NetStatus";
    private static final boolean DBG = true;
    private static NetStatus sInstance;
    private Context mContext;
    private State mState;
    private boolean mListening;
    private String mReason;
    private boolean mIsFailOver;
    private NetworkInfo mNetworkInfo;
    private boolean mIsWifi = false;
    /**
     * In case of a Disconnect, the connectivity manager may have already established, or may be attempting to establish, connectivity with another network. If so, {@code mOtherNetworkInfo} will be non-null.
     */
    private NetworkInfo mOtherNetworkInfo;
    private ConnectivityBroadcastReceiver mReceiver;

    private NetStatus() {
        mState = State.UNKNOWN;
        mReceiver = new ConnectivityBroadcastReceiver();
    }

    public interface onNetStatusWatcher{
        public void onConnectedChanged(boolean isConnected);
    }
    private LinkedList<onNetStatusWatcher> mWatchers = new LinkedList<>();

    public void addNetStatusWatcher(onNetStatusWatcher watcher){
        for(onNetStatusWatcher w : mWatchers){
            if(w == watcher) return;
        }
        mWatchers.push(watcher);
    }
    /**
     *
     */
    private static int getMobileNetworkClass(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_TYPE_2G;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NETWORK_TYPE_3G;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NETWORK_TYPE_4G;
            default:
                return NETWORK_TYPE_UNKNOWN;
        }
    }

    public static void init(Context context) {
        sInstance = new NetStatus();
        sInstance.mIsWifi = checkIsWifi(context);
        sInstance.startListening(context);
    }

    public static NetStatus getInstance() {
        return sInstance;
    }

    public static boolean checkIsWifi(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) (context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connectivity != null) {
            NetworkInfo networkInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkIsConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 2G/3G/4G/WIFI
     *
     * @return
     */
    public int getNetworkType() {

        NetworkInfo activeNetworkInfo = getNetworkInfo();

        if (activeNetworkInfo != null) {
            // WIFI
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return NETWORK_TYPE_WIFI;
            }
            // MOBILE
            else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return getMobileNetworkClass(activeNetworkInfo.getSubtype());
            }
        }

        return NETWORK_TYPE_UNKNOWN;
    }

    /**
     * This method starts listening for network connectivity state changes.
     *
     * @param context
     */
    public synchronized void startListening(Context context) {
        if (!mListening) {
            mContext = context;

            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            context.registerReceiver(mReceiver, filter);
            mListening = true;
        }
    }

    /**
     * This method stops this class from listening for network changes.
     */
    public synchronized void stopListening() {
        if (mListening) {
            mContext.unregisterReceiver(mReceiver);
            mContext = null;
            mNetworkInfo = null;
            mOtherNetworkInfo = null;
            mIsFailOver = false;
            mReason = null;
            mListening = false;
        }
    }

    /**
     * Return the NetworkInfo associated with the most recent connectivity event.
     *
     * @return {@code NetworkInfo} for the network that had the most recent connectivity event.
     */
    public NetworkInfo getNetworkInfo() {
        return mNetworkInfo;
    }

    /**
     * If the most recent connectivity event was a DISCONNECT, return any information supplied in the broadcast about an alternate network that might be available. If this returns a non-null value, then another broadcast should follow shortly indicating whether connection to the other network succeeded.
     *
     * @return NetworkInfo
     */
    public NetworkInfo getOtherNetworkInfo() {
        return mOtherNetworkInfo;
    }

    /**
     * Returns true if the most recent event was for an attempt to switch over to a new network following loss of connectivity on another network.
     *
     * @return {@code true} if this was a fail over attempt, {@code false} otherwise.
     */
    public boolean isFailover() {
        return mIsFailOver;
    }

    /**
     * An optional reason for the connectivity state change may have been supplied. This returns it.
     *
     * @return the reason for the state change, if available, or {@code null} otherwise.
     */
    public String getReason() {
        return mReason;
    }

    public boolean isWifi() {
        return mIsWifi;
    }

    public String getNetworkTypeName() {
        switch (getNetworkType()) {
            case NETWORK_TYPE_WIFI:
                return NETWORK_TYPE_WIFI_NAME;
            case NETWORK_TYPE_2G:
                return NETWORK_TYPE_2G_NAME;
            case NETWORK_TYPE_3G:
                return NETWORK_TYPE_3G_NAME;
            case NETWORK_TYPE_4G:
                return NETWORK_TYPE_4G_NAME;
            case NETWORK_TYPE_UNKNOWN:
                return NETWORK_TYPE_UNKNOWN_NAME;
        }
        return NETWORK_TYPE_UNKNOWN_NAME;
    }

    public enum State {
        UNKNOWN,

        /**
         * This state is returned if there is connectivity to any network *
         */
        CONNECTED,
        /**
         * This state is returned if there is no connectivity to any network. This is set to true under two circumstances:
         * <ul>
         * <li>When connectivity is lost to one network, and there is no other available network to attempt to switch to.</li>
         * <li>When connectivity is lost to one network, and the attempt to switch to another network fails.</li>
         */
        NOT_CONNECTED
    }

    private class ConnectivityBroadcastReceiver extends BroadcastReceiver {
        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (!action.equals(ConnectivityManager.CONNECTIVITY_ACTION) || mListening == false) {
                CLog.w(TAG, "onReceived() called with " + mState.toString() + " and " + intent);
                return;
            }

            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

            if (noConnectivity) {
                mState = State.NOT_CONNECTED;
            } else {
                mState = State.CONNECTED;
            }
            ///notify the watchers
            for(onNetStatusWatcher w : mWatchers){
                w.onConnectedChanged(mState==State.CONNECTED ? true : false);
            }

            mNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            mOtherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            mReason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            mIsFailOver = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);
            if (DBG) {
                CLog.d(TAG, "onReceive(): mNetworkInfo=" + mNetworkInfo + " mOtherNetworkInfo = " + (mOtherNetworkInfo == null ? "[none]" : mOtherNetworkInfo + " noConn=" + noConnectivity)
                        + " mState=" + mState.toString());
            }

            mIsWifi = checkIsWifi(mContext);
        }
    }
}
