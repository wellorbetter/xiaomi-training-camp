package com.example.weibo_wudongsheng.bean;

/**
 * @author wellorbetter
 * 验证码返回数据
 */
public class VerifyNumberResponse {
    private int code;
    private String ms;
    private boolean data;

    public VerifyNumberResponse(int code, String ms, boolean data) {
        this.code = code;
        this.ms = ms;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMs() {
        return ms;
    }

    public void setMs(String ms) {
        this.ms = ms;
    }

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }
}
