package com.example.notebook;

import android.net.Uri;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class word extends LitePalSupport {
    @Column(unique = true)
    private String en;
    private String zh;
    private Uri uri;
    private String example;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getExample() {
        return example;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getZh() {
        return zh;
    }

    public void setZh(String zh) {
        this.zh = zh;
    }
}
