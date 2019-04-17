package com.autohome_api.commons.ExcelToDto.dto;


import com.autohome_api.commons.ExcelToDto.ExcelInterface.ExcelColunm;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.lang.annotation.Annotation;

@Data
@Getter
@Setter
@ToString
public class ExcelDto implements Annotation {
    
    @ExcelColunm(colName="projectName")
    private String projectName;

    @ExcelColunm(colName="id")
    private String id;

    @ExcelColunm(colName="caseName")
    private String caseName;

    @ExcelColunm(colName="requestUrl")
    private String requestUrl;

    @ExcelColunm(colName="is_timestamp")
    private String is_timestamp;

    @ExcelColunm(colName="isSign")
    private String isSign;

    @ExcelColunm(colName="_appid")
    private String _appid;

    @ExcelColunm(colName="sign_method")
    private String sign_method;

    @ExcelColunm(colName="format")
    private String format;

    @ExcelColunm(colName="requestType")
    private String requestType;

    @ExcelColunm(colName="requestHeader")
    private String requestHeader;

    @ExcelColunm(colName="requestBody")
    private String requestBody;

    @ExcelColunm(colName="check")
    private String check;

    @ExcelColunm(colName="expect")
    private String expect;

    @Override
    public boolean equals(Object obj) {
        return false;
    }


    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }


}
