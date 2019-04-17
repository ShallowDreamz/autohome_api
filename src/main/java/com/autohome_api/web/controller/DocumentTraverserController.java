package com.autohome_api.web.controller;

import ch.ethz.ssh2.Connection;
import com.autohome_api.commons.RemoteCommandUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class DocumentTraverserController {
    @Autowired
    private RemoteCommandUtil remoteCommandUtil;
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping(method= RequestMethod.GET,value = "/resultDocument/{path}")
    @ResponseBody
    public String findDocument(@PathVariable String path){
        Connection login = remoteCommandUtil.login();
        String execute = remoteCommandUtil.execute(login);
        System.out.println(execute);
        return execute;
    }
}
