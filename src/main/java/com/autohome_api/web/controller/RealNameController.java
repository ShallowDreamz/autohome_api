package com.autohome_api.web.controller;

import com.autohome_api.dto.RequestBean;
import com.autohome_api.dto.RequestParams;
import com.autohome_api.dto.ResponseParam;
import com.autohome_api.service.RealNameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@Controller
public class RealNameController {
    public static final Logger logger = LoggerFactory.getLogger(RealNameController.class);
    @Autowired
    private RealNameService realNameService;
    @RequestMapping(method= RequestMethod.POST,value = "/returnRealNameData",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseParam returnRealNameData(@RequestBody RequestBean requestBean){
        logger.info(requestBean.toString());
        ResponseParam responseParam = realNameService.getResponse(requestBean);
        return responseParam;
    }
    @RequestMapping(method= RequestMethod.POST,value = "/returnResponseData",produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseParam returnResponseData(@RequestBody RequestParams requestParams){
        logger.info(requestParams.toString());
        try {
            ResponseParam responseParam = realNameService.getResponse(requestParams);
            return responseParam;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
