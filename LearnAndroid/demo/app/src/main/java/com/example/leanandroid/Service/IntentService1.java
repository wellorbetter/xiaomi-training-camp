package com.example.leanandroid.Service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class IntentService1 extends IntentService {
    String TAG = "IntentService1";
    public IntentService1() {
        super("IntentService1");
    }


    // 睡眠一秒，检验是否开启
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            Toast.makeText(this, "睡眠开始", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onHandleIntent: 睡眠开始");
            Thread.sleep(1000);
            Log.d(TAG, "onHandleIntent: 睡眠结束");
            Toast.makeText(this, "睡眠结束", Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }
}