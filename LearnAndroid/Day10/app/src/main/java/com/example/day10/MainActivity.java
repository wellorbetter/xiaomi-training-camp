package com.example.day10;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DeadlockDemo";

    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ANRWatchDog().start();
        Button button = findViewById(R.id.bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                createDeadlock();
                // 第一次ANRWatchDog发进来，这个时候主线程是没有阻塞的，它就会睡眠默认秒数
                // 如果是5s，那么这个时候主线程同时开始阻塞，5s之后，主线程此时已经阻塞了5s了，但是
                // 并不会捕捉到ANR，因为ANRWatchDog第二次发送的时候是在主线程第六秒，这是并没有到5s
                // 所以我们改成1s1发，5次累计确认
                simulateBlocking(6000);
            }
        });

    }

    private static void simulateBlocking(int duration) {
        System.out.println("Simulating blocking for " + duration + "ms");
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createDeadlock() {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock1) {
                    Log.d(TAG, "Thread 1: Holding lock 1...");

                    try { Thread.sleep(100); } catch (InterruptedException e) {}
                    Log.d(TAG, "Thread 1: Waiting for lock 2...");

                    synchronized (lock2) {
                        Log.d(TAG, "Thread 1: Holding lock 1 & 2...");
                    }
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock2) {
                    Log.d(TAG, "Thread 2: Holding lock 2...");

                    try { Thread.sleep(100); } catch (InterruptedException e) {}
                    Log.d(TAG, "Thread 2: Waiting for lock 1...");

                    synchronized (lock1) {
                        Log.d(TAG, "Thread 2: Holding lock 2 & 1...");
                    }
                }
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
