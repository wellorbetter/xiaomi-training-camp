package com.example.day5.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.day5.R;

public class ClassFragment2 extends Fragment {


    public ClassFragment2() {
    }


    public static ClassFragment2 newInstance(String param1, String param2) {
        ClassFragment2 fragment = new ClassFragment2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class2, container, false);
    }
}