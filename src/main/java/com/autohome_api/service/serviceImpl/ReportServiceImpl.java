package com.autohome_api.service.serviceImpl;

import com.autohome_api.dao.ReportMapping;
import com.autohome_api.dto.ReportParam;
import com.autohome_api.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapping reportMapping;
    @Override
    public long insertReport(ReportParam reportParam) {
        long i = reportMapping.insertReport(reportParam);
        return i;
    }
}
