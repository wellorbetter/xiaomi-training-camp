package com.example.leanandroid.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BindService1 extends Service {
    public  MyBinder myBinder = new MyBinder();
    private int count;
    private String TAG = "BindService1";

    public class MyBinder extends Binder {
        public int getCount() {
            return count;
        }
    }
    public BindService1() {

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        Toast.makeText(getApplicationContext(), "解绑", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);

    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        count ++;
                    }
                }
        ).start();
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return myBinder;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }
}