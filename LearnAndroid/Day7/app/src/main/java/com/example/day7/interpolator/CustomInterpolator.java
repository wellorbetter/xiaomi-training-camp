package com.example.day7.interpolator;

import android.view.animation.Interpolator;

/**
 * @author wellorbetter
 * 速度*10后取log10，会越来越慢
 */
public class CustomInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float v) {
        return (float) Math.log10(v * 10);
    }
}
