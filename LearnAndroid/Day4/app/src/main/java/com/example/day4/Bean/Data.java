package com.example.day4.Bean;

import android.graphics.drawable.Drawable;
import android.media.Image;

public class Data {
    Drawable img;
    String content;

    public Data(Drawable img, String content) {
        this.img = img;
        this.content = content;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
