package com.example.day6.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * PermissionService类，用于处理动态权限请求
 * @author wellorbetter
 */
public class PermissionService {
    /**
     * 动态申请权限的返回码
     */
    public static final int PERMISSIONS_RESULT_CODE = 121;

    /**
     * 需要申请的权限列表
     */
    private final String[] permissions = new String[]{
            Manifest.permission.INTERNET
    };

    /**
     * 单例实例
     */
    private static PermissionService instance;
    private final Context context;

    /**
     * 私有构造函数，防止外部实例化
     *
     * @param context 上下文，申请权限需要
     */
    private PermissionService(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * 获取单例实例的静态方法
     *
     * @param context 上下文，申请权限需要
     * @return 单例模式的实例
     */
    public static synchronized PermissionService getInstance(Context context) {
        if (instance == null) {
            instance = new PermissionService(context);
        }
        return instance;
    }

    /**
     * 申请权限
     *
     * @param activity 当前的Activity实例，用于请求权限
     */
    public void checkPermission(FragmentActivity activity) {
        // SDK版本小于23时，不需要动态申请权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        // 检查未授权的权限
        List<String> pl = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                pl.add(permission);
            }
        }

        // 申请未授权的权限
        if (!pl.isEmpty()) {
            ActivityCompat.requestPermissions(activity, pl.toArray(new String[0]), PERMISSIONS_RESULT_CODE);
        }
    }
}
