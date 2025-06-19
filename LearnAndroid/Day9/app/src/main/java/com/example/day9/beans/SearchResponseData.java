package com.example.day9.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wellorbetter
 */
@NoArgsConstructor
@Data
public class SearchResponseData {
    @JsonProperty("code")
    private Integer code;
    @JsonProperty("msg")
    private String msg;
    @JsonProperty("data")
    private GameInfoPage data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public GameInfoPage getData() {
        return data;
    }

    public void setData(GameInfoPage data) {
        this.data = data;
    }

    @NoArgsConstructor
    @Data
    public static class GameInfoPage {
        @JsonProperty("records")
        private List<GameInfo> records;
        @JsonProperty("total")
        private Integer total;
        @JsonProperty("size")
        private Integer size;
        @JsonProperty("current")
        private Integer current;
        @JsonProperty("orders")
        private List<?> orders;
        @JsonProperty("searchCount")
        private Boolean searchCount;
        @JsonProperty("pages")
        private Integer pages;

        public List<GameInfo> getRecords() {
            return records;
        }

        public void setRecords(List<GameInfo> records) {
            this.records = records;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public Integer getCurrent() {
            return current;
        }

        public void setCurrent(Integer current) {
            this.current = current;
        }

        public List<?> getOrders() {
            return orders;
        }

        public void setOrders(List<?> orders) {
            this.orders = orders;
        }

        public Boolean getSearchCount() {
            return searchCount;
        }

        public void setSearchCount(Boolean searchCount) {
            this.searchCount = searchCount;
        }

        public Integer getPages() {
            return pages;
        }

        public void setPages(Integer pages) {
            this.pages = pages;
        }

        @NoArgsConstructor
        @Data
        public static class GameInfo implements Parcelable {
            @JsonProperty("id")
            private Integer id;
            @JsonProperty("gameName")
            private String gameName;
            @JsonProperty("packageName")
            private String packageName;
            @JsonProperty("appId")
            private String appId;
            @JsonProperty("icon")
            private String icon;
            @JsonProperty("introduction")
            private String introduction;
            @JsonProperty("brief")
            private String brief;
            @JsonProperty("versionName")
            private String versionName;
            @JsonProperty("apkUrl")
            private String apkUrl;
            @JsonProperty("tags")
            private String tags;
            @JsonProperty("score")
            private Double score;
            @JsonProperty("playNumFormat")
            private String playNumFormat;
            @JsonProperty("createTime")
            private String createTime;

            protected GameInfo(Parcel in) {
                id = in.readInt();
                gameName = in.readString();
                packageName = in.readString();
                appId = in.readString();
                icon = in.readString();
                introduction = in.readString();
                brief = in.readString();
                versionName = in.readString();
                apkUrl = in.readString();
                tags = in.readString();
                if (in.readByte() == 0) {
                    score = null;
                } else {
                    score = in.readDouble();
                }
                playNumFormat = in.readString();
                createTime = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(id);
                dest.writeString(gameName);
                dest.writeString(packageName);
                dest.writeString(appId);
                dest.writeString(icon);
                dest.writeString(introduction);
                dest.writeString(brief);
                dest.writeString(versionName);
                dest.writeString(apkUrl);
                dest.writeString(tags);
                if (score == null) {
                    dest.writeByte((byte) 0);
                } else {
                    dest.writeByte((byte) 1);
                    dest.writeDouble(score);
                }
                dest.writeString(playNumFormat);
                dest.writeString(createTime);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<GameInfo> CREATOR = new Creator<GameInfo>() {
                @Override
                public GameInfo createFromParcel(Parcel in) {
                    return new GameInfo(in);
                }

                @Override
                public GameInfo[] newArray(int size) {
                    return new GameInfo[size];
                }
            };

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getGameName() {
                return gameName;
            }

            public void setGameName(String gameName) {
                this.gameName = gameName;
            }

            public String getPackageName() {
                return packageName;
            }

            public void setPackageName(String packageName) {
                this.packageName = packageName;
            }

            public String getAppId() {
                return appId;
            }

            public void setAppId(String appId) {
                this.appId = appId;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getIntroduction() {
                return introduction;
            }

            public void setIntroduction(String introduction) {
                this.introduction = introduction;
            }

            public String getBrief() {
                return brief;
            }

            public void setBrief(String brief) {
                this.brief = brief;
            }

            public String getVersionName() {
                return versionName;
            }

            public void setVersionName(String versionName) {
                this.versionName = versionName;
            }

            public String getApkUrl() {
                return apkUrl;
            }

            public void setApkUrl(String apkUrl) {
                this.apkUrl = apkUrl;
            }

            public String getTags() {
                return tags;
            }

            public void setTags(String tags) {
                this.tags = tags;
            }

            public Double getScore() {
                return score;
            }

            public void setScore(Double score) {
                this.score = score;
            }

            public String getPlayNumFormat() {
                return playNumFormat;
            }

            public void setPlayNumFormat(String playNumFormat) {
                this.playNumFormat = playNumFormat;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }
        }
    }
}
