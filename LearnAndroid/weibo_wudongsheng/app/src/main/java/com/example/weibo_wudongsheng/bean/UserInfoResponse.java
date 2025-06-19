package com.example.weibo_wudongsheng.bean;

/**
 * @author wellorbetter
 * 登录 返回用户信息
 */
public class UserInfoResponse {
    private UserInfo data;
    private String msg;
    private int code;

    public UserInfoResponse(UserInfo data, String msg, int code) {
        this.data = data;
        this.msg = msg;
        this.code = code;
    }

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
