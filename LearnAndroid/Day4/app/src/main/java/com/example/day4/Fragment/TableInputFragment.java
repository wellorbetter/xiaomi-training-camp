package com.example.day4.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.day4.R;

public class TableInputFragment extends Fragment implements TextWatcher, CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    EditText et_pw, et_nickName;
    SeekBar sk_avatar;
    ImageView iv_avatar;
    RadioButton rb_man, rb_woman;
    RadioGroup rg_sex;
    CheckBox ck;
    Button bt;

    public TableInputFragment() {

    }

    public static TableInputFragment newInstance(String param1, String param2) {
        return new TableInputFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_table_input, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvent();
    }

    private void initEvent() {
        et_pw.addTextChangedListener(this);
        et_nickName.addTextChangedListener(this);
        rb_man.setOnCheckedChangeListener(this);
        rb_woman.setOnCheckedChangeListener(this);
        sk_avatar.setOnSeekBarChangeListener(this);
        ck.setOnCheckedChangeListener(this);
        bt.setOnClickListener(this);
    }

    private void initView(View view) {
        et_pw = view.findViewById(R.id.et_pw);
        et_nickName = view.findViewById(R.id.et_nickName);
        sk_avatar = view.findViewById(R.id.sk_avatar);
        rb_man = view.findViewById(R.id.rb_man);
        rb_woman = view.findViewById(R.id.rb_woman);
        ck = view.findViewById(R.id.ck);
        bt = view.findViewById(R.id.bt);
        iv_avatar = view.findViewById(R.id.iv_avatar);
        rg_sex = view.findViewById(R.id.rg_sex);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (charSequence.length() > 0) {
            String input = charSequence.toString();
            StringBuilder filteredInput = new StringBuilder();

            if (et_pw.hasFocus()) {
                // 只能输入数字
                for (int i = 0; i < input.length(); i++) {
                    char c = input.charAt(i);
                    // 过滤，好像有个filter lambda我记得我用过，
                    // 写都写了 就不改了
                    if (c >= '0' && c <= '9') {
                        filteredInput.append(c);
                    }
                }
                // 如果有异常
                if (!filteredInput.toString().equals(input)) {
                    et_pw.setText(filteredInput.toString());
                    et_pw.setSelection(filteredInput.length());
                    Toast.makeText(getContext(), "请输入数字", Toast.LENGTH_SHORT).show();
                }
            } else if (et_nickName.hasFocus()) {
                // 只能输入大写字母
                for (int i = 0; i < input.length(); i++) {
                    char c = input.charAt(i);
                    if (c >= 'A' && c <= 'Z') {
                        filteredInput.append(c);
                    }
                }
                if (!filteredInput.toString().equals(input)) {
                    et_nickName.setText(filteredInput.toString());
                    et_nickName.setSelection(filteredInput.length());
                    Toast.makeText(getContext(), "请输入大写字母", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    @Override
    public void afterTextChanged(Editable editable) {

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = getId();
        if (id == R.id.rb_man) {
            Toast.makeText(getContext(), "性别男", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.rb_woman) {
            Toast.makeText(getContext(), "性别女", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.ck) {
            Toast.makeText(getContext(), "您选择了同意", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        float opacity = i / 360.0f;
        iv_avatar.setAlpha(opacity);
        iv_avatar.setRotation(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.bt) {
            Toast.makeText(getContext(), "提交", Toast.LENGTH_SHORT).show();
        }
    }
}