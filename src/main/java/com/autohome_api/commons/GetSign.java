package com.autohome_api.commons;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.SortedMap;
import java.util.UUID;

@Component
// @PropertySource("classpath:appkey.properties")
public class GetSign {
    @Value("${ymkey}")
    public String appKey;

    public String getSignString(SortedMap<String,String> map){
        //String appKey="d10e48d9f833e5988659d94a8ba6082e";
        //String appKey = "123b364005698ak20ea65fcj735k3532";
        StringBuilder sb=new StringBuilder();
        sb.append(appKey);
        for (Map.Entry<String, String> me:map.entrySet())
            sb.append(me.getKey()+me.getValue());
        sb.append(appKey);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(sb.toString().getBytes());
        byte b[] = md.digest();
        int i;
        StringBuffer buf = new StringBuffer();
        for (int offset = 0; offset < b.length; offset++) {
            i = b[offset];
            if(i<0)i+= 256;
            if(i<16)buf.append("0");
            buf.append(Integer.toHexString(i));
        }
        return buf.toString().toUpperCase();
    }
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
   public static String getRequestNo() {
       String uuid = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
       return uuid;
   }
}
