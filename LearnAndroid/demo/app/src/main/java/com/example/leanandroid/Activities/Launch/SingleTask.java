package com.example.leanandroid.Activities.Launch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.leanandroid.Activities.MainActivity;
import com.example.leanandroid.R;

public class SingleTask extends AppCompatActivity implements View.OnClickListener {

    private Button bt, bt_self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_single_task);
        bt = findViewById(R.id.bt);
        bt_self = findViewById(R.id.bt_self);
        bt.setOnClickListener(this);
        bt_self.setOnClickListener(this);
    }
    String TAG = "SingleTask";
    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent: ");
        super.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "onRestart: ");
        super.onRestart();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.bt) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.bt_self) {
            Intent intent = new Intent(this, SingleTop.class);
            startActivity(intent);
        }
    }
}