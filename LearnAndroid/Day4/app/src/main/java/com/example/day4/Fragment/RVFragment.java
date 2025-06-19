package com.example.day4.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.day4.Adapter.RVAdapter;
import com.example.day4.Bean.Data;
import com.example.day4.R;

import java.util.List;

public class RVFragment extends Fragment implements View.OnClickListener {
    List<Data> items;
    RecyclerView rv;
    Button bt_add ,bt_remove;
    private RVAdapter adapter;

    public RVFragment(List<Data> items) {
        this.items = items;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_r_v, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvent();
    }

    private void initEvent() {
        bt_add.setOnClickListener(this);
        bt_remove.setOnClickListener(this);
    }

    private void initView(View view) {
        rv = view.findViewById(R.id.rv);
        bt_add = view.findViewById(R.id.bt_add);
        bt_remove = view.findViewById(R.id.bt_remove);
        adapter = new RVAdapter(items);
        rv.setAdapter(adapter);
        // 非常非常非常非常关键的一点，记得加LayoutManager
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt_add) {
            Data item = new Data(getActivity().getDrawable(R.drawable.img), "这是添加的内容");
            items.add(0, item);
            adapter.notifyItemInserted(0);
            // 更新position
            adapter.notifyItemRangeChanged(0, items.size());
        } else if (id == R.id.bt_remove) {
            items.remove(0);
            adapter.notifyItemRemoved(0);
            // 更新position
            adapter.notifyItemRangeChanged(0, items.size());
        }
    }
}