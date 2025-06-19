package com.example.leanandroid.BroudCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BroadCastReceiver1 extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context.getApplicationContext(), intent.getAction(), Toast.LENGTH_SHORT).show();
        Log.d("BroadCastReceiver1", "onReceive: " + intent);
    }
}
