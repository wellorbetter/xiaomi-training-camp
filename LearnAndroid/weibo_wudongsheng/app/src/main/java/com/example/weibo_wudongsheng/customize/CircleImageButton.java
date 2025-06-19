package com.example.weibo_wudongsheng.customize;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageButton;

/**
 * @author wellorbetter
 *
 * 自定义圆形ImageButton
 */

public class CircleImageButton extends AppCompatImageButton {

    /**
     *  mPaint    画笔
     *  mRadius   圆形图片的半径
     *  mScale    图片的缩放比例
     */
    private Paint mPaint;
    private int mRadius;
    private float mScale;

    public CircleImageButton(Context context) {
        super(context);
        init();
    }

    public CircleImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        // 设置抗锯齿
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 由于是圆形，宽高应保持一致
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        // 画圆的半径 = min(宽, 长) / 2
        mRadius = size / 2;
        // 约定俗成 保存
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();

        if (drawable != null) {
            // 拿到画架
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            // 初始化BitmapShader，传入bitmap对象
            BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            // 计算缩放比例
            mScale = (mRadius * 2.0f) / Math.min(bitmap.getHeight(), bitmap.getWidth());

            Matrix matrix = new Matrix();
            matrix.setScale(mScale, mScale);
            bitmapShader.setLocalMatrix(matrix);
            mPaint.setShader(bitmapShader);
            // 画圆形，指定好坐标，半径，画笔
            canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        } else {
            super.onDraw(canvas);
        }
    }
}
