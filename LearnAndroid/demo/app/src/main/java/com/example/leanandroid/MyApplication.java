package com.example.leanandroid;

import android.app.Application;
import android.util.Log;
import android.widget.Button;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String TAG = "My Application";
        Log.d(TAG, "onCreate: My Application");
    }
}
