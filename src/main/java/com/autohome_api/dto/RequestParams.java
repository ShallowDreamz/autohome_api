package com.autohome_api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Setter
@Getter
public class RequestParams {
    private Integer id;
    private String caseName;
    private String requestUrl;
    private String is_timestamp;
    private String isSign;
    private String _appid;
    private String sign_method;
    private String format;
    private String requestType;
    private List<ParamList> requestHeader;
    private List<ParamList> requestBody;
    private String check;
    private String except;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getIs_timestamp() {
        return is_timestamp;
    }

    public void setIs_timestamp(String is_timestamp) {
        this.is_timestamp = is_timestamp;
    }

    public String getIsSign() {
        return isSign;
    }

    public void setIsSign(String isSign) {
        this.isSign = isSign;
    }

    public String get_appid() {
        return _appid;
    }

    public void set_appid(String _appid) {
        this._appid = _appid;
    }

    public String getSign_method() {
        return sign_method;
    }

    public void setSign_method(String sign_method) {
        this.sign_method = sign_method;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public List<ParamList> getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(List<ParamList> requestHeader) {
        this.requestHeader = requestHeader;
    }

    public List<ParamList> getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(List<ParamList> requestBody) {
        this.requestBody = requestBody;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getExcept() {
        return except;
    }

    public void setExcept(String except) {
        this.except = except;
    }

    @Override
    public String toString() {
        return "RequestParams{" +
                "id=" + id +
                ", caseName='" + caseName + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", is_timestamp='" + is_timestamp + '\'' +
                ", isSign='" + isSign + '\'' +
                ", _appid='" + _appid + '\'' +
                ", sign_method='" + sign_method + '\'' +
                ", format='" + format + '\'' +
                ", requestType='" + requestType + '\'' +
                ", requestHeader=" + requestHeader +
                ", requestBody=" + requestBody +
                ", check='" + check + '\'' +
                ", except='" + except + '\'' +
                '}';
    }
}
