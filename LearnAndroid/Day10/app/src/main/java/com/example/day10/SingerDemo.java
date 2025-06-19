package com.example.day10;

import android.content.Context;

/**
 * @author wellorbetter
 */
public class SingerDemo {

    private Context context;
    private static SingerDemo instance;

    private SingerDemo(Context context) {
        this.context = context;
    }

    public static SingerDemo getInstance(Context context) {
        if (instance == null) {
            instance = new SingerDemo(context);
        }
        return instance;
    }
}
