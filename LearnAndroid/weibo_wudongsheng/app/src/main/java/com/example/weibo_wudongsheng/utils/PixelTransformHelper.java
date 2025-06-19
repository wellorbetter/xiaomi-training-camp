package com.example.weibo_wudongsheng.utils;

import android.content.Context;

/**
 * @author wellorbetter
 * dp转像素的工具类
 */
public class PixelTransformHelper {
    private Context mContext;

    public static PixelTransformHelper pixelTransformHelper;
    public static PixelTransformHelper newInstance(Context context) {
        if (pixelTransformHelper == null) {
            synchronized (PixelTransformHelper.class) {
                if (pixelTransformHelper == null) {
                    pixelTransformHelper = new PixelTransformHelper(context.getApplicationContext());
                }
            }
        }
        return pixelTransformHelper;
    }
    private PixelTransformHelper(Context mContext) {
        this.mContext = mContext;
    }

    public int dp2Pixel(int pixel) {
        return (int)(mContext.getResources().getDisplayMetrics().density* pixel);
    }

}
