package com.autohome_api.dto;


public class ResponseParam {
    private String code;
    private String message;

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "RespobseBody{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
