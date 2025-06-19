package com.example.androidstudy;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class NetworkActivity extends Activity {
    private static final String TAG = "NetworkActivity";
    private static final int MSG_UPDATE_TIME_TICKER = 1;

    private final Handler handler = new MyHandler(Looper.getMainLooper(), new WeakReference<>(this));
    private class MyHandler extends Handler {
        private final WeakReference<NetworkActivity> reference;

        public MyHandler(@NonNull Looper looper, WeakReference<NetworkActivity> reference) {
            super(looper);
            this.reference = reference;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_UPDATE_TIME_TICKER) {
                reference.get().timeNumber = reference.get().timeNumber - 1;
                reference.get().updateTickerText();
                if (reference.get().timeNumber >= 1) {
                    sendMessageDelayed(Message.obtain(this, 1), 1000);
                }
            }
        }
    }
    private TextView textBody;
    private TextView timeTicker;
    private int timeNumber = 120;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        findViewById(R.id.getWithCallback).setOnClickListener(v -> getWithCallback());
        findViewById(R.id.retrofitGet).setOnClickListener(v -> retrofitGet());
        textBody = findViewById(R.id.body);
        timeTicker = findViewById(R.id.timeTicker);
        updateTickerText();
        handler.sendMessageDelayed(Message.obtain(handler, MSG_UPDATE_TIME_TICKER), 1000);

    }

    private void updateTickerText() {
        timeTicker.setText("倒计时：" + timeNumber + "s");
    }
    class mRetroCallback implements retrofit2.Callback<CommonData<GameItem>> {
        WeakReference<NetworkActivity> reference;

        public mRetroCallback(WeakReference<NetworkActivity> reference) {
            this.reference = reference;
        }
        @Override
        public void onResponse(retrofit2.Call<CommonData<GameItem>> call, retrofit2.Response<CommonData<GameItem>> response) {
            CommonData<GameItem> body = response.body();
            reference.get().handler.post(() -> reference.get().textBody.setText(body.toString()));
        }

        @Override
        public void onFailure(retrofit2.Call<CommonData<GameItem>> call, Throwable t) {

        }
    }

    private void retrofitGet() {
        retrofit2.Call<CommonData<GameItem>> commonDataCall = ServerApiManager.getInstance(this)
                .getApiService().queryGame("109");
        commonDataCall.enqueue(new mRetroCallback(new WeakReference<>(this)));
    }
    class mCallBack implements Callback {
        WeakReference<NetworkActivity> reference;

        public mCallBack(WeakReference<NetworkActivity> reference) {
            this.reference = reference;
        }

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            Log.e(TAG, "get onFailure: ", e);
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            String string = response.body().string();
            fromJson(string);
            Log.d(TAG, "get body: " + string);
            Log.d(TAG, "get headers: " + response.headers().toMultimap());
            reference.get().handler.post(() -> reference.get().textBody.setText(string));
        }


    }
    private void getWithCallback() {
        Request request = new Request.Builder().get().url("https://hotfix-service-prod.g.mi.com/quick-game/game/109").build();
        Call call = ServerApiManager.getInstance(getApplicationContext()).getOkHttpClient().newCall(request);
        call.enqueue(new mCallBack(new WeakReference<>(this)));
    }

    public void fromJson(String jsonStr) {
        Gson gson = ServerApiManager.getInstance(getApplicationContext()).getGson();
        Data data = gson.fromJson(jsonStr, Data.class);
        Log.d(TAG, "fromJson: data " + data);
        CommonData<GameItem> commonData = gson.fromJson(jsonStr, new TypeToken<CommonData<GameItem>>() {
        });
        Log.d(TAG, "fromJson: commonData " + commonData);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 移除所有消息和回调
        handler.removeCallbacksAndMessages(null);
    }

}