package com.example.day5.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.day5.Adapter.LVAdapter;
import com.example.day5.Bean.QuesInfo;
import com.example.day5.R;

import java.util.List;

public class QuesInfoFragment extends Fragment {

    ListView lv;
    List<QuesInfo> items;
    LVAdapter adapter;
    public QuesInfoFragment() {
    }

    public QuesInfoFragment(List<QuesInfo> items) {
        this.items = items;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ques_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);
        initEvent();
    }

    private void initData() {
    }

    private void initEvent() {

    }

    private void initView(View view) {
        lv = view.findViewById(R.id.lv);
        lv.setAdapter(new LVAdapter(getContext(), items));
    }
}