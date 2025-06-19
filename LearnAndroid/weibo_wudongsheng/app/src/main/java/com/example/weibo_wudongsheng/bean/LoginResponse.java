package com.example.weibo_wudongsheng.bean;

/**
 * @author wellorbetter
 * 登录请求 返回数据
 */
public class LoginResponse {
    private int code;
    private String msg;
    private String data;


    public LoginResponse(int code, String msg, String data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
