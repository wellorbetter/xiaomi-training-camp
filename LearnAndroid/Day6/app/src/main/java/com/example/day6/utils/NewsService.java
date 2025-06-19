package com.example.day6.utils;

/**
 * @author wellorbetter
 */
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import com.example.day6.beans.NewsItem;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class NewsService {
    private static final String TAG = "NewsService";
    public static final String API_PREFIX = "https://news-at.zhihu.com/api/4/news/before/";
    /**
     * client 客户端对象
     * objectMapper 对象映射器
     * instance 单例实例
     */
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private static NewsService instance;
    private NewsService() {
        // 设置 OkHttp 日志拦截器，用于记录和调试网络请求和响应
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 初始化 OkHttp 客户端
        client = new OkHttpClient.Builder()
                // 添加日志拦截器
                .addInterceptor(logging)
                .build();

        // 初始化 Jackson 对象映射器
        objectMapper = new ObjectMapper();
    }

    /**
     * 获取单例实例的静态方法
     *
     * @return 返回单例模式实例
     */
    public static synchronized NewsService getInstance() {
        if (instance == null) {
            instance = new NewsService();
        }
        return instance;
    }


    /**
     * 获取指定日期的知乎日报数据
     *
     * @return 包含最新知乎日报数据的 NewsItem 对象
     * @throws IOException 网络请求或解析异常
     */
    public CompletableFuture<NewsItem> getTargetNews(String url) {
        CompletableFuture<NewsItem> future = new CompletableFuture<>();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.e(TAG, "Network request failed", e);
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Unexpected code: " + response.code() + ", Response: " + response.body().string());
                    future.completeExceptionally(new IOException("Unexpected code " + response));
                    return;
                }
                String responseBody = response.body() == null ? null : response.body().string();
                if (responseBody != null) {
                    NewsItem newsItem = objectMapper.readValue(responseBody, NewsItem.class);
                    future.complete(newsItem);
                } else {
                    Log.d(TAG, "Response body is null");
                    future.completeExceptionally(new IOException("Response body is null"));
                }
            }
        });
        return future;
    }
    /**
     * 获取最新的知乎日报数据
     *
     * @return 包含最新知乎日报数据的 NewsItem 对象
     * @throws IOException 网络请求或解析异常
     */
    public CompletableFuture<NewsItem> getLatestNews() {
        CompletableFuture<NewsItem> future = new CompletableFuture<>();
        String url = "https://news-at.zhihu.com/api/4/news/latest";
        return getTargetNews(url);
    }
}
