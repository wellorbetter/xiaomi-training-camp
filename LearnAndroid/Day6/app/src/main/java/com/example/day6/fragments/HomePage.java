package com.example.day6.fragments;// HomePage.java

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.day6.R;
import com.example.day6.adapters.HomePageAdapter;
import com.example.day6.beans.Item;
import com.example.day6.beans.NewsItem;
import com.example.day6.utils.ConvertUtil;
import com.example.day6.utils.DateUtil;
import com.example.day6.utils.NewsService;

import java.text.ParseException;
import java.util.List;

public class HomePage extends Fragment {
    private List<Item> mItems;
    private NewsItem mNewsItems;
    private RecyclerView mRecyclerview;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private HomePageAdapter adapter;
    private Context mContext;
    private String mTopDate;
    private String mNowDate;
    private String mBottomDate;
    private boolean isRefreshing;
    private int daysLimit = 30;

    public HomePage(Context mContext, List<Item> mItems, String mTopDate) {
        this.mItems = mItems;
        this.mContext = mContext;
        this.mTopDate = mTopDate;
        this.mBottomDate = mTopDate;
        this.mNowDate = mTopDate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);
        loadData(mTopDate);
    }

    private void initData() {
        mNewsItems = new NewsItem();
        adapter = new HomePageAdapter(mItems);
    }

    private void initView(View view) {
        mRecyclerview = view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this::refresh);
        adapter.getLoadMoreModule().setEnableLoadMoreIfNotFullPage(false);
        adapter.getLoadMoreModule().setOnLoadMoreListener(this::loadMore);
        mRecyclerview.setAdapter(adapter);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void loadMore() {
        try {
            String previousDate = DateUtil.getInstance().getPreviousDate(mBottomDate);
            mBottomDate = previousDate;
            if (DateUtil.getInstance().daysBetween(mNowDate, previousDate) > daysLimit) {
                adapter.getLoadMoreModule().loadMoreEnd(); // 超过30天，不再加载更多
                Toast.makeText(getContext(), "加载完毕", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("loadmore previousDate", NewsService.API_PREFIX + previousDate);
            loadData(previousDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void loadData(String date) {

        Log.d("loadmore", NewsService.API_PREFIX + date);
        NewsService.getInstance().getTargetNews(NewsService.API_PREFIX + date).thenAccept(newsItem -> {
            boolean isTop;
            mNewsItems = newsItem;
            if (isRefreshing) {
                ConvertUtil.getInstance().convertData(mContext, mItems, mNewsItems, true);
                isTop = true;
            } else {
                ConvertUtil.getInstance().convertData(mContext, mItems, mNewsItems, false);
                isTop = false;
            }

            getActivity().runOnUiThread(() -> {
                adapter.setNewData(mItems); // 设置新数据
                if (isTop) {
                    // 只保存前15条数据
                    mItems = mItems.subList(0, 15);
                    adapter.notifyItemRangeChanged(0, mItems.size());
                } else {
                    // 每次五条，添加在最后只需要更新最后五条即可
                    adapter.notifyItemRangeChanged(mItems.size() - 5 - 1,  mItems.size());
                }
                if (isRefreshing) {
                    mSwipeRefreshLayout.setRefreshing(false); // 停止刷新
                    isRefreshing = false; // 重置刷新状态
                }
                adapter.getLoadMoreModule().loadMoreComplete(); // 加载更多完成
            });
        }).exceptionally(throwable -> {
            Log.e("loadmore", "Error loading more news: " + throwable.getMessage());
            getActivity().runOnUiThread(() -> {
                adapter.getLoadMoreModule().loadMoreFail(); // 加载更多失败
                if (isRefreshing) {
                    mSwipeRefreshLayout.setRefreshing(false); // 停止刷新
                    isRefreshing = false; // 重置刷新状态
                }
            });
            return null;
        });
    }

    private void refresh() {
        isRefreshing = true;
        try {
            String nextDate = DateUtil.getInstance().getPreviousDate(mTopDate);
            mTopDate = nextDate;
            if (DateUtil.getInstance().daysBetween(nextDate, nextDate) > daysLimit) {
                adapter.getLoadMoreModule().loadMoreEnd(); // 超过30天，不再加载更多
                Toast.makeText(getContext(), "加载完毕", Toast.LENGTH_SHORT).show();
                return;
            }
            Log.d("loadmore nextDate", NewsService.API_PREFIX + nextDate);
            loadData(nextDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
