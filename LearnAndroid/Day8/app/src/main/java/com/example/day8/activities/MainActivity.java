package com.example.day8.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.day8.R;
import com.example.day8.views.CustomEditView;
import com.example.viewgroup.TagCloud;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wellorbetter
 */
public class MainActivity extends AppCompatActivity {

    private GestureDetector mDetector;
    private static final String TAG = "GestureDetector";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    private void initData() {
        List<String> t = new ArrayList<>();
        t.add("标签1");
        t.add("标签2标签");
        t.add("标签3我测你的马");
        t.add("标签4");
        t.add("标签5我");
        t.add("标签6");
        t.add("标签7雪豹雪豹");
        t.add("标签8丁真的");
        ((TagCloud) findViewById(R.id.custom_viewgroup)).setTags(t);

        mDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(@NonNull MotionEvent motionEvent) {
                Log.d(TAG, "onDown: ");
                return false;
            }

            @Override
            public void onShowPress(@NonNull MotionEvent motionEvent) {
                Log.d(TAG, "onShowPress: ");
            }

            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
                Log.d(TAG, "onSingleTapUp: ");
                return false;
            }

            @Override
            public boolean onScroll(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
                Log.d(TAG, "onScroll: ");
                return false;
            }

            @Override
            public void onLongPress(@NonNull MotionEvent motionEvent) {
                Log.d(TAG, "onLongPress: ");
            }

            @Override
            public boolean onFling(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
                Log.d(TAG, "onFling: ");
                return false;
            }
        });
    }

    private void initView() {

    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        boolean result = mDetector.onTouchEvent(event);
//        int action = event.getAction();
//        if (action == MotionEvent.ACTION_UP) {
//            Log.d(TAG, "ACTION_UP");
//        }
//        return result;
//    }
}