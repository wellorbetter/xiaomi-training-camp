package com.example.day8.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.day8.R;

/**
 * @author wellorbetter
 */
public class CustomView extends View {
    Paint mPaint;
    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 文字
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(42);
        canvas.drawText("这是画的文字", 100, 100, mPaint);

        // 直线
        // 抗锯齿，不加也看不太出来
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10);
        canvas.drawLine(100, 150, 300, 350, mPaint);

        // 空心圆
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(200, 250,80, mPaint);
        // 空心矩形
        canvas.drawRect(100, 400, 400, 500, mPaint);
        // 保存画布状态
        canvas.save();
        // 实心
        mPaint.setStyle(Paint.Style.FILL);
        // 裁剪区域
        canvas.clipRect(new Rect(100, 550, 300, 750));
        canvas.drawColor(Color.LTGRAY);
        canvas.drawCircle(150, 600, 100, mPaint);
        // 恢复状态
        canvas.restore();
        // 圆弧矩形
        mPaint.setStyle(Paint.Style.STROKE);
        RectF rectF = new RectF(100, 800, 400, 900);
        canvas.drawRoundRect(rectF, 20, 20, mPaint);
        // 点
        canvas.drawPoint(400, 200, mPaint);
        canvas.drawPoints(new float[]{400, 220, 420, 220, 440, 220}, mPaint);

        // 图片
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.android_icon);
        canvas.drawBitmap(bitmap, 500, 150, mPaint);

        // Path
        Path path = new Path();
        // 移动画笔 起点
        path.moveTo(400, 300);
        // 绘制直线
        path.lineTo(1000, 350);
        path.moveTo(400, 310);
        // 圆滑曲线/贝塞尔曲线
        path.quadTo(650, 400, 400, 600);
        path.moveTo(400, 610);
        // 同样是贝塞尔曲线
        path.cubicTo(400, 700, 500, 550, 600, 900);
        path.moveTo(600, 1000);
        RectF mRectF = new RectF(400, 900, 600, 1100);
        // 绘制弧线 （截取圆/椭圆的一部分）
        path.arcTo(mRectF, 0, 270);
        canvas.drawPath(path, mPaint);
        // 圆弧
        RectF rectF1 = new RectF(100, 900, 200, 1000);
        canvas.drawArc(rectF1, 0, 120, false, mPaint);

        // 沿着坐标绘制文字
        mPaint.setTextSize(32);
        mPaint.setStrokeWidth(1);
        canvas.drawPosText("Hello world", new float[]{20, 20, 20, 20, 40, 40, 50, 50, 60, 60, 70, 70, 80, 60, 90, 50, 100, 40, 110, 30, 120, 20}, mPaint);


        // 沿着路径绘制文字
        Path path2 = new Path();
        path2.moveTo(400, 300);
        path.lineTo(1000, 200);
        canvas.drawTextOnPath("1591008610010", path2, 50, -30, mPaint);
    }
}
