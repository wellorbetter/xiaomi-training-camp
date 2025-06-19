package com.example.weibo_wudongsheng.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.weibo_wudongsheng.MyApp;
import com.example.weibo_wudongsheng.R;
import com.example.weibo_wudongsheng.adapter.HomePageItemAdapter;
import com.example.weibo_wudongsheng.adapter.ViewPagerAdapter;
import com.example.weibo_wudongsheng.bean.UserInfo;
import com.example.weibo_wudongsheng.bean.UserInfoResponse;
import com.example.weibo_wudongsheng.fragment.MineFragment;
import com.example.weibo_wudongsheng.fragment.RecommendFragment;
import com.example.weibo_wudongsheng.fragment.RequestFailedFragment;
import com.example.weibo_wudongsheng.utils.WeiboRequestHelper;
import com.example.weibo_wudongsheng.utils.retrofit.ApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wellorbetter
 * 用户主页面，包含推荐页和个人界面
 * 处理推荐页面和个人页面的显示，以及用户数据的获取
 */
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {

    // UI控件
    private BottomNavigationView mBottomNavigation;
    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    // 数据相关
    private MMKV mMMKV;
    private String mLoginToken = "";
    private UserInfo mUserInfo;

    // 页面相关
    private List<Fragment> mPages;
    private RecommendFragment mRecommendFragment;
    private MineFragment mMineFragment;
    private RequestFailedFragment mRequestFailedFragment;
    private WeiboRequestHelper weiboRequestHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        initEvent();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        weiboRequestHelper = new WeiboRequestHelper(getApplicationContext());
        EventBus.getDefault().register(this);
        mMMKV = MMKV.defaultMMKV();
        mPages = new ArrayList<>();
        mRecommendFragment = new RecommendFragment(this.getApplicationContext());
        // 这个时候还没有传入数据，也就是说，这里是默认用户状态
        mMineFragment = new MineFragment();
        mPages.add(mRecommendFragment);
        mPages.add(mMineFragment);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mPages);
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        mViewPager.addOnPageChangeListener(this);
        mBottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mBottomNavigation = findViewById(R.id.nv_main);
        mViewPager = findViewById(R.id.vp_main);
        mBottomNavigation.setSelectedItemId(R.id.nav_recommend);
        mViewPager.setAdapter(mViewPagerAdapter);
        // 这里尝试自动登录
        getSelfInfo(mMMKV.getString("loginToken", ""));
    }

    /**
     * 处理底部导航栏和ViewPager的联动
     *
     * @param item 当前选中的底部按钮
     * @return 是否处理该事件
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_recommend) {
            mViewPager.setCurrentItem(0);
            return true;
        } else if (id == R.id.nav_mine) {
            mViewPager.setCurrentItem(1);
            return true;
        }
        return false;
    }

    /**
     * 根据传入的loginToken进行网络请求，获取用户信息并更新界面
     *
     * @param loginToken 用户的登录token
     */
    public void getSelfInfo(String loginToken) {
        Call<UserInfoResponse> call = ApiClient.getApiService().getSelfInfo("Bearer " + loginToken);
        call.enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                if (response.isSuccessful() && response.body().getData().getId() != null) {
                    loginRequestSuccess(response);
                } else {
                    if (response.message() != null && !response.message().isEmpty()) {
                        Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "请检查网络状况!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 登录请求成功
     * @param response 返回体
     */
    private void loginRequestSuccess(Response<UserInfoResponse> response) {
        // 请求成功处理
        UserInfoResponse userInfoResponse = response.body();
        if (userInfoResponse.getCode() == 200) {
            // 登录成功，写入消息
            loginSuccess(userInfoResponse);
            MyApp.isLogin = true;
        } else if (userInfoResponse.getCode() == 403) {
            weiboRequestHelper.tokenExpired();
        } else {
            Toast.makeText(getApplicationContext(), userInfoResponse.getMsg(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 成功登录 替换数据
     * @param userInfoResponse  成功请求的用户数据
     */
    private void loginSuccess(UserInfoResponse userInfoResponse) {
        Toast.makeText(getApplicationContext(), "登陆成功!欢迎您" + userInfoResponse.getData().getUsername(), Toast.LENGTH_SHORT).show();
        mUserInfo = userInfoResponse.getData();
        mMineFragment = new MineFragment(mUserInfo);
        mPages.set(1, mMineFragment);
        mViewPagerAdapter.notifyDataSetChanged();
    }

    /**
     *
     * @param event 登陆成功会触发
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(String event) {
        // 请求成功之后
        if (event.equals("login_success")) {
            mLoginToken = mMMKV.getString("loginToken", "");
            if (!mLoginToken.isEmpty()) {
                getSelfInfo(mLoginToken);
                // 这里同时还要更新主页
                // 也就是重新创建RecommentFragment
                mRecommendFragment = new RecommendFragment(this.getApplicationContext());
                // 然后替换
                mPages.set(0, mRecommendFragment);
                mViewPagerAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁之前需要反注册EventBus，遵循规定流程
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 完成ViewPager和BottomNavigationView的联动
     *
     * @param position 当前第几个页面
     */
    @Override
    public void onPageSelected(int position) {
        mBottomNavigation.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

}
