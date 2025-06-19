package com.example;

/**
 * @author wellorbetter
 */
import android.app.Application;
import com.tencent.mmkv.MMKV;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化 MMKV
        String rootDir = MMKV.initialize(this);
        // 自定义路径
        // String rootDir = MMKV.initialize(this, "/path/to/your/directory");
    }
}
