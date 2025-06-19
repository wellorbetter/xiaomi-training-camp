package com.example.weibo_wudongsheng.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.example.weibo_wudongsheng.utils.NetworkStatusHelper;

import org.greenrobot.eventbus.EventBus;


/**
 * @author wellorbetter
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            EventBus.getDefault().post(NetworkStatusHelper.isNetworkConnected(context));
        }
    }
}
