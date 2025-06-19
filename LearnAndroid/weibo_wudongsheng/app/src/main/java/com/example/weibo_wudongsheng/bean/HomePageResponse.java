package com.example.weibo_wudongsheng.bean;

/**
 * @author wellorbetter
 */
public class HomePageResponse {
    private int code;
    private String msg;
    private Page<WeiboInfo> data;

    public HomePageResponse(int code, String msg, Page<WeiboInfo> data) {
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

    public Page<WeiboInfo> getData() {
        return data;
    }

    public void setData(Page<WeiboInfo> data) {
        this.data = data;
    }
}
