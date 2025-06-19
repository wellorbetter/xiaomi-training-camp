package com.example.weibo_wudongsheng.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weibo_wudongsheng.R;

import org.greenrobot.eventbus.EventBus;

public class EmptyDataFragment extends Fragment {


    private ConstraintLayout cl_empty_data;

    public EmptyDataFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_empty_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvent();
    }

    private void initEvent() {
        cl_empty_data.setOnClickListener(v -> {
            // 只要点击了，就会尝试进入loading加载数据
            EventBus.getDefault().post("loading");
        });
    }

    private void initView(View view) {
        cl_empty_data = view.findViewById(R.id.cl_empty_data);
    }
}