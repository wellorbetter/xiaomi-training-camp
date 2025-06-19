package com.example.day4.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.viewmodel.CreationExtras;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.day4.Adapter.RVAdapter;
import com.example.day4.Bean.Data;
import com.example.day4.R;

import java.util.List;


public class ImprovedRVFragment extends Fragment implements View.OnClickListener, TextWatcher {

    List<Data> items;
    RecyclerView rv;
    Button bt_add ,bt_remove;
    EditText et;
    int inputNum = -1;
    private RVAdapter adapter;

    public ImprovedRVFragment(List<Data> items) {
        this.items = items;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_improved_r_v, container, false);
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
        et.addTextChangedListener(this);
    }

    private void initView(View view) {
        rv = view.findViewById(R.id.rv);
        bt_add = view.findViewById(R.id.bt_add);
        bt_remove = view.findViewById(R.id.bt_remove);
        et = view.findViewById(R.id.et);
        adapter = new RVAdapter(items);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt_add) {
            Data item = new Data(getActivity().getDrawable(R.drawable.img), "这是添加的内容");
            if (inputNum < 0) {
                // 输入负数，添加到第一个位置
                items.add(0, item);
                adapter.notifyItemInserted(0);
                Toast.makeText(getContext(), "您输入为负数/未输入，已为您添加到第一个", Toast.LENGTH_SHORT).show();
            } else if (inputNum >= items.size()) {
                // 超出范围，添加到最后一个位置
                items.add(items.size(), item);
                adapter.notifyItemInserted(items.size() - 1);
                Toast.makeText(getContext(), "超出范围，已为您添加到最后一个", Toast.LENGTH_SHORT).show();
            } else {
                // 在范围内，添加到指定位置
                items.add(inputNum, item);
                adapter.notifyItemInserted(inputNum);
                Toast.makeText(getContext(), "已为您添加到指定位置", Toast.LENGTH_SHORT).show();
            }
            // 更新position
            adapter.notifyItemRangeChanged(0, items.size());
        } else if (id == R.id.bt_remove) {
            // 在items范围内
            if (inputNum >= 0 && inputNum < items.size()) {
                items.remove(inputNum);
                adapter.notifyItemRemoved(inputNum);
                adapter.notifyItemRangeChanged(inputNum, items.size() - inputNum);
            } else if (inputNum < 0 && items.size() > 0) {
                int tmp = inputNum;
                inputNum = 0;
                items.remove(inputNum);
                adapter.notifyItemRemoved(inputNum);
                adapter.notifyItemRangeChanged(inputNum, items.size() - inputNum);
                Toast.makeText(getContext(), "您输入为负数/未输入，已为您删除第一个", Toast.LENGTH_SHORT).show();
                inputNum = tmp;
            } else if (inputNum >= items.size() && items.size() > 0) {
                items.remove(items.size() - 1);
                adapter.notifyItemRemoved(items.size());
                Toast.makeText(getContext(), "超出范围，已为您删除最后一个", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "空列表，无法删除", Toast.LENGTH_SHORT).show();
            }
        }
    }




    @NonNull
    @Override
    public CreationExtras getDefaultViewModelCreationExtras() {
        return super.getDefaultViewModelCreationExtras();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (et.hasFocus()) {
            String input = charSequence.toString().trim();
            if (TextUtils.isEmpty(input)) {
                inputNum = -1; // 空输入，或者其他默认值，表示没有有效输入
            } else {
                try {
                    inputNum = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    if (input.startsWith("-") && input.length() == 1) {
                        inputNum = -1; // 输入了负号，还没有输入数字
                    } else {
                        inputNum = -1; // 其他非法输入，可以根据需求设置默认值
                    }
                }
            }
        } else {
            inputNum = -1; // 或者其他默认值，表示没有有效输入
        }
    }



    @Override
    public void afterTextChanged(Editable editable) {

    }
}