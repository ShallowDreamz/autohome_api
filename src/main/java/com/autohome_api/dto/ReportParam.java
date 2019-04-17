package com.autohome_api.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@Getter
@Setter
public class ReportParam {
    private long id;
    private String projectName;
    private String report;
    private Date createDate;
}
