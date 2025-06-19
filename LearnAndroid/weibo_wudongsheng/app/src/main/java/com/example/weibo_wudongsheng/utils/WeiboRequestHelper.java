package com.example.weibo_wudongsheng.utils;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.weibo_wudongsheng.MyApp;
import com.example.weibo_wudongsheng.activity.LoginActivity;
import com.example.weibo_wudongsheng.bean.HomePageResponse;
import com.example.weibo_wudongsheng.bean.LikeRequest;
import com.example.weibo_wudongsheng.bean.LikeResponse;
import com.example.weibo_wudongsheng.bean.LoginRequest;
import com.example.weibo_wudongsheng.bean.LoginResponse;
import com.example.weibo_wudongsheng.bean.Page;
import com.example.weibo_wudongsheng.bean.WeiboInfo;
import com.example.weibo_wudongsheng.utils.retrofit.ApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeiboRequestHelper {

    private MMKV mmkv;
    private Context mContext;

    public WeiboRequestHelper(Context context) {
        this.mmkv = MMKV.defaultMMKV();
        this.mContext = context;
    }

    public void getHomePageInfo(String loginToken, int current, int size, OnHomePageInfoListener listener) {
        Call<HomePageResponse> call = ApiClient.getApiService()
                .getHomePageInfo("Bearer " + loginToken, current, size);

        call.enqueue(new Callback<HomePageResponse>() {
            @Override
            public void onResponse(Call<HomePageResponse> call, Response<HomePageResponse> response) {
                if (response.isSuccessful()) {
                    HomePageResponse homePageResponse = response.body();
                    if (homePageResponse != null && homePageResponse.getCode() == 200) {
                        Page page = homePageResponse.getData();
                        List<WeiboInfo> weiboInfoList = page.getRecords();
                        Collections.shuffle(weiboInfoList);
                        listener.onHomePageInfoSuccess(weiboInfoList);
                    } else if (homePageResponse != null && homePageResponse.getCode() == 403) {
                        listener.onTokenExpired();
                    } else {
                        listener.onFailure("数据请求失败!" + response.body().getMsg());
                    }
                } else {
                    listener.onFailure("数据请求失败!");
                }
            }

            @Override
            public void onFailure(Call<HomePageResponse> call, Throwable t) {
                listener.onFailure("请检查网络状况!");
            }
        });
    }
    public void login(String phoneNumber, String verifyNumber, OnLoginListener listener) {
        Call<LoginResponse> call = ApiClient.getApiService().login(new LoginRequest(phoneNumber, verifyNumber));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null && loginResponse.getCode() == 200) {
                        String loginToken = loginResponse.getData();
                        // 打错字了之前，坑死了
                        mmkv.encode("loginToken", loginToken);
                        mmkv.encode("isLogin", true);
                        EventBus.getDefault().post("login_success");
                        Toast.makeText(mContext, "登录成功!", Toast.LENGTH_SHORT).show();
                    } else {
                        EventBus.getDefault().post("login_failure");
                        Toast.makeText(mContext, "登录失败!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    EventBus.getDefault().post("login_failure");
                    Toast.makeText(mContext, "登录失败!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                listener.onFailure("请检查网络状况!");
            }
        });
    }
    public List<WeiboInfo> getDataFromMMKV() {
        String jsonData = mmkv.decodeString("weibo_data", null);
        if (jsonData != null) {
            Type listType = new TypeToken<List<WeiboInfo>>(){}.getType();
            return new Gson().fromJson(jsonData, listType);
        } else {
            return new ArrayList<>();
        }
    }

    public void saveDataToMMKV(List<WeiboInfo> data) {
        mmkv.encode("weibo_data", new Gson().toJson(data));
    }

    public void saveDataToMMKV(LoginResponse data) {
        // 这里别写错了，小坑bug
        mmkv.encode("loginToken", new Gson().toJson(data.getData()));
    }


    public void requestLogin(String phoneNumber, String verifyNumber) {
        this.login(phoneNumber, verifyNumber, new OnLoginListener() {
            @Override
            public void onLoginSuccess(LoginResponse loginResponse) {
                // 保存token
                saveDataToMMKV(loginResponse);
                MyApp.isLogin = true;
                // 登录成功后返回
                EventBus.getDefault().post("login_success");
            }

            @Override
            public void onFailure(String errorMessage) {
                // 请求失败，跳转到失败页面
                EventBus.getDefault().post("loading_failed");
                MyApp.isLogin = false;
            }

            @Override
            public void onTokenExpired() {
                tokenExpired();

            }
        });
    }

    public void requestHomePageInfo() {
        String hasData = "";
        // 是否有本地数据
        if (this.getDataFromMMKV().size() > 0) {
            hasData = "no_data";
        }
        // 是否可以请求数据
        String loginToken = MMKV.defaultMMKV().getString("loginToken", "");
        this.getHomePageInfo(loginToken, 1, 10, new WeiboRequestHelper.OnHomePageInfoListener() {
            @Override
            public void onHomePageInfoSuccess(List<WeiboInfo> weiboInfoList) {
                // 请求成功了 保存到本地
                saveDataToMMKV(weiboInfoList);
                // 然后发送消息请求跳转
                EventBus.getDefault().post("loading_success");
            }

            @Override
            public void onFailure(String errorMessage) {
                // 请求失败，跳转到失败页面
                EventBus.getDefault().post("loading_failed");
            }

            @Override
            public void onTokenExpired() {
                tokenExpired();
            }
        });
    }

    public void tokenExpired() {
        MyApp.isLogin = false;
        // 过期了
        MMKV.defaultMMKV().remove("loginToken");
        Toast.makeText(mContext, "登录状态已过期，请重新登录!", Toast.LENGTH_SHORT).show();
        // 跳转到登录页面
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
    }
    public void requestLike(Long id) {
        this.likeWeibo(id, new OnLikeListener() {
            @Override
            public void onLikeSuccess(LikeResponse likeResponse) {
                //
                Log.d("onLikeSuccess", "点赞成功");
            }

            @Override
            public void onFailure(String errorMessage) {
                // 请求失败，跳转到失败页面
                EventBus.getDefault().post("loading_failed");
                MyApp.isLogin = false;
            }

            @Override
            public void onTokenExpired() {
                tokenExpired();
            }
        });
    }


    public void likeWeibo(Long id, OnLikeListener listener) {
        String loginToken = mmkv.getString("loginToken", "");
        Call<LikeResponse> call = ApiClient.getApiService().likeWeibo(
                "Bearer " + loginToken,
                new LikeRequest(id));
        call.enqueue(new Callback<LikeResponse>() {
            @Override
            public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                if (response.isSuccessful()) {
                    LikeResponse loginResponse = response.body();
                    if (loginResponse != null && loginResponse.getCode() == 200) {
                        listener.onLikeSuccess(loginResponse);
                    } else if (loginResponse != null && loginResponse.getCode() == 403) {
                        listener.onTokenExpired();
                    } else {
                        listener.onFailure("数据请求失败!" + response.body().getMsg());
                    }
                } else {
                    listener.onFailure("数据请求失败!");
                }
            }

            @Override
            public void onFailure(Call<LikeResponse> call, Throwable t) {
                listener.onFailure("请检查网络状况!");
            }
        });
    }
    public void unLikeWeibo(Long id, OnLikeListener listener) {
        String loginToken = mmkv.getString("loginToken", "");
        Call<LikeResponse> call = ApiClient.getApiService().unlikeWeibo(
                "Bearer " + loginToken,
                new LikeRequest(id));
        call.enqueue(new Callback<LikeResponse>() {
            @Override
            public void onResponse(Call<LikeResponse> call, Response<LikeResponse> response) {
                if (response.isSuccessful()) {
                    LikeResponse loginResponse = response.body();
                    if (loginResponse != null && loginResponse.getCode() == 200) {
                        listener.onLikeSuccess(loginResponse);
                    } else if (loginResponse != null && loginResponse.getCode() == 403) {
                        listener.onTokenExpired();
                    } else {
                        listener.onFailure("数据请求失败!" + response.body().getMsg());
                    }
                } else {
                    listener.onFailure("数据请求失败!");
                }
            }

            @Override
            public void onFailure(Call<LikeResponse> call, Throwable t) {
                listener.onFailure("请检查网络状况!");
            }
        });
    }
    public void requestUnLikeWeibo(Long id) {
        this.unLikeWeibo(id, new OnLikeListener() {
            @Override
            public void onLikeSuccess(LikeResponse likeResponse) {
                Log.d("onLikeSuccess", "取消点赞成功");
            }

            @Override
            public void onFailure(String errorMessage) {
                // 请求失败，跳转到失败页面
                EventBus.getDefault().post("loading_failed");
                MyApp.isLogin = false;
            }

            @Override
            public void onTokenExpired() {
                tokenExpired();
            }
        });
    }
    public interface OnLikeListener {
        void onLikeSuccess(LikeResponse likeResponse);
        void onFailure(String errorMessage);
        void onTokenExpired();
    }

    public interface OnLoginListener {
        void onLoginSuccess(LoginResponse loginResponse);
        void onFailure(String errorMessage);
        void onTokenExpired();
    }
    public interface OnHomePageInfoListener {
        void onHomePageInfoSuccess(List<WeiboInfo> weiboInfoList);
        void onFailure(String errorMessage);
        void onTokenExpired();
    }
}
