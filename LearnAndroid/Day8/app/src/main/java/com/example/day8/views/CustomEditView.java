package com.example.day8.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * @author wellorbetter
 */
public class CustomEditView extends EditText {
    private Paint mPaint;
    private Rect mRect;
    public CustomEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mRect = new Rect();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(0x800000FF);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        int count = getLineCount();
        Rect r = mRect;
        Paint paint = mPaint;

        for (int i = 0; i < count; i ++ ) {
            int baseline = getLineBounds(i, r);
            canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
        }
        super.onDraw(canvas);
    }
}
