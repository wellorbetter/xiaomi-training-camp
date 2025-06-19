package com.example.weibo_wudongsheng.bean;

/**
 * @author wellorbetter
 * 用户信息 接收登录返回信息
 */
public class UserInfo {
    Long id;
    String username;
    String phone;
    String avatar;
    Boolean loginStatus;

    public UserInfo(Long id, String username, String phone, String avatar, Boolean loginStatus) {
        this.id = id;
        this.username = username;
        this.phone = phone;
        this.avatar = avatar;
        this.loginStatus = loginStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Boolean getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Boolean loginStatus) {
        this.loginStatus = loginStatus;
    }
}
