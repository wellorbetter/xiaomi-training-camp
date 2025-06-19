package com.example.day9.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.example.day9.R;
import com.example.day9.adapters.GameInfoPageAdapter;
import com.example.day9.beans.SearchResponseData;
import com.example.day9.retrofit.ApiClient;
import com.example.day9.retrofit.ApiService;
import com.example.day9.utils.PermissionHelper;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wellorbetter
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener {
    public static final int REQUEST_GAME_PAGE_INFO;
    public static final String KEY_SEARCH_LIST_INFO;
    public static final HashSet<String> DEFAULT_SEARCH_LIST_INFO = new HashSet<>();
    static {
        // 默认留下一个提示消息
        DEFAULT_SEARCH_LIST_INFO.add("这里很空哦");
        KEY_SEARCH_LIST_INFO = "searchInfoList";
        REQUEST_GAME_PAGE_INFO = 1;
    }
    SwipeRefreshLayout srlGameInfo;
    RecyclerView rvGameInfo;
    GameInfoPageAdapter gameInfoPageAdapter;
    ArrayAdapter searchAdapter;
    Handler handler;
    ApiService apiService;
    ApiClient apiClient;
    SearchView svGameInfo;
    /**
     *   因为mmkv不支持List<String> 但是支持Set<String>
     *   所以这里使用searchInfoSet来进行存储
     *   searchInfoList用来更新Adapter
     */
    HashSet<String> searchInfoSet;
    List<String> searchInfoList;
    ListView lvSearchGameInfo;
    ImageView ivNetworkOut;
    String mSearch;
    /**
     * current  记录当前的页面
     * size   数据个数
     */
    int current, size;
    /**
     *  当前页面的渲染数据 需要处理
     */
    List<SearchResponseData.GameInfoPage.GameInfo> gameInfo;
    private MMKV mmkv;
    /**
     *   1    正在下拉刷新
     *   0    正在上拉加载
     *  -1    第一次加载
     */
    int requestCondition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        initEvent();
    }

    private void initData() {
        // 存储数据
        mmkv = MMKV.defaultMMKV();
        searchInfoSet = (HashSet<String>) mmkv.getStringSet(KEY_SEARCH_LIST_INFO, DEFAULT_SEARCH_LIST_INFO);
        searchInfoList = new ArrayList<>(searchInfoSet);
        searchAdapter = new ArrayAdapter<>(this, R.layout.lv_search_game_info_item, searchInfoList);

        gameInfo = new ArrayList<>();
        apiClient = new ApiClient();
        apiService = ApiClient.getApiService();
        initHandler();

        current = 1; size = 5;
        mSearch = "";
        getGameInfo(mSearch, current, size);
    }

    /**
     * 初始化handler，晚上处理事务逻辑
     * 分别处理不同的加载情况
     */
    private void initHandler() {
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                // 根据数据加载recyclerview
                if (msg.what == REQUEST_GAME_PAGE_INFO) {
                    List<SearchResponseData.GameInfoPage.GameInfo> info = ((SearchResponseData)msg.obj).getData().getRecords();
                    // 还要判断是刷新还是加载 / 第一次加载
                    if (requestCondition == -1 || requestCondition == 1) {
                        // 这里判断是否是刷新或者第一次加载
                        if (requestCondition == 1) {
                            // 下拉刷新，把新数据合并到现有列表的前面
                            gameInfo.clear();
                        }
                        gameInfo.addAll(info);

                        // 第一次加载，只需要加载一次
                        if (requestCondition == -1) {
                            initPage();
                        } else {
                            if (gameInfoPageAdapter != null) {
                                // 刷新结束了
                                srlGameInfo.setRefreshing(false);
                                gameInfoPageAdapter.notifyDataSetChanged();
                            }
                        }
                    } else if (requestCondition == 0) {
                        // 加载更多，将新数据添加到现有列表的末尾
                        int notifyIndex = gameInfo.size();
                        gameInfo.addAll(info);
                        gameInfoPageAdapter.notifyItemRangeInserted(notifyIndex, info.size());
                        gameInfoPageAdapter.getLoadMoreModule().loadMoreComplete();
                    }
                    Log.d("handleMessage", "handleMessage: " + info);
                    if (gameInfo.size() != 0) {
                        ivNetworkOut.setVisibility(View.INVISIBLE);
                    }
                }
            }
        };
    }

    /**
     * 第一次加载好页面的时候需要设置一下
     */
    private void initPage() {
        gameInfoPageAdapter = new GameInfoPageAdapter(R.layout.rv_game_info_item, gameInfo);
        rvGameInfo.setAdapter(gameInfoPageAdapter);
        rvGameInfo.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        // 这个东西还不能放在initEvent里面，因为可能还没加载好
        setLoadMore();
    }

    /**
     * 配置加载更多
     */
    private void setLoadMore() {
        gameInfoPageAdapter.getLoadMoreModule().setAutoLoadMore(true);
        gameInfoPageAdapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        gameInfoPageAdapter.getLoadMoreModule().setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                current++;
                requestCondition = 0;
                getGameInfo(mSearch, current, size);
            }
        });
    }

    /**
     * 拿到当前页面的数据进行渲染
     *
     * @param search    搜索关键词
     * @param current   当前页面
     * @param size      页面数据个数
     */
    private void getGameInfo(String search, int current, int size) {
        Call<SearchResponseData> call = apiService.search(search, current, size);
        call.enqueue(new Callback<SearchResponseData>() {
            @Override
            public void onResponse(Call<SearchResponseData> call, Response<SearchResponseData> response) {
                Log.d("retrofit", "onResponse: " + response.body());
                if (response.isSuccessful()) {
                    Message message = new Message();
                    message.what = REQUEST_GAME_PAGE_INFO;
                    message.obj = response.body();
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(Call<SearchResponseData> call, Throwable t) {
                // 网络请求失败处理
                Log.e("retrofit", "Network request failed", t);
                // 如果没有初始化，必须先初始化一下 因为可能一进去就没网
                if (requestCondition == -1) {
                    initPage();
                }
                // 停止刷新动画（如果正在刷新）
                if (srlGameInfo.isRefreshing()) {
                    srlGameInfo.setRefreshing(false);
                }

                // 停止加载更多（如果正在加载更多）
                if (gameInfoPageAdapter.getLoadMoreModule().isLoading()) {
                    gameInfoPageAdapter.getLoadMoreModule().loadMoreFail();
                }
                // 啥也没有的时候才会显示这个
                if (gameInfo.size() != 0) {
                    ivNetworkOut.setVisibility(View.INVISIBLE);
                }
                // 显示错误消息给用户
                Toast.makeText(MainActivity.this, "网络请求失败，请检查网络连接", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void initEvent() {
        svGameInfo.setOnQueryTextListener(this);
        setOnRefresh();
        setFocusChange();
        setRootView();
        setListView();
    }

    /**
     * 当searchview焦点改变的时候隐藏下面的listview提示
     */
    private void setFocusChange() {
        svGameInfo.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    lvSearchGameInfo.setVisibility(View.VISIBLE); // 显示历史记录
                } else {
                    lvSearchGameInfo.setVisibility(View.GONE); // 隐藏历史记录
                }
            }
        });
    }

    /**
     * 刷新
     */
    private void setOnRefresh() {
        srlGameInfo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestCondition = 1;
                current = 1;
                getGameInfo(mSearch, current, size);
            }
        });
    }

    /**
     *  似乎只有这样才能实现获取焦点，然后隐藏listview，点击卡片也可以隐藏(也写了触摸事件)
     */
    private void setRootView() {
        View rootView = findViewById(android.R.id.content);
        if (rootView != null) {
            rootView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        // 点击根视图时，隐藏历史记录列表，并取消SearchView的焦点
                        lvSearchGameInfo.setVisibility(View.GONE);
                        svGameInfo.clearFocus();
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 点击listview里面的item的时候，拿到数据并搜索
     */
    private void setListView() {
        lvSearchGameInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取点击的历史记录项的文本
                String selectedQuery = searchInfoList.get(position);
                // 设置到SearchView中
                svGameInfo.setQuery(selectedQuery, false); // false表示不提交查询
                // 隐藏历史记录列表
                lvSearchGameInfo.setVisibility(View.GONE);
                // 清除SearchView的焦点
                svGameInfo.clearFocus();
                // 请求数据
                current = 1;
                mSearch = selectedQuery;
                getGameInfo(mSearch, current, size);
            }
        });
    }

    private void initView() {
        rvGameInfo = findViewById(R.id.rv_game_info);
        srlGameInfo = findViewById(R.id.srl_game_info);
        svGameInfo = findViewById(R.id.sv_game_info);
        lvSearchGameInfo = findViewById(R.id.lv_search_game_info);
        lvSearchGameInfo.setAdapter(searchAdapter);
        // 隐藏 ListView focus的时候才有焦点
        lvSearchGameInfo.setVisibility(View.GONE);
        ivNetworkOut = findViewById(R.id.iv_network_out);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionHelper.REQUEST_CODE) {
            boolean allGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                // 权限被授予
                Toast.makeText(this, "权限已授予", Toast.LENGTH_SHORT).show();
                // 执行相应的操作
                if (permissions.length > 0 && permissions[0].equals(Manifest.permission.CAMERA)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivity(intent);
                } else if (permissions.length > 0 && (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE) || permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE))) {
                    Toast.makeText(this, "可以访问SD卡", Toast.LENGTH_SHORT).show();
                }
            } else {
                // 权限被拒绝
                Toast.makeText(this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                    // 权限被永久拒绝
                    new AlertDialog.Builder(this)
                            .setTitle("需要权限")
                            .setMessage("应用需要权限来执行此操作。请在设置中手动开启权限。")
                            .setPositiveButton("去设置", (dialog, which) -> {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            })
                            .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                            .create()
                            .show();
                }
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (!query.trim().isEmpty()) {
            // 如果列表中已经包含此查询
            if (searchInfoList.contains(query)) {
                searchInfoList.remove(query);
            }

            // 添加新的查询到列表
            searchInfoList.add(0, query);

            // 只保留最近的五个搜索记录
            if (searchInfoList.size() > 5) {
                searchInfoList.remove(searchInfoList.size() - 1);
            }

            // 更新存储的数据
            searchInfoSet = new HashSet<>(searchInfoList);
            mmkv.encode(KEY_SEARCH_LIST_INFO, searchInfoSet);

            searchAdapter.notifyDataSetChanged();
        }
        // 调用数据请求方法刷新列表
        // 重置当前页数为1
        current = 1;
        mSearch = query;
        // 需要刷新
        requestCondition = 1;
        // 请求新的搜索数据
        getGameInfo(mSearch, current, size);
        return false;
    }



    @Override
    public boolean onQueryTextChange(String newText) {
        mSearch = newText;
        // 调用数据请求方法刷新列表
        current = 1;
        // 需要刷新
        requestCondition = 1;
        getGameInfo(mSearch, current, size);

        return false;
    }


}
