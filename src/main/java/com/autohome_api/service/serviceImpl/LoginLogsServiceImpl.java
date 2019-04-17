package com.autohome_api.service.serviceImpl;

import com.autohome_api.dao.LoginLogsMapping;
import com.autohome_api.dto.LoginLogs;
import com.autohome_api.service.LoginLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginLogsServiceImpl implements LoginLogsService {
    @Autowired
    private LoginLogsMapping loginLogsMapping;
    @Override
    public void insertLoginLogs(LoginLogs loginLogs) {
        loginLogsMapping.insertLoginLogs(loginLogs);
    }

}
