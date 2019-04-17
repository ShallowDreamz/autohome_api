package com.autohome_api.dto;

public class RequestType {
    private String requestType;

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    @Override
    public String toString() {
        return "RequestType{" +
                "requestType='" + requestType + '\'' +
                '}';
    }
}
