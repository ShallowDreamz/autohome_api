package com.autohome_api.web.controller;

import ch.ethz.ssh2.Connection;
import com.autohome_api.commons.ExcelToDto.Utils.ExcelToDto;
import com.autohome_api.commons.ExcelToDto.dto.ExcelDto;
import com.autohome_api.commons.ExcelUtil;
import com.autohome_api.commons.HttpMailTemplete;
import com.autohome_api.commons.Path;
import com.autohome_api.commons.RemoteCommandUtil;
import com.autohome_api.dto.BatchResponseBody;
import com.autohome_api.dto.ReportParam;
import com.autohome_api.service.BatchProcessingService;
import com.autohome_api.service.ReportService;
import com.autohome_api.service.SftpService;
import com.autohome_api.service.serviceImpl.RealNameServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class BatchProcessingController {
    public static final Logger logger = LoggerFactory.getLogger(RealNameServiceImpl.class);
    @Autowired
    private ExcelUtil excelUtil;
    @Autowired
    private BatchProcessingService batchProcessingService;
    @Autowired
    private ExcelToDto excelToDto;
    @Autowired
    private HttpMailTemplete httpMailTemplete;
    @Autowired
    private SftpService sftpService;
    @Autowired
    private ReportService reportService;
    @RequestMapping(method= RequestMethod.POST,value = "/fileUpload")
    public String springUpload(@RequestParam("file") CommonsMultipartFile file,HttpServletRequest request) throws IOException {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String pathRoot = request.getSession().getServletContext().getRealPath("/upload/");
            File tempFile = new File(pathRoot + fileName);
            if (!tempFile.exists()) {
                tempFile.mkdir();
            }
            file.transferTo(tempFile);
            return "/success";
        } else {
            return "";
        }
    }
    @RequestMapping(method= RequestMethod.GET,value = "/read",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String springUpload(@RequestParam(value="path", required=false) String path){
        File f = new File(path);
        List<ExcelDto> list;
        List<BatchResponseBody> responseParamList = new ArrayList<>();
        try {
            list = excelToDto.readExcel(f, ExcelDto.class);
            for (int ExcelDtoIndex = 0; ExcelDtoIndex < list.size(); ExcelDtoIndex++) {
                BatchResponseBody responseParam = batchProcessingService.excelBatchService(list.get(ExcelDtoIndex));
                responseParamList.add(responseParam);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return httpMailTemplete.genHtmlContent(responseParamList);
    }
    //linux部署实现文件上传
    @RequestMapping(method= RequestMethod.POST,value = "/fileResultReport",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String fileResultReport(@RequestParam("file") CommonsMultipartFile file,HttpServletRequest request) throws IOException, InvalidFormatException, InstantiationException, IllegalAccessException {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String pathRoot = request.getSession().getServletContext().getRealPath("/upload/");
            File tempFile = new File(pathRoot + fileName);
            logger.info(pathRoot + fileName);
            if (!tempFile.exists()) {
                tempFile.mkdir();
                try {
                    file.transferTo(tempFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            List<BatchResponseBody> responseParamList = new ArrayList<>();
            List<ExcelDto> list =  excelUtil.excelToDTO(pathRoot + fileName, ExcelDto.class);
                /*Connection login = remoteCommandUtil.login();
                login.requestRemotePortForwarding();
                String s = remoteCommandUtil.execute(login, "ls");
                logger.info(String.valueOf(s));
                String execute = remoteCommandUtil.execute(login,"mv /opt/apache-tomcat-8.5.9/webapps/autohome_api/upload/" + fileName + " /root/zhb&&cd /root/zhb&&pwd");
                Connection login1 = remoteCommandUtil.login();
                String execute1 = remoteCommandUtil.execute(login1,"cd /root/zhb&&ls " + fileName);
                logger.info(StringUtils.replace(execute + "/" + execute1,"\n",""));
                List<BatchResponseBody> responseParamList = new ArrayList<>();
                List<ExcelDto> list =  excelUtil.excelToDTO(StringUtils.replace(execute + "/" + execute1,"\n",""), ExcelDto.class);*/
                /*for (int ExcelDtoIndex = 0; ExcelDtoIndex < list.size(); ExcelDtoIndex++) {
                    BatchResponseBody responseParam = batchProcessingService.excelBatchService(list.get(ExcelDtoIndex));
                    responseParamList.add(responseParam);
                }*/
            for (int ExcelDtoIndex = 0; ExcelDtoIndex < list.size(); ExcelDtoIndex++) {
                responseParamList.add(batchProcessingService.excelBatchService(list.get(ExcelDtoIndex)));
            }
            String report = httpMailTemplete.genHtmlContent(responseParamList);
            ReportParam reportParam = new ReportParam();
            reportParam.setProjectName(fileName);
            reportParam.setCreateDate(new Date());
            reportParam.setReport(report);
            reportService.insertReport(reportParam);
            return report;
        }
        return "上传文件为空！！！";
    }
    //windows部署实现文件上传
    @RequestMapping(method= RequestMethod.GET,value = "/resultReport",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String resultReport(@RequestParam(value="path", required=false) String path){
        sftpService.createChannel();
        logger.info(path);
        if(sftpService.uploadFile(path)){
            try {
                List<BatchResponseBody> responseParamList = new ArrayList<>();
                List<ExcelDto> list =  excelUtil.excelToDTO(path, ExcelDto.class);
                for (int ExcelDtoIndex = 0; ExcelDtoIndex < list.size(); ExcelDtoIndex++) {
                    BatchResponseBody responseParam = batchProcessingService.excelBatchService(list.get(ExcelDtoIndex));
                    responseParamList.add(responseParam);
                }
                return httpMailTemplete.genHtmlContent(responseParamList);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidFormatException e) {
                e.printStackTrace();
            }
        }
        sftpService.closeChannel();
        return "二逼,出错了。。。";
    }
}
