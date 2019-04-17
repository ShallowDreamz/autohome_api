package com.autohome_api.commons;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.*;

/**
 * Created by zhanghuibin on 2017/8/21.
 *
 */
public class HttpClientDriver {
    private static HttpResponse response;
    private static HttpEntity entity;
    public static CloseableHttpClient httpClient = HttpClientUtil.createSSLClientDefault();

    //接口以post方式请求，参数：url、body(map)、header
    public static List httpPost(String url, Map<String, Object> httpbody, HashMap<String, String> header) throws UnsupportedEncodingException {

        StringEntity entity;
        HttpPost post = new HttpPost(url);
        if (!header.isEmpty() || null != header) {
            Set<?> entrySet = header.entrySet();
            for (Iterator<?> itor = entrySet.iterator(); itor.hasNext();) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) itor.next();
                post.addHeader(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        String jsonBody;
        if(!httpbody.isEmpty()){
            jsonBody = JsonUtil.MapToJson(httpbody);
            entity = new StringEntity(jsonBody,"utf-8");
        }else{
            entity = new StringEntity("{,}");
        }
        post.setEntity(entity);
        List result = executeRequest(post);
        return result;
    }

    //接口以post方式请求，参数：url、body(string)、header
    public static List httpPost(String url, String str, HashMap<String,String> header){
        StringEntity entity = null;
        HttpPost post = new HttpPost(url);
        if (!header.isEmpty() || null != header) {
            Set<?> entrySet = header.entrySet();
            for (Iterator<?> itor = entrySet.iterator(); itor.hasNext();) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) itor.next();
                post.addHeader(entry.getKey().toString(), entry.getValue().toString());
            }
        }

        if(!str.isEmpty()){
            entity = new StringEntity(str,"utf-8");
        }else{
            try {
                entity = new StringEntity("{,}");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        post.setEntity(entity);
        List result = executeRequest(post);
        return result;
    }
    public static List formPost(String url, SortedMap<String,String> map, HashMap<String,String> header){
        HttpPost post = new HttpPost(url);
        if (!header.isEmpty() && null != header) {
            Set<?> entrySet = header.entrySet();
            for (Iterator<?> itor = entrySet.iterator(); itor.hasNext();) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) itor.next();
                post.addHeader(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        List<BasicNameValuePair> pair =new ArrayList();

        for (Map.Entry<String,String> entry: map.entrySet()) {
            pair.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
        }
        try {
            post.setEntity(new UrlEncodedFormEntity(pair,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        List result = executeRequest(post);
        return result;
    }

    //接口以get方式请求，参数：url、header，没有params
    public static List httpGet(String url,HashMap<String, String> header){
        HttpGet get = new HttpGet(url);
        if ( null != header) {
            Set<?> entrySet = header.entrySet();
            for (Iterator<?> itor = entrySet.iterator(); itor.hasNext();) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) itor.next();
                get.addHeader(entry.getKey().toString(), entry.getValue().toString());
            }
        }
        List list = executeRequest(get);
        return list;
    }

    //接口以get方式请求，参数：url、header，有params
    public static List httpGetOnParams(String url, String token, Map<String,String> map){
        URI uri = null;
        try {
            uri = getURI(url,map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpGet get = new HttpGet(uri);
        HttpResponse httpResponse;
        HttpEntity entity;
        get.setHeader("token",token);
        //get.setHeader("Content-Type","application/json");
        get.setHeader("version","1.0");
        get.setHeader("filter-key","filter-header");
        get.setHeader("deviceType","android");
        get.setHeader("deviceModel","le1");

        List list = executeRequest(get);
        return list;
    }
    private static URI getURI(String httpUrl, Map<String, String> map) throws Exception {
        String hostPort = httpUrl;
        String schema = "http";
        if (httpUrl.startsWith("http://")) {
            hostPort = httpUrl.substring("http://".length());
        } else if (httpUrl.startsWith("https://")) {
            hostPort = httpUrl.substring("https://".length());
            schema = "https";
        }
        String path = "/";
        int index = hostPort.indexOf("/");
        if (-1 != index) {
            path = hostPort.substring(index);
            hostPort = hostPort.substring(0, index);
        }
        String[] hostPortSplit = hostPort.split(":");
        String host = hostPort;
        int port = 80;
        if (2 == hostPortSplit.length) {
            host = hostPortSplit[0];
            port = Integer.parseInt(hostPortSplit[1]);
        }

        URIBuilder builder = new URIBuilder();
        if(schema.equals("http")){
            builder.setScheme(schema).setHost(host).setPort(port).setPath(path);
        }else if (schema.equals("https")){
            builder.setScheme(schema).setHost(host).setPath(path);
        }else{
            builder.setScheme(schema).setHost(host).setPort(port).setPath(path);
        }
        if (null != map) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.addParameter(entry.getKey(), entry.getValue());
            }
        }
        URI uri = builder.build();
        return uri;
    }

    //执行post或get的接口请求，并返回接口请求的结果
    private static List executeRequest(HttpUriRequest req){
        List list = new ArrayList();
        String result = null;
        HttpResponse httpResponse = null;
        HttpEntity entity;
        try{
            httpResponse = httpClient.execute(req);
            entity = httpResponse.getEntity();
            result = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        list.add(httpResponse.getStatusLine().getStatusCode());
        list.add(result);
        return list;
    }
}
