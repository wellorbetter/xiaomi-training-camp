package com.example.weibo_wudongsheng.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author wellorbetter
 */
public class NetworkStatusHelper {

    /**
     * 检查设备是否连接到网络
     *
     * @param context 上下文
     * @return true 如果设备连接到网络，否则为 false
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }
}