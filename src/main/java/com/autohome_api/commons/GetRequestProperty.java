package com.autohome_api.commons;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.autohome_api.dto.RequestHeader;
import com.autohome_api.dto.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class GetRequestProperty {
    public static final Logger logger = LoggerFactory.getLogger(GetRequestProperty.class);

    @Autowired
    private GetSign getSign;
    public String getURL(String requestBody,String host,String isSign,String is_timestamp,HashMap<String,String> hashMap){
        SortedMap<String, String> sortedMap = getSortedMap(requestBody, isSign, is_timestamp, hashMap);
        return getLastURL(host, sortedMap);
    }

    private String getLastURL(String host, SortedMap<String, String> sortedMap) {
        StringBuilder url = new StringBuilder();
        url.append(host + "?");
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            url.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        logger.info("最终生成的URL为：" + url.toString().substring(0, url.length() - 1));
        return url.toString().substring(0, url.length() - 1);
    }

    public SortedMap<String,String> getBody(String requestBody,String isSign,String is_timestamp,HashMap<String,String> hashMap){
        SortedMap<String, String> sortedMap = getSortedMap(requestBody, isSign, is_timestamp, hashMap);
        return sortedMap;
    }

    private SortedMap<String, String> getSortedMap(String requestBody, String isSign, String is_timestamp, HashMap<String, String> hashMap) {
        long _timestamp = System.currentTimeMillis() / 1000;
        HashMap<String, String> map = returnHashMap(requestBody);
        SortedMap<String, String> sortedMap = new TreeMap();
        sortedMap.putAll(map);
        sortedMap.putAll(hashMap);
        if ("Y".equals(is_timestamp.toUpperCase())) {
            sortedMap.put("_timestamp", String.valueOf(_timestamp));
        }
        if ("Y".equals(isSign.toUpperCase())) {
            String sign = getSign.getSignString(sortedMap);
            sortedMap.put("_sign", sign);
        }
        return sortedMap;
    }

    public String getLastUrl(RequestParam requestParam, String host){
        long _timestamp = System.currentTimeMillis()/1000;
        String requestParams = requestParam.getRequestBody().replaceAll("\"","");
        SortedMap<String, String> sortedMap = new TreeMap<String, String>();
        HashMap map = returnMap(requestParams);
        sortedMap.putAll(map);
        sortedMap.put("_timestamp", String.valueOf(_timestamp));
        String sign = getSign.getSignString(sortedMap);
        sortedMap.put("_sign",sign);
        return getLastURL(host, sortedMap);
    }

    public static HashMap<String, String> objectToMap(RequestHeader requestHeader) {
        HashMap<String, String> map;
        String requestHeaders = requestHeader.getRequestHeaderData().replaceAll("\"","");
        map = returnMap(requestHeaders);
        return map;
    }
    public static HashMap returnMap(String obj){
        HashMap<String,String> map = new HashMap();
        if(obj.length() != 0 && obj != null){
            String params[] = obj.split(",");
            for (int i = 0; i < params.length; i++) {
                if(params[i].split(":").length != 1){
                    map.put(params[i].split(":")[0], params[i].split(":")[1]);
                }else{
                    map.put(params[i].split(":")[0], "");
                }
            }
            return map;
        }
        return map;
    }
    public static HashMap returnHashMap(String obj){
        HashMap<String, String> map = new HashMap<String, String>();
        if(obj.length() != 0 && obj != null) {
            JSONArray jsonArray = JSONArray.parseArray(obj);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = JSONObject.parseObject(jsonArray.get(i).toString());
                map.put(jsonObject.getString("key"),jsonObject.getString("value"));
            }
            return map;
        }
        return map;
    }
}
