package com.example.weibo_wudongsheng.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weibo_wudongsheng.R;
import com.tencent.mmkv.MMKV;

/**
 * @author wellorbetter
 * 进入动画界面
 *
 * 用户首次使用时展示进入动画和隐私政策，同意后进入主界面
 */
public class EnterAnimActivity extends AppCompatActivity implements View.OnClickListener {

    // UI控件
    private LinearLayout mLlEnterIcon;
    private LinearLayout mLlStatement;
    private ConstraintLayout mClMain;
    private FrameLayout mFlAgree;
    private FrameLayout mFlDisagree;
    private TextView mTvStatementContent;

    // 数据相关
    private MMKV mMMKV;
    private boolean mIsFirstUse = true;
    private boolean mIsAllowPrivacy = true;
    private SpannableString mSpannableString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_anim);

        // 获取 MMKV 实例
        mMMKV = MMKV.defaultMMKV();
        // 获取是否已经点击过隐私，如果是，并且没有同意，就退出
        mIsAllowPrivacy = mMMKV.getBoolean("isAllowPrivacy", true);
        if (!mIsAllowPrivacy) {
            finish();
            return;
        }
        initData();
        initView();
        initAnim();
        initEvent();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 获取用户是否第一次使用
        mIsFirstUse = mMMKV.getBoolean("isFirstUse", true);

        // 设置声明内容
        String statementContent = "欢迎使用 iH微博 ，我们将严格遵守相关法律和隐私政策保护您的个人隐私，请您阅读并同意《用户协议》与《隐私政策》";
        mSpannableString = new SpannableString(statementContent);

        // 获取颜色
        int blueColor = getColor(R.color.blue_500);

        // 创建点击事件
        ClickableSpan userAgreementClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // 在这里处理《用户协议》的点击事件
                Toast.makeText(widget.getContext(), "查看用户协议", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false); // 去掉下划线
            }
        };

        ClickableSpan privacyPolicyClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                // 在这里处理《隐私政策》的点击事件
                Toast.makeText(widget.getContext(), "查看隐私政策", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false); // 去掉下划线
            }
        };

        // 设置点击事件和颜色
        mSpannableString.setSpan(new ForegroundColorSpan(blueColor),
                statementContent.length() - 13, statementContent.length() - 7,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mSpannableString.setSpan(userAgreementClick,
                statementContent.length() - 13, statementContent.length() - 7,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mSpannableString.setSpan(new ForegroundColorSpan(blueColor),
                statementContent.length() - 6, statementContent.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mSpannableString.setSpan(privacyPolicyClick,
                statementContent.length() - 6, statementContent.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    /**
     * 初始化动画
     */
    private void initAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                float currentValue = (float) valueAnimator.getAnimatedValue();
                mLlEnterIcon.setRotation(currentValue * 360);
                mLlEnterIcon.setAlpha(currentValue);
                mLlEnterIcon.setScaleX(currentValue);
                mLlEnterIcon.setScaleY(currentValue);

                // 动画结束时处理
                if (currentValue == 1 && mIsFirstUse) {
                    mClMain.setBackgroundColor(getColor(R.color.transparent_70));
                    mLlStatement.setVisibility(View.VISIBLE);
                    mIsFirstUse = false;
                    mMMKV.encode("isFirstUse", mIsFirstUse);
                } else if (currentValue == 1) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        valueAnimator.setDuration(1000);
        valueAnimator.start();
    }

    /**
     * 初始化事件监听
     */
    private void initEvent() {
        mFlAgree.setOnClickListener(this);
        mFlDisagree.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mTvStatementContent = findViewById(R.id.tv_statement_content);
        mFlAgree = findViewById(R.id.fl_agree);
        mFlDisagree = findViewById(R.id.fl_disagree);
        mClMain = findViewById(R.id.cl_main);
        mLlEnterIcon = findViewById(R.id.ll_enter_icon);
        mLlStatement = findViewById(R.id.ll_statement);
        mClMain.setBackgroundColor(getColor(R.color.white));
        mLlStatement.setVisibility(View.INVISIBLE);

        mTvStatementContent.setText(mSpannableString);
        mTvStatementContent.setHighlightColor(getColor(R.color.trans));
        // 要添加这个，不然点击事件不生效
        mTvStatementContent.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fl_agree) {
            mIsAllowPrivacy = true;
            if (mMMKV != null) {
                mMMKV.encode("isAllowPrivacy", mIsAllowPrivacy);
            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.fl_disagree) {
            mIsAllowPrivacy = false;
            if (mMMKV != null) {
                mMMKV.encode("isAllowPrivacy", mIsAllowPrivacy);
            }
            finish();
        }
    }
}
