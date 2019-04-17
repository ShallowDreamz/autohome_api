package com.autohome_api.service;

import com.autohome_api.commons.ExcelToDto.dto.ExcelDto;
import com.autohome_api.dto.BatchResponseBody;

public interface BatchProcessingService {
    BatchResponseBody excelBatchService(ExcelDto excelDto);
}
