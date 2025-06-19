package com.example.weibo_wudongsheng.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * 最后解析出来的真正的微博条目的信息类
 */
public class WeiboInfo implements MultiItemEntity {
    private Long id;
    private Long userId;

    private String username;
    private String phone;
    private String avatar;
    private String title;
    private String videoUrl;
    private String poster;
    private List<String> images;
    private Integer likeCount;
    private Boolean likeFlag;
    private String createTime;

    public static final int VIEW_TYPE_IMAGES = 0;
    public static final int VIEW_TYPE_VIDEO = 1;
    public static final int VIEW_TYPE_TEXT = 2;
    public static final int VIEW_TYPE_SINGLE_IMAGE = 3;

    private int itemType;

    public WeiboInfo(Long id, Long userId, String username, String phone, String avatar, String title, String videoUrl, String poster, List<String> images, Integer likeCount, Boolean likeFlag, String createTime) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.phone = phone;
        this.avatar = avatar;
        this.title = title;
        this.videoUrl = videoUrl;
        this.poster = poster;
        this.images = images;
        this.likeCount = likeCount;
        this.likeFlag = likeFlag;
        this.createTime = createTime;
        setItemType();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        setItemType();
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
        setItemType();
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Boolean getLikeFlag() {
        return likeFlag;
    }

    public void setLikeFlag(Boolean likeFlag) {
        this.likeFlag = likeFlag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType() {
        if (images == null && videoUrl == null) {
            this.itemType = VIEW_TYPE_TEXT;
        } else if (images != null && !images.isEmpty()) {
            if (images.size() > 1) {
                this.itemType = VIEW_TYPE_IMAGES;
            } else {
                this.itemType = VIEW_TYPE_SINGLE_IMAGE;
            }
        } else if (videoUrl != null && !videoUrl.isEmpty()) {
            this.itemType = VIEW_TYPE_VIDEO;
        }
    }

    private int resumePosition = 0;

    public int getResumePosition() {
        return resumePosition;
    }

    public void setResumePosition(int resumePosition) {
        this.resumePosition = resumePosition;
    }
}
