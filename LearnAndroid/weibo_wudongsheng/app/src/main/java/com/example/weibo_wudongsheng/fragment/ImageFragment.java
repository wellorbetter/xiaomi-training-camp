package com.example.weibo_wudongsheng.fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.weibo_wudongsheng.R;

public class ImageFragment extends Fragment {
    ImageView iv;

    public ImageFragment() {

    }
    private String img;
    public ImageFragment(String img) {
        this.img = img;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvent();
    }

    private void initEvent() {
        iv.setOnClickListener(v -> {
            getActivity().finish();
        });
    }

    private void initView(View view) {
        iv = view.findViewById(R.id.iv);
        Glide.with(getContext())
                .load(Uri.parse(img))
                .into(iv);
    }
}