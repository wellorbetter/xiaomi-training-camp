package com.example.weibo_wudongsheng.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weibo_wudongsheng.R;
import com.example.weibo_wudongsheng.bean.LoginRequest;
import com.example.weibo_wudongsheng.bean.LoginResponse;
import com.example.weibo_wudongsheng.bean.VerifyNumberResponse;
import com.example.weibo_wudongsheng.utils.WeiboRequestHelper;
import com.example.weibo_wudongsheng.utils.retrofit.ApiClient;
import com.tencent.mmkv.MMKV;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author wellorbetter
 * 登录界面
 *
 * 用户可以通过此界面进行登录操作，输入手机号码和验证码来登录
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // 控件
    private Button mBtnSendVerifyNum;
    private Button mBtnLogin;
    private Button mBtnBack;
    private EditText mEtVerifyNum;
    private EditText mEtPhoneNumber;

    // 数据相关
    private MMKV mMMKV;
    private String mVerifyNumber = "";
    private String mPhoneNumber = "";
    private CountDownTimer mCountDownTimer;
    private WeiboRequestHelper weiboRequestHelper;
    // 常量
    private static final long COUNTDOWN_TIME = 60000; // 60秒倒计时
    private static final long COUNTDOWN_INTERVAL = 1000; // 每秒更新一次

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initData();
        initView();
        initEvent();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        EventBus.getDefault().register(this);
        mMMKV = MMKV.defaultMMKV();
        weiboRequestHelper = new WeiboRequestHelper(getApplicationContext());
    }

    /**
     * 初始化事件监听
     */
    private void initEvent() {
        mBtnSendVerifyNum.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);

        // 拿到输入的数据
        mEtVerifyNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mVerifyNumber = s.toString();
                checkLoginButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mEtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPhoneNumber = s.toString();
                checkLoginButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // 初始时禁用登录按钮
        mBtnLogin.setEnabled(false);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mBtnSendVerifyNum = findViewById(R.id.bt_send_verify_num);
        mEtVerifyNum = findViewById(R.id.et_verify_num);
        mEtPhoneNumber = findViewById(R.id.et_phone_number);
        mBtnBack = findViewById(R.id.bt_back);
        mBtnLogin = findViewById(R.id.bt_login);
        // 一进来就需要设置一次
        checkLoginButtonState();
    }

    /**
     * 开始倒计时
     */
    private void startCountdown() {
        // 禁用按钮防止多次点击
        mBtnSendVerifyNum.setEnabled(false);
        // 设置变灰
        mBtnSendVerifyNum.setTextColor(getResources().getColor(R.color.grey_800));
        mCountDownTimer = new CountDownTimer(COUNTDOWN_TIME, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                mBtnSendVerifyNum.setText(getString(R.string.seconds_remaining, millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mBtnSendVerifyNum.setEnabled(true);
                mBtnSendVerifyNum.setText(R.string.get_verify_code);
                mBtnSendVerifyNum.setTextColor(getResources().getColor(R.color.input_color));
            }
        }.start();
    }

    /**
     * 获取验证码 无论成功都会进入倒计时并变灰无法点击，防止点击次数过多导致一直Toast
     *
     * @param phoneNumber 用户输入的手机号码
     */
    private void getVerifyNumber(String phoneNumber) {
        Call<VerifyNumberResponse> call = ApiClient.getApiService().getVerifyNumber(phoneNumber);
        call.enqueue(new Callback<VerifyNumberResponse>() {
            // 不管发送成不成功都倒计时，防止一直重复点击导致Toast过多
            @Override
            public void onResponse(Call<VerifyNumberResponse> call, Response<VerifyNumberResponse> response) {
                // 开始倒计时
                startCountdown();
                if (response.isSuccessful()) {
                    // 请求成功处理
                    VerifyNumberResponse verifyNumberResponse = response.body();
                    Log.d("verifyNumberResponse", verifyNumberResponse.toString());
                    if (verifyNumberResponse.getCode() != 200 && !verifyNumberResponse.isData()) {
                        Toast.makeText(getApplicationContext(), "请确认输入的手机号无误!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "验证码发送成功!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 请求失败处理
                    Toast.makeText(getApplicationContext(), "验证码发送失败!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VerifyNumberResponse> call, Throwable t) {
                // 开始倒计时
                startCountdown();
                // 请求失败处理
                Toast.makeText(getApplicationContext(), "验证码发送失败!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 登录   登录成功后会返回主界面并传输登录成功信息，通知主界面刷新
     *
     * @param phoneNumber 用户输入的手机号码
     * @param verifyNumber 用户输入的验证码
     */
    public void login(String phoneNumber, String verifyNumber) {
        weiboRequestHelper.requestLogin(phoneNumber, verifyNumber);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_send_verify_num) {
            // 发送成功了才会将颜色设置成灰色同时不可用
            // 因为可能出现断网，然后点击，同时获取失败了
            // 就算恢复网络了，此时的按钮就不能点了
            getVerifyNumber(mPhoneNumber);
        } else if (id == R.id.bt_login) {
            login(mPhoneNumber, mVerifyNumber);
        } else if (id == R.id.bt_back) {
            finish();
        }
    }

    /**
     * 检查登录按钮的状态
     */
    private void checkLoginButtonState() {
        if (!mPhoneNumber.isEmpty() && !mVerifyNumber.isEmpty()) {
            mBtnLogin.setEnabled(true);
            mBtnLogin.setBackground(getDrawable(R.drawable.button_blue));
        } else {
            mBtnLogin.setEnabled(false);
            mBtnLogin.setBackground(getDrawable(R.drawable.button_grey));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在销毁 Activity 的时候停止倒计时，防止内存泄漏
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(String login_success) {
        if (login_success.equals("login_success")) {
            finish();
        }
    }
}
