package com.autohome_api.commons;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class Path {
    @Value("${path}")
    public String path;
    public String getPath(MultipartFile file){
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(currentTime);
        String lastPath = path + dateString + file.getOriginalFilename();
        return lastPath;
    }
}
