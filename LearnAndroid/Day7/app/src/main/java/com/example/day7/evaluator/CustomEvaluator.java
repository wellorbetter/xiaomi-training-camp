package com.example.day7.evaluator;

import android.animation.TypeEvaluator;

/**
 * @author wellorbetter
 * 在y轴上进行sin震荡
 */
public class CustomEvaluator implements TypeEvaluator<Float> {

    @Override
    public Float evaluate(float fraction, Float startValue, Float endValue) {
        return new Float(Math.sin(fraction * Math.PI * 4) * 100);
    }
}
