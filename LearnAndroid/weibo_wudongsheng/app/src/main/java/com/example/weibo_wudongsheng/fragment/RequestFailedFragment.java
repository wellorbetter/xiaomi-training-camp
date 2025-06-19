package com.example.weibo_wudongsheng.fragment;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.weibo_wudongsheng.R;
import com.example.weibo_wudongsheng.broadcast.NetworkChangeReceiver;
import com.example.weibo_wudongsheng.utils.WeiboRequestHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RequestFailedFragment extends Fragment {
    private Button bt_request_failed;
    private NetworkChangeReceiver networkChangeReceiver;
    private WeiboRequestHelper weiboRequestHelper;

    public RequestFailedFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        // 注册网络变化广播接收器
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(networkChangeReceiver, filter);
        weiboRequestHelper = new WeiboRequestHelper(getContext().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_request_failed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvent();
    }

    private void initEvent() {
        bt_request_failed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击跳转到loading
                EventBus.getDefault().post("loading");
            }
        });
    }

    private void initView(View view) {
        bt_request_failed = view.findViewById(R.id.bt_request_failed);
    }



    // 订阅网络变化事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChangeEvent(Boolean hasNetwork) {
        if (hasNetwork) {
            weiboRequestHelper.requestHomePageInfo();
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        // 反注册
        if (networkChangeReceiver != null) {
            getActivity().unregisterReceiver(networkChangeReceiver);
        }
        EventBus.getDefault().unregister(this);
    }
}
