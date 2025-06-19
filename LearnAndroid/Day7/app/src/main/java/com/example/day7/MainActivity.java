package com.example.day7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
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

import com.example.day7.evaluator.CustomEvaluator;
import com.example.day7.interpolator.CustomInterpolator;
import com.example.day7.utils.AnimationUtil;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener {
    ImageView ivAnimDemo;
    TextView tvAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
    }
    private void initView() {
        ivAnimDemo = findViewById(R.id.iv_anim_demo);
        tvAnim = findViewById(R.id.tv_anim);
    }
    private void initEvent() {
        tweenAnimationDemo(ivAnimDemo);
        attrAnimationDemo(tvAnim);
    }

    /**
     * 使用AnimatorSet同时组合多个不同动画，rotateXAnimator X轴移动
     * 使用自定义插值器CustomInterpolator，速度会越来越慢
     *
     * translateYAnimator y轴移动，使用自定义的CustomEvaluator
     * 会在y轴震荡
     *
     * 先绕X轴旋转 1000ms 然后同时X轴移动，y轴移动
     * 最后改变透明度
     *
     * @param view 操作的view对象
     */
    private void attrAnimationDemo(View view) {
        AnimatorSet animatorSet = new AnimatorSet();

        // 绕X轴旋转
        ObjectAnimator rotateXAnimator = ObjectAnimator.ofFloat(view, View.ROTATION_X, 0f, 360f);
        rotateXAnimator.setInterpolator(new CustomInterpolator());
        rotateXAnimator.setDuration(1000);

        // X轴移动
        ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0f, 120f);
        translateXAnimator.setDuration(1000);

        // Y轴移动，使用自定义的 CustomEvaluator
        ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0f, 100f);
        translateYAnimator.setEvaluator(new CustomEvaluator());
        translateYAnimator.setDuration(1000);

        // 透明度变化
        ObjectAnimator opacityAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 1.0f, 0.5f);
        opacityAnimator.setDuration(500);

        // 先旋转，然后同时进行X轴移动和Y轴移动，最后改变透明度
        animatorSet.play(rotateXAnimator).before(translateXAnimator);
        animatorSet.play(translateXAnimator).with(translateYAnimator);
        animatorSet.play(opacityAnimator).after(translateXAnimator);

        animatorSet.start();
    }

    /**
     * 使用 AnimationSet 组合多个补间动画。
     * AlphaAnimation 改变透明度。
     * RotateAnimation 旋转。
     * ScaleAnimation 缩放。
     *
     * @param view 操作的 view 对象
     */
    void tweenAnimationDemo(View view) {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(
                1.0f, 0.8f
        );
        alphaAnimation.setRepeatCount(2);
        RotateAnimation rotateAnimation = new RotateAnimation(
                0, -720,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnimation.setRepeatCount(2);
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                1f, 1.5f, 1f, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setRepeatCount(2);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setDuration(2000);
        alphaAnimation.setAnimationListener(this);
        view.startAnimation(animationSet);
    }


    String TAG = "ANIMATION";

    @Override
    public void onAnimationStart(Animation animation) {
        Log.d(TAG, "onAnimationStart: ");
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Log.d(TAG, "onAnimationEnd: ");
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        Log.d(TAG, "onAnimationRepeat: ");
    }
}
