package com.example.day5.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.day5.R;

public class ClassFragment4 extends Fragment {


    public ClassFragment4() {
    }


    public static ClassFragment4 newInstance(String param1, String param2) {
        ClassFragment4 fragment = new ClassFragment4();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_class4, container, false);
    }
}