package com.example.weibo_wudongsheng.utils;

import android.os.Handler;

import okhttp3.OkHttpClient;

/**
 * @author wellorbetter
 */
public class OkHttpHelper {
    private static OkHttpHelper instance;
    private Handler handler;

    private OkHttpClient client;

    private OkHttpHelper() {
        client = new OkHttpClient();
    }

    public static synchronized OkHttpHelper getInstance() {
        if (instance == null) {
            instance = new OkHttpHelper();
        }
        return instance;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

}
