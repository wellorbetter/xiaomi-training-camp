package com.example.day5.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.day5.R;

public class ClassFragment3 extends Fragment {


    public ClassFragment3() {
    }


    public static ClassFragment3 newInstance(String param1, String param2) {
        ClassFragment3 fragment = new ClassFragment3();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class3, container, false);
    }
}