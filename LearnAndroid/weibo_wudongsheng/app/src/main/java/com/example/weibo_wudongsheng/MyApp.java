package com.example.weibo_wudongsheng;

/**
 * @author wellorbetter
 */

import android.app.Application;

import com.tencent.mmkv.MMKV;

public class MyApp extends Application {
    public static boolean isLogin = false;

    @Override
    public void onCreate() {
        super.onCreate();
        MMKV.initialize(getApplicationContext());
        isLogin = MMKV.defaultMMKV().getBoolean("isLogin", false);
    }
}
