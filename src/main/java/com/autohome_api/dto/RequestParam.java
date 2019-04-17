package com.autohome_api.dto;

public class RequestParam {
    private String requestBody;

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    @Override
    public String toString() {
        return "RequestParam{" +
                "requestBody='" + requestBody + '\'' +
                '}';
    }
}
