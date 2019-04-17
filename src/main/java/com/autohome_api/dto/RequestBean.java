package com.autohome_api.dto;

public class RequestBean {
    private RequestUrl requestUrl;
    private RequestHeader requestHeader;
    private RequestParam requestParam;
    private RequestType requestType;

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public RequestUrl getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(RequestUrl requestUrl) {
        this.requestUrl = requestUrl;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }

    public RequestParam getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(RequestParam requestParam) {
        this.requestParam = requestParam;
    }

    @Override
    public String toString() {
        return "RequestBean{" +
                "requestUrl=" + requestUrl +
                ", requestHeader=" + requestHeader +
                ", requestParam=" + requestParam +
                ", requestType=" + requestType +
                '}';
    }
}
