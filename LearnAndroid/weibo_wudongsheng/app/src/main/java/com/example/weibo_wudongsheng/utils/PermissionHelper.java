package com.example.weibo_wudongsheng.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

/**
 * @author wellorbetter
 */
public class PermissionHelper {
    Context mContext;
    List<String> mRequestPermissions;
    public static final int REQUEST_CODE = 200;

    public PermissionHelper(Context mContext, List<String> mRequestPermissions) {
        this.mContext = mContext;
        this.mRequestPermissions = mRequestPermissions;
    }

    public boolean checkPermission(String mRequestPermission) {
        return ContextCompat.checkSelfPermission(mContext, mRequestPermission) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermission(String mRequestPermission) {
        if (!checkPermission(mRequestPermission)) {
            // 如果没有权限，申请权限
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{mRequestPermission}, REQUEST_CODE);
        }
    }

    public void requestPermissions() {
        for (String mRequestPermission : mRequestPermissions) {
            if (!checkPermission(mRequestPermission)) {
                ActivityCompat.requestPermissions((Activity) mContext, mRequestPermissions.toArray(new String[0]), REQUEST_CODE);
                break;
            }
        }
    }
}
