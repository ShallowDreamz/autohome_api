package com.autohome_api.commons;

import com.alibaba.fastjson.JSONObject;
import com.autohome_api.commons.ExcelToDto.dto.ExcelDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class BatchParameterNormalization {
    public static final Logger logger = LoggerFactory.getLogger(BatchParameterNormalization.class);
    @Autowired
    private GetSign getSign;
    public String getExcelURL(HashMap bodyMap, String host, String isSign, String is_timestamp, HashMap<String,String> hashMap){
        SortedMap<String, String> sortedMap = getSortedMap(bodyMap, isSign, is_timestamp, hashMap);
        return getLastURL(host, sortedMap);
    }
    public SortedMap<String,String> getExcelBody(HashMap bodyMap,String isSign,String is_timestamp,HashMap<String,String> hashMap){
        SortedMap<String, String> sortedMap = getSortedMap(bodyMap, isSign, is_timestamp, hashMap);
        return sortedMap;
    }
    private SortedMap<String, String> getSortedMap(HashMap bodyMap, String isSign, String is_timestamp, HashMap<String, String> hashMap) {
        long _timestamp = System.currentTimeMillis() / 1000;
        SortedMap<String, String> sortedMap = new TreeMap();
        sortedMap.putAll(bodyMap);
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
    private String getLastURL(String host, SortedMap<String, String> sortedMap) {
        StringBuilder url = new StringBuilder();
        url.append(host + "?");
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            url.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        logger.info("最终生成的URL为：" + url.toString().substring(0, url.length() - 1));
        return url.toString().substring(0, url.length() - 1);
    }
    public HashMap<String,String> paramOverWrite(String params){
        String params_reg = replaceSpecStr(StringUtils.remove(StringUtils.deleteWhitespace(params),"\n"),",");
        HashMap<String, String> map = new HashMap<>();
        String parameterInfo[] = StringUtils.split(params_reg, ",");
        for (int paramIndex = 0; paramIndex < parameterInfo.length; paramIndex++) {
            String parameterInfo_reg = replaceSpecStr(parameterInfo[paramIndex],">");
            String parameterMode[] = StringUtils.split(parameterInfo_reg, '>');
            map.put(parameterMode[0], parameterMode[1]);
        }
        return map;
    }
    public String replaceSpecStr(String orgStr,String replacement){
        if (null!=orgStr&&!"".equals(orgStr.trim())) {
            String regEx="[\\s~·`!！@#￥$%^……&*（()）=+【\\[\\]】｛{}｝\\|、\\\\；;：:‘'“”\"《<。》、？?]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(orgStr);
            return m.replaceAll(replacement);
        }
        return null;
    }
    public String isPassed(ExcelDto excelDto,String response){
        String exceptCode = excelDto.getExpect();
        JSONObject jsonObject = JSONObject.parseObject(response);
        String realCode = jsonObject.getString("returnCode");
        return exceptCode.equals(realCode)?"通过":"失败";
    }
}
