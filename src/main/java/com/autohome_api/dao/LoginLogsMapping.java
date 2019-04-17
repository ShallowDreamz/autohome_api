package com.autohome_api.dao;

import com.autohome_api.dto.LoginLogs;

import java.util.List;
import java.util.Map;

public interface LoginLogsMapping {
    void insertLoginLogs(LoginLogs loginlog);
    List<LoginLogs> selectLoginLogs(Map map);
}
