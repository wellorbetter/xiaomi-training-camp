package com.example.weibo_wudongsheng.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.weibo_wudongsheng.R;
import com.example.weibo_wudongsheng.activity.LoginActivity;
import com.example.weibo_wudongsheng.bean.UserInfo;
import com.example.weibo_wudongsheng.bean.UserInfoResponse;
import com.example.weibo_wudongsheng.customize.CircleImageButton;
import com.tencent.mmkv.MMKV;

/**
 * @author wellorbetter
 * Fragment 个人页面
 * 登录后展示个人信息
 * 未登录会提示登录信息
 */
public class MineFragment extends Fragment implements View.OnClickListener {

    /**
     /**civ_avatar  个人头像, 自定义圆形
     * tv_nickname  昵称
     * tv_fans_num  粉丝数量
     * tv_info      具体内容
     * bt_logout    退出按钮
     * userInfo     存用户信息，用于更新用户信息
     * loginStatus  登录状况，根据传入数据判断是否登录，然后执行渲染逻辑
     * mmkv         存储数据
     */
    CircleImageButton civ_avatar;
    TextView tv_nickname, tv_fans_num, tv_info;
    Button bt_logout;
    UserInfo userInfo;
    Boolean loginStatus = false;
    MMKV mmkv;
    public MineFragment() {

    }
    public MineFragment(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvent();
    }

    private void initEvent() {
        civ_avatar.setOnClickListener(this);
        bt_logout.setOnClickListener(this);
    }

    private void initView(View view) {
        civ_avatar = view.findViewById(R.id.civ_avatar);
        tv_nickname = view.findViewById(R.id.tv_nickname);
        tv_fans_num = view.findViewById(R.id.tv_fans_num);
        tv_info = view.findViewById(R.id.tv_info);
        bt_logout = view.findViewById(R.id.bt_logout);

        // 两个构造函数，如果userInfo不为空，就说明登录成功，需要渲染界面
        if (userInfo != null) {
            loginStatus = true;
            reRenderByUserInfo(userInfo);
        }
    }

    /**
     * @param userInfo 传入用户的数据更新视图
     */
    private void reRenderByUserInfo(UserInfo userInfo) {
        // 如果返回的结果是正确的(如果这里请求头传错了，返回也是会success，但是loginStatus为false)
        if (userInfo.getAvatar() != null) {
            Glide.with(getActivity())
                    .load(Uri.parse(userInfo.getAvatar()))
                    .into(civ_avatar);
        } else {
            Glide.with(getActivity())
                    .load(R.drawable.img)
                    .into(civ_avatar);
        }
        if (userInfo.getUsername() != null) {
            tv_nickname.setText(userInfo.getUsername());
        } else {
            tv_nickname.setText("null");
        }
        // 这里可能需要判断粉丝数量
        tv_fans_num.setText("粉丝 9999");
        tv_info.setText("你没有新的动态哦~");
        // 只有当登录的时候才会显示右上角的退出登录
        bt_logout.setVisibility(View.VISIBLE);
        bt_logout.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.civ_avatar) {
            jumpToLogin();
        } else if (id == R.id.bt_logout) {
            logoutRender();
        }
    }

    /**
     * logout 登录之后可以选择退出登录
     * 同时退出登录时会重新渲染界面
     */
    private void logoutRender() {
        loginStatus = false;
        // 退出登录，删除本地的token
        mmkv = MMKV.defaultMMKV();
        mmkv.remove("loginToken");
        String tmp = mmkv.getString("loginToken", "");
        // 更新状态
        Glide.with(getActivity())
                .load(R.drawable.default_avatar)
                .into(civ_avatar);
        tv_nickname.setText("请先登录");
        bt_logout.setVisibility(View.INVISIBLE);
        bt_logout.setEnabled(false);
        tv_fans_num.setText("点击头像去登录");
        tv_info.setText("登陆后查看");
    }


    private void jumpToLogin() {
        if (loginStatus) {
            Toast.makeText(getActivity(), "您已经成功登录了", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
    }
}