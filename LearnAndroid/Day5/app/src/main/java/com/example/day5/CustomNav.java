package com.example.day5;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustomNav extends BottomNavigationView {
    public CustomNav(@NonNull Context context) {
        super(context);
    }

    public CustomNav(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getMaxItemCount() {
        return 8;
    }
}
