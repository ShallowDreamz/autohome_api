package com.autohome_api.service.serviceImpl;

import com.autohome_api.commons.BatchParameterNormalization;
import com.autohome_api.commons.ExcelToDto.dto.ExcelDto;
import com.autohome_api.commons.HttpClientDriver;
import com.autohome_api.dto.BatchResponseBody;
import com.autohome_api.service.BatchProcessingService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class BatchProcessingServiceImpl implements BatchProcessingService {
    public static final Logger logger = LoggerFactory.getLogger(BatchProcessingServiceImpl.class);
    @Autowired
    private BatchParameterNormalization batchParameterNormalization;
    @Override
    public BatchResponseBody excelBatchService(ExcelDto excelDto) {
        String url = excelDto.getRequestUrl();
        String headers = excelDto.getRequestHeader();
        String isSing = excelDto.getIsSign();
        String isTimetamp = excelDto.getIs_timestamp();
        String requestBody = excelDto.getRequestBody();
        HashMap<String,String> headerMap = batchParameterNormalization.paramOverWrite(headers);
        HashMap <String,String>BodyMap = batchParameterNormalization.paramOverWrite(requestBody);
        BodyMap.put("_appid", excelDto.get_appid());
        BodyMap.put("sign_method", excelDto.getSign_method());
        BodyMap.put("format", excelDto.getFormat());
        if ("get".equalsIgnoreCase(excelDto.getRequestType().trim())) {
            BatchResponseBody batchResponseBody = new BatchResponseBody();
            String host = batchParameterNormalization.getExcelURL(BodyMap, url, isSing, isTimetamp, headerMap);
            long startTime = System.currentTimeMillis();
            List list = HttpClientDriver.httpGet(host, headerMap);
            long endTime = System.currentTimeMillis();
            batchResponseBody.setProjectName(excelDto.getProjectName());
            batchResponseBody.setCode(list.get(0).toString());
            batchResponseBody.setMessage(list.get(1).toString());
            batchResponseBody.setCaseId(excelDto.getId());
            batchResponseBody.setCaseName(excelDto.getCaseName());
            batchResponseBody.setRequestParam(StringUtils.replace(excelDto.getRequestBody(),"\n",""));
            batchResponseBody.setExpected(excelDto.getExpect());
            batchResponseBody.setResponseTime(endTime - startTime);
            batchResponseBody.setIsPass(batchParameterNormalization.isPassed(excelDto,list.get(1).toString()));
            return batchResponseBody;
        }else if("post".equalsIgnoreCase(excelDto.getRequestType().trim())){
            BatchResponseBody batchResponseBody = new BatchResponseBody();
            SortedMap sortedMap = batchParameterNormalization.getExcelBody(BodyMap, isSing, isTimetamp, headerMap);
            if ("application/x-www-form-urlencoded".equalsIgnoreCase(headerMap.get("Content-Type").trim()) || headerMap.get("Content-Type").isEmpty() || headerMap.get("Content-Type").length() == 0) {
                long startTime = System.currentTimeMillis();
                List list = HttpClientDriver.formPost(url, sortedMap, headerMap);
                long endTime = System.currentTimeMillis();
                batchResponseBody.setProjectName(excelDto.getProjectName());
                batchResponseBody.setCaseId(excelDto.getId());
                batchResponseBody.setCaseName(excelDto.getCaseName());
                batchResponseBody.setIsPass(batchParameterNormalization.isPassed(excelDto,list.get(1).toString()));
                batchResponseBody.setExpected(excelDto.getExpect());
                batchResponseBody.setRequestParam(StringUtils.replace(excelDto.getRequestBody(),"\n",""));
                batchResponseBody.setResponseTime(endTime - startTime);
                batchResponseBody.setCode(list.get(0).toString());
                batchResponseBody.setMessage(list.get(1).toString());
                return batchResponseBody;
            }else if("application/json".equalsIgnoreCase(headerMap.get("Content-Type").trim())){
                List list = null;
                long startTime = System.currentTimeMillis();
                try {
                    list = HttpClientDriver.httpPost(url, sortedMap, headerMap);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                long endTime = System.currentTimeMillis();
                batchResponseBody.setProjectName(excelDto.getProjectName());
                batchResponseBody.setCaseId(excelDto.getId());
                batchResponseBody.setCode(list.get(0).toString());
                batchResponseBody.setCaseName(excelDto.getCaseName());
                batchResponseBody.setExpected(excelDto.getExpect());
                batchResponseBody.setResponseTime(endTime - startTime);
                batchResponseBody.setRequestParam(StringUtils.replace(excelDto.getRequestBody(),"\n",""));
                batchResponseBody.setIsPass(batchParameterNormalization.isPassed(excelDto,list.get(1).toString()));
                batchResponseBody.setMessage(list.get(1).toString());
                return batchResponseBody;
            }else {
                batchResponseBody.setCode("250");
                batchResponseBody.setMessage("还TM没实现");
                return batchResponseBody;
            }
        }
        return null;
    }
}
