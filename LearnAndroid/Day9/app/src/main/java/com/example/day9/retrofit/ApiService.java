package com.example.day9.retrofit;

import com.example.day9.beans.SearchResponseData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author wellorbetter
 */
public interface ApiService {

    /**
     *
     * @param search    搜索功能接口定义
     * @param current   当前页面
     * @param size      当前页面数据个数
     * @return          根据当前页面和页面数据个数返回一定范围的数据
     */
    @GET("search")
    Call<SearchResponseData> search(
            @Query("search") String search,
            @Query("current") int current,
            @Query("size") int size
    );
}