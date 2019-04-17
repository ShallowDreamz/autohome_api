package com.autohome_api.dto;

public class RequestUrl {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "RequestUrl{" +
                "url='" + url + '\'' +
                '}';
    }
}
