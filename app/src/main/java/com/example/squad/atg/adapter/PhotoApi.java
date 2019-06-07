package com.example.squad.atg.adapter;

public class PhotoApi {

    private String url_s;
    private String title;


    public PhotoApi(String url_s, String title) {
        this.title = title;
        this.url_s = url_s;

    }


    public String getUrl() {
        return url_s;
    }

    public String getTitle() {
        return title;
    }

    public void setUrl(String url_s) {
        this.url_s = url_s;
    }

}
