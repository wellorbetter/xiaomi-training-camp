package com.example.weibo_wudongsheng.fragment;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weibo_wudongsheng.R;
import com.example.weibo_wudongsheng.utils.WeiboRequestHelper;

public class LoadingFragment extends Fragment {

    private ImageView iv_circle_outline;
    private TextView tv_loading;
    private ValueAnimator circleAnimator;
    private WeiboRequestHelper weiboRequestHelper;

    public LoadingFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);
        initEvent();
    }

    private void initData() {
        weiboRequestHelper = new WeiboRequestHelper(getContext());
    }

    private void initEvent() {
        // 在loading里面，只会尝试请求推荐页面的具体数据
        weiboRequestHelper.requestHomePageInfo();
    }

    private void initView(View view) {
        iv_circle_outline = view.findViewById(R.id.iv_circle_outline);
        tv_loading = view.findViewById(R.id.tv_loading);
        circleAnim();
    }

    private void circleAnim() {
        // 创建旋转动画
        circleAnimator = ValueAnimator.ofFloat(0f, 360f);
        circleAnimator.setDuration(750); // 设置动画时长，单位毫秒
        circleAnimator.setRepeatCount(ValueAnimator.INFINITE); // 设置无限循环
        circleAnimator.setInterpolator(new LinearInterpolator()); // 设置线性插值器，保持匀速旋转

        // 添加动画更新监听器
        circleAnimator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            iv_circle_outline.setRotation(animatedValue);
        });

        // 开始动画
        circleAnimator.start();
    }
}
