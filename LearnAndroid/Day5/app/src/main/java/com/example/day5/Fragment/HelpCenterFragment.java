package com.example.day5.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.day5.Bean.QuesInfo;
import com.example.day5.R;

import java.util.ArrayList;
import java.util.List;
public class HelpCenterFragment extends Fragment implements View.OnClickListener {

    Button common_ques, bt_login, modify_info, account_security, bt_more;
    List<QuesInfo> common_items, login_items, modify_items, security_items;
    FragmentManager manager;

    public HelpCenterFragment() {
    }

    public static HelpCenterFragment newInstance() {
        return new HelpCenterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_help_center, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);
        initEvent();
    }

    private void initEvent() {
        common_ques.setOnClickListener(this);
        bt_login.setOnClickListener(this);
        modify_info.setOnClickListener(this);
        account_security.setOnClickListener(this);
    }

    private void initData() {
        manager = getActivity().getSupportFragmentManager();

        common_items = new ArrayList<>();
        login_items = new ArrayList<>();
        modify_items = new ArrayList<>();
        security_items = new ArrayList<>();

        common_items.add(new QuesInfo("忘记账号了，改如何找回？"));
        common_items.add(new QuesInfo("忘记密码了，如何重置密码？"));
        common_items.add(new QuesInfo("手机号停用了，如何登录或换绑手机号"));
        common_items.add(new QuesInfo("申诉不通过怎么办？"));
        common_items.add(new QuesInfo("账号被盗了，怎么办？"));
        common_items.add(new QuesInfo("如何退出小米账号？"));

        login_items.add(new QuesInfo("如何登录小米账号？"));
        login_items.add(new QuesInfo("如何使用第三方账号登录小米账号？"));
        login_items.add(new QuesInfo("忘记账号了，该如何找回"));
        login_items.add(new QuesInfo("没有绑定手机号，怎么登陆账号？"));
        login_items.add(new QuesInfo("账号登录异常的原因？"));
        login_items.add(new QuesInfo("如何查看账号下登录的小米设备？"));
        login_items.add(new QuesInfo("账号长期不登录，会自动注销吗？"));

        modify_items.add(new QuesInfo("如何更换安全手机？"));
        modify_items.add(new QuesInfo("如何更换安全邮箱？"));
        modify_items.add(new QuesInfo("如何解绑手机号和邮箱？"));
        modify_items.add(new QuesInfo("如何将手机号换绑到另一台账号？"));
        modify_items.add(new QuesInfo("如何绑定/换绑/解绑第三方账号？"));
        modify_items.add(new QuesInfo("如何重置密保？"));

        security_items.add(new QuesInfo("如何处理修改密码后，提示登录异常？"));
        security_items.add(new QuesInfo("忘记密码了，怎么重置密码？"));
        security_items.add(new QuesInfo("如何处理账号被绑定他人手机号/邮箱的问题"));
        security_items.add(new QuesInfo("账号被盗了怎么办？"));
        security_items.add(new QuesInfo("账号被他人实名认证了怎么办？"));
        security_items.add(new QuesInfo("如何冻结解冻账号？"));
        security_items.add(new QuesInfo("为什么账号会被自动冻结？"));
        security_items.add(new QuesInfo("为什么账号会被封禁？"));

        loadFragment(common_items);
    }

    private void initView(View view) {
        common_ques = view.findViewById(R.id.common_ques);
        bt_login = view.findViewById(R.id.bt_login);
        modify_info = view.findViewById(R.id.modify_info);
        account_security = view.findViewById(R.id.account_security);
        selectButton(common_ques);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.common_ques) {
            selectButton(common_ques);
            loadFragment(common_items);
        } else if (id == R.id.bt_login) {
            selectButton(bt_login);
            loadFragment(login_items);
        } else if (id == R.id.modify_info) {
            selectButton(modify_info);
            loadFragment(modify_items);
        } else if (id == R.id.account_security) {
            selectButton(account_security);
            loadFragment(security_items);
        }
    }

    private void selectButton(Button button) {
        common_ques.setSelected(false);
        bt_login.setSelected(false);
        modify_info.setSelected(false);
        account_security.setSelected(false);
        button.setSelected(true);
    }

    private void loadFragment(List<QuesInfo> items) {
        QuesInfoFragment fragment = new QuesInfoFragment(items);
        manager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
