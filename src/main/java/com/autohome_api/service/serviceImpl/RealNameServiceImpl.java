package com.autohome_api.service.serviceImpl;

import com.autohome_api.commons.GetRequestProperty;
import com.autohome_api.commons.HttpClientDriver;
import com.autohome_api.dto.*;
import com.autohome_api.service.RealNameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class RealNameServiceImpl implements RealNameService {
    public static final Logger logger = LoggerFactory.getLogger(RealNameServiceImpl.class);
    @Autowired
    private GetRequestProperty getRequestProperty;
    @Override
    public ResponseParam getResponse(RequestBean requestBean) {
        String url = requestBean.getRequestUrl().getUrl();
        RequestHeader headers = requestBean.getRequestHeader();
        HashMap map = GetRequestProperty.objectToMap(headers);
        String requestType = requestBean.getRequestType().getRequestType();
        String requestHeaderData = (String) map.get("Content-Type");
        RequestParam requestParam = requestBean.getRequestParam();
        if("get".equalsIgnoreCase(requestType.trim())){
            ResponseParam responseParam = new ResponseParam();
            String host = getRequestProperty.getLastUrl(requestParam,url);
            if("html/text".equalsIgnoreCase(requestHeaderData.trim())|| requestHeaderData == null || requestHeaderData.length() == 0){
                List list = HttpClientDriver.httpGet(host, map);
                logger.info("返回数据为：" + list.toString());
                responseParam.setCode(list.get(0).toString());
                responseParam.setMessage(list.get(1).toString());
                return responseParam;
            }
        }
        return null;
    }

    @Override
    public ResponseParam getResponse(RequestParams requestParams) {
        String url = requestParams.getRequestUrl();
        String headers = requestParams.getRequestHeader().toString();
        String isSing = requestParams.getIsSign();
        String isTimetamp = requestParams.getIs_timestamp();
        String requestBody = requestParams.getRequestBody().toString();
        HashMap<String,String> hashMap = GetRequestProperty.returnHashMap(headers);
        HashMap<String, String> map = new HashMap();
        map.put("_appid", requestParams.get_appid());
        map.put("sign_method", requestParams.getSign_method());
        map.put("format", requestParams.getFormat());
        if ("get".equalsIgnoreCase(requestParams.getRequestType().trim())) {
            ResponseParam responseParam = new ResponseParam();
            String host = getRequestProperty.getURL(requestBody, url, isSing, isTimetamp, map);
            List list = HttpClientDriver.httpGet(host, hashMap);
            responseParam.setCode(list.get(0).toString());
            responseParam.setMessage(list.get(1).toString());
            return responseParam;
        }else if("post".equalsIgnoreCase(requestParams.getRequestType().trim())){
            ResponseParam responseParam = new ResponseParam();
            SortedMap sortedMap = getRequestProperty.getBody(requestBody, isSing, isTimetamp, map);
            if ("application/x-www-form-urlencoded".equalsIgnoreCase(hashMap.get("Content-Type").trim()) || hashMap.get("Content-Type").isEmpty() || hashMap.get("Content-Type").length() == 0) {
                List list = HttpClientDriver.formPost(url, sortedMap, hashMap);
                responseParam.setCode(list.get(0).toString());
                responseParam.setMessage(list.get(1).toString());
                return responseParam;
            }else if("application/json".equalsIgnoreCase(hashMap.get("Content-Type").trim())){
                List list = null;
                try {
                    list = HttpClientDriver.httpPost(url, sortedMap, hashMap);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                responseParam.setCode(list.get(0).toString());
                responseParam.setMessage(list.get(1).toString());
                return responseParam;
            }else {
                responseParam.setCode("250");
                responseParam.setMessage("还TM没实现");
                return responseParam;
            }
        }
        return null;
    }
}
