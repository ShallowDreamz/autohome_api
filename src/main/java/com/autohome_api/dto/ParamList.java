package com.autohome_api.dto;

public class ParamList {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" +
                "key:'" + key + '\'' +
                ", value:'" + value + '\'' +
                '}';
    }
}
