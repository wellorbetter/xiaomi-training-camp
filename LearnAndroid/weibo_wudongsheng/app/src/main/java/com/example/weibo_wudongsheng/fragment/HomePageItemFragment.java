package com.example.weibo_wudongsheng.fragment;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.weibo_wudongsheng.R;
import com.example.weibo_wudongsheng.adapter.HomePageItemAdapter;
import com.example.weibo_wudongsheng.bean.HomePageResponse;
import com.example.weibo_wudongsheng.bean.Page;
import com.example.weibo_wudongsheng.bean.WeiboInfo;
import com.example.weibo_wudongsheng.broadcast.NetworkChangeReceiver;
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

public class HomePageItemFragment extends Fragment {
    private List<WeiboInfo> data;
    private boolean isTimeout = false;
    private SwipeRefreshLayout sl_items;
    private NetworkChangeReceiver networkChangeReceiver;
    private RecyclerView rv_items;
    private HomePageItemAdapter homePageItemAdapter;;
    boolean isLoadMore = false;
    private int current = 1, size = 10;
    boolean isRefreshing = false;

    boolean isInit = true;
    String loginToken;
    boolean isEnd = false;
    MMKV mmkv = MMKV.defaultMMKV();
    private Context mContext;
    public HomePageItemFragment(Context mContext) {
        this.mContext = mContext;
    }
    public HomePageItemFragment(){}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_page_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);
        initEvent();
    }

    private void initEvent() {
        sl_items.setOnRefreshListener(this::refresh);
    }

    /**
     * 刷新逻辑
     */
    private void refresh() {
        if (loginToken != null) {
            // 重置
            current = 1;
            size = 10;
            isRefreshing = true;
            // 如果在3秒内请求成功，会设置isTimeout为false
            isTimeout = true;
            // 发起网络请求
            getHomePageInfo(loginToken, current, size);

            checkDelay();
        } else {
            // 取消刷新状态
            sl_items.setRefreshing(false);
        }
    }

    /**
     * 刷新超时逻辑
     */
    private void checkDelay() {
        // 设置刷新超时
        // 3秒超时时间
        sl_items.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isRefreshing && isTimeout) {
                    sl_items.setRefreshing(false);
                    Toast.makeText(getContext(), "刷新超时", Toast.LENGTH_SHORT).show();
                    // 显示无网络提示  这里需要通信
                    isRefreshing = false; // 标记刷新结束
                }
            }
        }, 3000);
    }


    private void setupLoadMore() {
        homePageItemAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        homePageItemAdapter.getLoadMoreModule().setAutoLoadMore(true);
        homePageItemAdapter.getLoadMoreModule().setOnLoadMoreListener(this::loadMore);
    }

    /**
     * 加载更多逻辑
     */
    private void loadMore() {
        // 防止一直触发
        if (!isLoadMore) {
            isLoadMore = true;
            current ++ ;
            getHomePageInfo(loginToken, current, size);
        }
    }
    private void initView(View view) {
        sl_items = view.findViewById(R.id.sl_items);
        rv_items = view.findViewById(R.id.rv_items);

        // 尝试获取本地数据
        initPage();
        // 尝试请求网络进行初始化
        isInit = true;
        getHomePageInfo(loginToken, current, size);
    }
    /**
     * 请求推荐页面的item数据
     *
     * @param loginToken  本地存储的用户token
     * @param current     请求的页面页码数
     * @param size        一页的item数量
     */
    public void getHomePageInfo(String loginToken, int current, int size) {
        Call<HomePageResponse> call = ApiClient.getApiService()
                .getHomePageInfo(
                        "Bearer " + loginToken,
                        current, size
                );
        call.enqueue(new Callback<HomePageResponse>() {
            @Override
            public void onResponse(Call<HomePageResponse> call, Response<HomePageResponse> response) {
                getHomePageInfoResponseSuccess(response);
            }
            @Override
            public void onFailure(Call<HomePageResponse> call, Throwable t) {
                Toast.makeText(getContext(), "请检查网络状况!", Toast.LENGTH_SHORT).show();
                if (isNoDataInPage()) {
                    // 替换当前的fragment为RequestFragment
                    EventBus.getDefault().post("loading_failed");
                }
            }
        });
    }
    /**
     * 成功请求主页信息
     * @param response 返回体
     */
    private void getHomePageInfoResponseSuccess(Response<HomePageResponse> response) {
        if (response.isSuccessful()) {
            // 请求成功处理
            HomePageResponse homePageResponse = response.body();
            if (homePageResponse.getCode() == 200) {
                getHomePageInfoSuccess(homePageResponse);
            } else if (homePageResponse.getCode() == 403) {
                // 删除过期的token
                removeToken();
            }
        } else {
            Toast.makeText(getContext(), "数据请求失败!" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
            // 第一次请求且本地请求也失败了,没有如何数据在页面上,进入点击重试页面
            if (isNoDataInPage()) {
                // 替换当前的fragment为RequestFragment
                EventBus.getDefault().post("loading_failed");
            }
        }
    }

    /**
     * 成功拿到HomePageInfo 进行数据处理
     *
     * @param homePageResponse 请求的返回体
     */
    private void getHomePageInfoSuccess(HomePageResponse homePageResponse) {
        // 登录成功，写入消息
        Page page = homePageResponse.getData();
        // 如果是第一次进来的话，是需要加载数据的
        if (isInit) {
            // 初始化数据以及适配器
            data = page.getRecords();
            data.stream().forEach(WeiboInfo::setItemType);
            initRecyclerView();
            isInit = false;
            isEnd = false;
        }
        // 如果是刷新
        if (isRefreshing) {
            isTimeout = false;
            // 初始化和加载都会让isEnd置为false,这个时候显然是没有到底的
            isEnd = false;
            // 刷新之后只会截取到这一页
            data.clear(); // 清空数据
            data.addAll(page.getRecords());
            Collections.shuffle(data);
            data.stream().forEach(WeiboInfo::setItemType);
            homePageItemAdapter.notifyDataSetChanged();
            isRefreshing = false;
            sl_items.setRefreshing(false);
            // 保存数据到 MMKV
            saveDataToMMKV(data);
        } else if (isLoadMore) {
            // 下拉加载
            int recordIndex = data.size();
            data.addAll(page.getRecords());
            // 每次使用前都需要拿到自己的itemtype,太关键了,因为它传进来是不会自己写好type的
            // 巨坑的bug
            data.stream().forEach(WeiboInfo::setItemType);
            homePageItemAdapter.notifyItemRangeChanged(recordIndex, page.getRecords().size());
            isLoadMore = false;
            // 加载到底之后只会显示一次没有更多
            if (page.getRecords().size() == 0 && !isEnd) {
                isEnd = true;
                Toast.makeText(getContext(), "没有更多了!", Toast.LENGTH_SHORT).show();
            }
            homePageItemAdapter.getLoadMoreModule().loadMoreComplete();
        }
    }

    /**
     * 保存数据到本地
     * @param data 当前刷新的数据
     */
    private void saveDataToMMKV(List<WeiboInfo> data) {
        mmkv.encode("weibo_data", new Gson().toJson(data));
    }

    /**
     * 从本地加载数据
     * @return 保存到本地的item数据
     */
    private List<WeiboInfo> getDataFromMMKV() {
        String jsonData = mmkv.decodeString("weibo_data", null);
        if (jsonData != null) {
            Type listType = new TypeToken<List<WeiboInfo>>(){}.getType();
            return new Gson().fromJson(jsonData, listType);
        } else {
            // 返回空列表或者默认数据
            return new ArrayList<>();
        }
    }
    /**
     * 从 MMKV 中恢复数据
     */
    private void initPage() {
        List<WeiboInfo> savedData = getDataFromMMKV();
        if (!savedData.isEmpty()) {
            data.addAll(savedData);
            initRecyclerView();
        }
    }
    private void initData() {
        // 广播需要注册 非常关键,经常忘了
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(networkChangeReceiver, filter);
        EventBus.getDefault().register(this);

        loginToken = mmkv.getString("loginToken", "");
        if (data == null) {
            data = new ArrayList<>();
        }
    }

    /**
     * 删除过期的token
     */
    private void removeToken() {
        mmkv.remove("loginToken");
        Toast.makeText(getContext(), "登录状态已过期，请重新登录!", Toast.LENGTH_SHORT).show();
    }

    /**
     * 判断当前是否是没有数据在页面加载
     *
     * @return  是否有数据在页面上
     */
    private boolean isNoDataInPage() {
        return isInit && (data == null || data.size() == 0);
    }

    /**
     * 初始化recyclerview并设置加载更多
     */
    private void initRecyclerView() {
        if (getContext() == null) {
            return;
        }
        data.stream().forEach(WeiboInfo::setItemType);
        homePageItemAdapter = new HomePageItemAdapter(data, getContext());
        rv_items.setAdapter(homePageItemAdapter);
        rv_items.setLayoutManager(new LinearLayoutManager(getContext()));
        setupLoadMore();
    }

    // 订阅网络变化事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNetworkChangeEvent(Boolean hasNetwork) {
        // 重新加载 isInit设置为true
        current = 1; size = 10; isInit = true;
        getHomePageInfo(loginToken, current, size);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 别忘了反注册
        EventBus.getDefault().unregister(this);
    }
}