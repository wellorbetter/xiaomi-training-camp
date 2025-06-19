package com.example.day7.utils;


import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

/**
 * @author wellorbetter
 * 上课使用的所有动画函数实现
 */

public class AnimationUtil {
    private AnimationUtil(){}

    public static AnimationUtils instance;
    public synchronized AnimationUtils getInstance() {
        if (instance == null) {
            instance = new AnimationUtils();
        }
        return instance;
    }

    public static void staticAnimator(View view, int animatorResId) {
        ObjectAnimator animator = (ObjectAnimator) AnimatorInflater.loadAnimator(view.getContext(), animatorResId);
        animator.setTarget(view);
        animator.start();
    }

    public static void activeAnimator(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.ROTATION_X, 0.f, 360f);
        animator.setDuration(1000);
        animator.start();
    }

    public static void activeCombineAnimator(View view) {
        PropertyValuesHolder rotationXHolder = PropertyValuesHolder.ofFloat(View.ROTATION_X, 0f, 360f);
        PropertyValuesHolder translateXHolder = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0, 100);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, rotationXHolder, translateXHolder);
        animator.setDuration(1000);
        animator.start();
    }

    public static void activeMultiAnimator(View view1, View view2) {
        ObjectAnimator rotationXAnimator = ObjectAnimator.ofFloat(view1, View.ROTATION_X, 0f, 360f);
        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(view2, View.TRANSLATION_X, 0f, 360f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(rotationXAnimator).after(translateXAnimator);
        animatorSet.setDuration(1000);
        animatorSet.start();
    }

    public static void valueAnimator(final ImageView ivAnimDemo, final TextView tvAnim) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float currentValue = (float) valueAnimator.getAnimatedValue();
                ivAnimDemo.setRotation(currentValue * 360);
                ivAnimDemo.setTranslationX(currentValue * 100);
                tvAnim.setRotation(currentValue * 360 * 2);
            }
        });
        valueAnimator.setDuration(1000);
        valueAnimator.start();
    }

    public static void viewPropertyAnimator(TextView tvAnim) {
        tvAnim.animate()
                .rotationX(360)
                .translationX(100)
                .setDuration(1000)
                .setStartDelay(1000)
                .start();
    }

    public static void objectAnimator(TextView tvAnim) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(tvAnim,
                "backgroundColor", Color.parseColor("#009688"), Color.RED);
        objectAnimator.setEvaluator(new ArgbEvaluator());
        objectAnimator.setDuration(1000);
        objectAnimator.start();
    }

    public static void activeCombine(ImageView ivAnimDemo) {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        RotateAnimation rotateAnimation = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1f, 1.2f, 1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setDuration(1000);
        animationSet.setRepeatMode(Animation.RESTART);
        animationSet.setRepeatCount(5);
        ivAnimDemo.startAnimation(animationSet);
    }

    public static void staticCombine(ImageView ivAnimDemo, int animResId) {
        Animation animation = AnimationUtils.loadAnimation(ivAnimDemo.getContext(), animResId);
        ivAnimDemo.startAnimation(animation);
    }

    public static void staticOpacity(ImageView ivAnimDemo, int animResId) {
        Animation animation = AnimationUtils.loadAnimation(ivAnimDemo.getContext(), animResId);
        ivAnimDemo.startAnimation(animation);
    }

    public static void activeRotate(ImageView ivAnimDemo) {
        RotateAnimation rotateAnimation = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnimation.setRepeatCount(3);
        rotateAnimation.setRepeatMode(Animation.RESTART);
        rotateAnimation.setDuration(1000);
        ivAnimDemo.startAnimation(rotateAnimation);
    }

    public static void activeOpacity(ImageView ivAnimDemo) {
        AlphaAnimation scaleAnimation = new AlphaAnimation(0.0f, 1.0f);
        scaleAnimation.setDuration(6000);
        ivAnimDemo.startAnimation(scaleAnimation);
    }

    public static void staticRotate(ImageView ivAnimDemo, int animResId) {
        Animation animation = AnimationUtils.loadAnimation(ivAnimDemo.getContext(), animResId);
        ivAnimDemo.startAnimation(animation);
    }

    public static void staticScale(ImageView ivAnimDemo, int animResId) {
        Animation animation = AnimationUtils.loadAnimation(ivAnimDemo.getContext(), animResId);
        ivAnimDemo.startAnimation(animation);
    }

    public static void activeScale(ImageView ivAnimDemo) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1f, 1.2f, 1f, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(1000);
        ivAnimDemo.setAnimation(scaleAnimation);
        ivAnimDemo.startAnimation(scaleAnimation);
    }

    public static void staticFrameIn(ImageView ivAnimDemo, int animResId) {
        Animation animation = AnimationUtils.loadAnimation(ivAnimDemo.getContext(), animResId);
        ivAnimDemo.startAnimation(animation);
    }

    public static void activeFrameIn(ImageView ivAnimDemo) {
        Animation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        ivAnimDemo.setAnimation(animation);
    }

    public static void startAnimList(ImageView ivAnimDemo, int[] drawableResIds) {
        AnimationDrawable animationDrawable = new AnimationDrawable();
        animationDrawable.setOneShot(false);
        for (int resId : drawableResIds) {
            animationDrawable.addFrame(ContextCompat.getDrawable(ivAnimDemo.getContext(), resId), 100);
        }
        ivAnimDemo.setImageDrawable(animationDrawable);
        animationDrawable.start();
    }
}
