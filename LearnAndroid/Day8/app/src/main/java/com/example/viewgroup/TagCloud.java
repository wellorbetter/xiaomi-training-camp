package com.example.viewgroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TimeUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.Loader;

import com.example.day8.R;
import com.example.day8.beans.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @author wellorbetter
 */
public class TagCloud extends FrameLayout {
    private static String TAG = "TagCloud";
    private float mHorizontalMargin;
    private float mVerticalMargin;
    private List<String> mTags;
    private GestureDetector mDetector;
    private View mSelectView;
    private View mShadowView;
    private int mShadowIndex = -1;
    private int secondIndex;
    public TagCloud(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagCloud);
        mVerticalMargin = a.getDimension(R.styleable.TagCloud_vMargin, 20);
        mHorizontalMargin = a.getDimension(R.styleable.TagCloud_hMargin, 10);
        mShadowView = new TextView(getContext());
        mShadowView.setBackground(getResources().getDrawable(R.color.grey_200));
        ((TextView)mShadowView).setText("这是阴影区域");
        a.recycle();
        mDetector = new GestureDetector(this.getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(@NonNull MotionEvent motionEvent) {
                RectF r = new RectF();
                for (int i = 0; i < getChildCount(); i ++ ) {
                    View view = getChildAt(i);
                    r.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
                    if (r.contains(motionEvent.getX(), motionEvent.getY())) {
                        select(view, i);
                        break;
                    }
                }
                return true;
            }

            @Override
            public void onShowPress(@NonNull MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
                return false;
            }
            class Center {
                int x, y;

            }
            @Override
            public boolean onScroll(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
                if (mSelectView != null) {
                    if (motionEvent1.getPointerCount() != 1) {
                        // 如果有两根手指，让位置移动到第二根手指，用第二根手指的位置和原始view的位置进行计算
                        int pointerIndex = secondIndex;
                        float x = motionEvent1.getX(pointerIndex);
                        float y = motionEvent1.getY(pointerIndex);
                        int[] pos = new int[2];
                        mSelectView.getLocationOnScreen(pos);
                        int mX = pos[0], my = pos[1];
                        Log.d(TAG, " " + mX + " " + my + " " + x + " " + y);
                        scrollSelectView(mX - x, my - y);
//                        Log.d(TAG, "onTouchEvent: 111" + pointerIndex);
                    }
                    else{
                        scrollSelectView(v, v1);
                    }
                    int heightIndex = 0;
                    float heightDiff = 1000000;

                    // 根据当前select的绘制位置来决定它的实际位置插入
                    for (int i = 0; i < getChildCount(); i ++ ) {
                        View view = getChildAt(i);
                        // 找到最近的一行
                        if (Math.abs(mSelectView.getY() - view.getY()) < heightDiff && mSelectView != view) {
                            heightDiff = Math.abs(mSelectView.getY() - view.getY());
                            heightIndex = i;
                        }
                    }

                    // 在这一行中寻找插入的位置
                    List<Float> height = new ArrayList<>();
                    for (int i = 0; i < getChildCount(); i ++ ) {
                        View view = getChildAt(i);
                        if (Math.abs(view.getY() - getChildAt(heightIndex).getY()) < 0.01) {
                            height.add(view.getX());
                        }
                    }
                    removeView(mShadowView);
                    int pos = Collections.binarySearch(height, mSelectView.getX());

                    if (pos < 0) {
                        pos = -(pos + 1); // 获取插入点
                    }
                    mShadowIndex = heightIndex + pos;
                    if (heightIndex + pos < 0) {
                        mShadowIndex = 0;
                    } else if (heightIndex + pos >= getChildCount()) {
                        mShadowIndex = getChildCount();
                        addView(mShadowView);
                        requestLayout();
                        invalidate();
                        return true;
                    }

                    addView(mShadowView, mShadowIndex);
                    requestLayout();
                    invalidate();
                    return true;
                }
                return false;
            }

            @Override
            public void onLongPress(@NonNull MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
                release();
                return true;
            }
        });
    }

    private void select(View v, int index) {
        //Log.d(TAG, "select: " + index);
        mSelectView = v;
        ScaleAnimation sa = new ScaleAnimation(1, 1.2f, 1, 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        sa.setDuration(200);
        sa.setFillAfter(true);
        v.startAnimation(sa);
    }
    public TagCloud(@NonNull Context context) {
        super(context);
    }
    public void setTags(List<String> tags) {
        if (mTags != tags) {
            mTags = tags;
            int tagCount = mTags != null ? mTags.size() : 0;
            int childCount = getChildCount();
            if (tagCount > childCount) {
                for (int i = childCount; i < tagCount; i ++ ) {
                    TextView child = new TextView(getContext());
                    child.setTextSize(25);
                    child.setMaxLines(1);
                    child.setEllipsize(TextUtils.TruncateAt.END);
                    child.setBackgroundColor(getResources().getColor(R.color.purple_200));
                    addView(child, i);
                }
            } else if (tagCount < childCount) {
                for (int i = childCount; i > tagCount; i -- ) {
                    removeViewAt(i - 1);
                }
            }
            for (int i = 0; i < getChildCount(); i ++ ) {
                ((TextView)getChildAt(i)).setText(mTags.get(i));
            }
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int childWidthSpec;
        switch (widthSpecMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                childWidthSpec = MeasureSpec.makeMeasureSpec(widthSpecSize - (int)mHorizontalMargin * 2, MeasureSpec.AT_MOST);
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                childWidthSpec = widthMeasureSpec;
                break;
        }

        int childHeightSpec;
        switch (heightSpecMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                childHeightSpec = MeasureSpec.makeMeasureSpec(heightSpecSize - (int)mVerticalMargin * 2, MeasureSpec.AT_MOST);
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                childHeightSpec = heightMeasureSpec;
        }

        int height = 0;
        int remainWidth = 0;
        int top = 0;

        for (int i = 0; i < getChildCount(); i ++ ) {
            View child = getChildAt(i);
            // 我选中的时候不绘制，根据它的位置插入一个阴影，最后抬起的时候再删除阴影加入这个选中的
            if (child == mSelectView) {
                continue;
            }
            child.measure(childWidthSpec, childHeightSpec);
            int l, t, r, b;
            if (height == 0 || remainWidth + mHorizontalMargin + child.getMeasuredWidth() > widthSpecSize) {
                t = height + (int) mVerticalMargin;
                top = t;

                height += mVerticalMargin + child.getMeasuredHeight();
                b = height;

                remainWidth = (int) mHorizontalMargin;
                l = remainWidth;

                remainWidth += child.getMeasuredWidth();
                r = remainWidth;
            } else {
                t = top;
                b = top + child.getMeasuredHeight();
                l = remainWidth + (int) mHorizontalMargin;
                remainWidth += mHorizontalMargin + child.getMeasuredWidth();
                r = remainWidth;
            }
            Location location = new Location(l, t, r, b);
            child.setTag(location);
        }
        setMeasuredDimension(widthSpecSize, heightSpecSize);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        layoutChildren();
    }

    private void layoutChildren() {
        for (int i = 0; i < getChildCount(); i ++ ) {
            View child = getChildAt(i);
            Location location = (Location) child.getTag();
            child.layout(location.l, location.t, location.r, location.b);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_UP) {
            if (mSelectView != null) {
                release();
                return true;
            }
        }
        else if (action == MotionEvent.ACTION_POINTER_DOWN) {
            if (mSelectView != null) {
                int pointerIndex = event.getActionIndex();
                // 按下第二根手指之后在gesturedetector里面处理逻辑
                secondIndex = pointerIndex;
//                float x = event.getX(pointerIndex);
//                float y = event.getY(pointerIndex);
//                int[] pos = new int[2];
//                mSelectView.getLocationOnScreen(pos);
//                int mX = pos[0], my = pos[1];
//                Log.d(TAG, " " + mX + " " + my + " " + x + " " + y);
//                scrollSelectView(mX - x, my - y);
//                Log.d(TAG, "onTouchEvent: 111" + pointerIndex);
                return true;
            }
        }
        return mDetector.onTouchEvent(event);
    }

    private void scrollSelectView(float disX, float disY) {
        mSelectView.setTranslationX(mSelectView.getTranslationX() - disX);
        mSelectView.setTranslationY(mSelectView.getTranslationY() - disY);
    }
//    private void scrollSelectView(float disX, float disY) {
//        Location location = (Location) mSelectView.getTag();
//        int newLeft = location.l - (int) disX;
//        int newTop = location.t - (int) disY;
//        int newRight = location.r - (int) disX;
//        int newBottom = location.b - (int) disY;
//        mSelectView.layout(newLeft, newTop, newRight, newBottom);
//        mSelectView.setTag(new Location(newLeft, newTop, newRight, newBottom));
//        Log.d(TAG, "scrollSelectView: " + mSelectView.getLeft() + " " + mSelectView.getTop());
//    }

    private void release() {
        if (mSelectView != null) {
            mSelectView.clearAnimation();
            mSelectView.setTranslationX(0);
            mSelectView.setTranslationY(0);
            removeView(mShadowView);
            removeView(mSelectView);
            // 写的实在是丑陋，算法学得太烂了
            if (mShadowIndex >= getChildCount()) {
                addView(mSelectView);
            } else if (mShadowIndex < 0){
                addView(mSelectView, 0);
            } else {
                addView(mSelectView, mShadowIndex);
            }
            mSelectView = null;
        }
    }
}
