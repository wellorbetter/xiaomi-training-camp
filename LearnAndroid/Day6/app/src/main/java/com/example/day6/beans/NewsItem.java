package com.example.day6.beans;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author wellorbetter
 * 使用GSONFormat自动生成的类，很奇怪，使用lombok注解@Getter没用，只能生成一下方法了
 */
@lombok.NoArgsConstructor
@lombok.Data
public class NewsItem {
    @com.fasterxml.jackson.annotation.JsonProperty("date")
    @Getter(value = AccessLevel.PUBLIC)
    private String date;
    @com.fasterxml.jackson.annotation.JsonProperty("stories")
    @Getter(value = AccessLevel.PUBLIC)
    private List<StoriesDTO> stories;
    @com.fasterxml.jackson.annotation.JsonProperty("top_stories")
    @Getter(value = AccessLevel.PUBLIC)
    private List<TopStoriesDTO> topStories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesDTO> getStories() {
        return stories;
    }

    public void setStories(List<StoriesDTO> stories) {
        this.stories = stories;
    }

    public List<TopStoriesDTO> getTopStories() {
        return topStories;
    }

    public void setTopStories(List<TopStoriesDTO> topStories) {
        this.topStories = topStories;
    }

    @lombok.NoArgsConstructor
    @lombok.Data
    public static class StoriesDTO {
        @com.fasterxml.jackson.annotation.JsonProperty("image_hue")
        private String imageHue;
        @com.fasterxml.jackson.annotation.JsonProperty("title")
        private String title;
        @com.fasterxml.jackson.annotation.JsonProperty("url")
        private String url;
        @com.fasterxml.jackson.annotation.JsonProperty("hint")
        private String hint;
        @com.fasterxml.jackson.annotation.JsonProperty("ga_prefix")
        private String gaPrefix;
        @com.fasterxml.jackson.annotation.JsonProperty("images")
        private List<String> images;
        @com.fasterxml.jackson.annotation.JsonProperty("type")
        private Integer type;
        @com.fasterxml.jackson.annotation.JsonProperty("id")
        private Integer id;

        public String getImageHue() {
            return imageHue;
        }

        public void setImageHue(String imageHue) {
            this.imageHue = imageHue;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getHint() {
            return hint;
        }

        public void setHint(String hint) {
            this.hint = hint;
        }

        public String getGaPrefix() {
            return gaPrefix;
        }

        public void setGaPrefix(String gaPrefix) {
            this.gaPrefix = gaPrefix;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

    @lombok.NoArgsConstructor
    @lombok.Data
    public static class TopStoriesDTO {
        @com.fasterxml.jackson.annotation.JsonProperty("image_hue")
        private String imageHue;
        @com.fasterxml.jackson.annotation.JsonProperty("hint")
        private String hint;
        @com.fasterxml.jackson.annotation.JsonProperty("url")
        private String url;
        @com.fasterxml.jackson.annotation.JsonProperty("image")
        private String image;
        @com.fasterxml.jackson.annotation.JsonProperty("title")
        private String title;
        @com.fasterxml.jackson.annotation.JsonProperty("ga_prefix")
        private String gaPrefix;
        @com.fasterxml.jackson.annotation.JsonProperty("type")
        private Integer type;
        @com.fasterxml.jackson.annotation.JsonProperty("id")
        private Integer id;
    }
}
