package com.example.weibo_wudongsheng.fragment;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.weibo_wudongsheng.R;
import com.example.weibo_wudongsheng.adapter.HomePageItemAdapter;
import com.example.weibo_wudongsheng.bean.HomePageResponse;
import com.example.weibo_wudongsheng.bean.Page;
import com.example.weibo_wudongsheng.bean.WeiboInfo;
import com.example.weibo_wudongsheng.broadcast.NetworkChangeReceiver;
import com.example.weibo_wudongsheng.utils.PixelTransformHelper;
import com.example.weibo_wudongsheng.utils.retrofit.ApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @作者 wellorbetter
 */
public class RecommendFragment extends Fragment {

    List<WeiboInfo> data;
    private TextView tv_no_internet;
    private PixelTransformHelper pixelTransformHelper;
    private FrameLayout fl_items;
    private HomePageItemFragment homePageItemFragment;
    private LoadingFragment loadingFragment;
    private RequestFailedFragment requestFailedFragment;
    private NetworkChangeReceiver networkChangeReceiver;
    private EmptyDataFragment emptyDataFragment;
    private Context mContext;

    public RecommendFragment(Context mContext) {
        this.mContext = mContext;
    }
    public RecommendFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pixelTransformHelper = PixelTransformHelper.newInstance(getContext().getApplicationContext());
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recommend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        getActivity().unregisterReceiver(networkChangeReceiver);
    }

    private void initData() {
        // 广播需要注册 非常关键,经常忘了
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(networkChangeReceiver, filter);
    }


    private void initView(View view) {
        fl_items = view.findViewById(R.id.fl_items);
        tv_no_internet = view.findViewById(R.id.tv_no_internet);
        loadingFragment = new LoadingFragment();
        // MainActivity -> RecommendFragment -> LoadingFragment
        replaceFragment(loadingFragment);
    }

    private void replaceFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fl_items, fragment)
                .commitNow();
    }

    /**
     * 设置不可见
     */
    private void setInvisible() {
        // 这里不能设置为0dp,因为是约束布局.0dp会感觉margin来调整高度,小坑
        setInternetHint(View.INVISIBLE, 1);
    }

    /**
     * 根据网络情况设置当前网络状态提示消息
     * 不可见同时需要设置高度为0,防止占用空间导致视觉体验不佳
     * 可见时设置固定高度
     *
     * @param invisible  是否可见
     * @param dp     当前提示消息的高度
     */
    private void setInternetHint(int invisible, int dp) {
        tv_no_internet.setVisibility(invisible);
        ViewGroup.LayoutParams params = tv_no_internet.getLayoutParams();
        params.height = pixelTransformHelper.dp2Pixel(dp);
        tv_no_internet.setLayoutParams(params);
    }

    // 订阅网络变化事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChangeEvent(Boolean hasNetwork) {
        if (hasNetwork) {
            setInternetHint(View.INVISIBLE, 1);
        } else {
            setInternetHint(View.VISIBLE, 29);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoadingProcess(String loading_process) {
        if (loading_process != null && loading_process.equals("loading_success")) {
            homePageItemFragment = new HomePageItemFragment(mContext);
            replaceFragment(homePageItemFragment);
        } else if (loading_process != null && loading_process.equals("loading_failed")) {
            requestFailedFragment = new RequestFailedFragment();
            replaceFragment(requestFailedFragment);
        } else if (loading_process != null && loading_process.equals("empty_data")) {
            emptyDataFragment = new EmptyDataFragment();
            replaceFragment(emptyDataFragment);
        } else if (loading_process != null && loading_process.equals("loading")) {
            loadingFragment = new LoadingFragment();
            replaceFragment(loadingFragment);
        }
    }
}
