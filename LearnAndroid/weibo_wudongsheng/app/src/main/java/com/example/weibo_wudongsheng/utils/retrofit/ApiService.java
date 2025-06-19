package com.example.weibo_wudongsheng.utils.retrofit;

import com.example.weibo_wudongsheng.bean.HomePageResponse;
import com.example.weibo_wudongsheng.bean.LikeRequest;
import com.example.weibo_wudongsheng.bean.LikeResponse;
import com.example.weibo_wudongsheng.bean.LoginRequest;
import com.example.weibo_wudongsheng.bean.LoginResponse;
import com.example.weibo_wudongsheng.bean.UserInfoResponse;
import com.example.weibo_wudongsheng.bean.VerifyNumberResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Retrofit API 接口
 */
public interface ApiService {

    // 发送验证码接口
    @POST("weibo/api/auth/sendCode")
    Call<VerifyNumberResponse> getVerifyNumber(@Body String phone);

    @POST("weibo/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("weibo/api/user/info")
    Call<UserInfoResponse> getSelfInfo(@Header("Authorization") String authorizationHeader);

    @GET("weibo/homePage")
    Call<HomePageResponse> getHomePageInfo(
            @Header("Authorization") String authorizationHeader,
            @Query("current") int current,
            @Query("size") int size
    );

    // 点赞接口
    @POST("weibo/like/up")
    Call<LikeResponse> likeWeibo(
            @Header("Authorization") String authorizationHeader,
            @Body LikeRequest likeRequest
    );

    // 取消点赞接口
    @POST("weibo/like/down")
    Call<LikeResponse> unlikeWeibo(
            @Header("Authorization") String authorizationHeader,
            @Body LikeRequest likeRequest
    );
}
