package com.autohome_api.commons;

import java.io.*;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RemoteCommandUtil {

    @Value("${host}")
    private String host;
    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;
    @Value("${command}")
    private String command;
    private static final Logger logger = LoggerFactory.getLogger(RemoteCommandUtil.class);
    private String  DEFAULTCHART="UTF-8";

    public Connection login(){
        boolean flg;
        Connection conn = null;
        try {
            conn = new Connection(host);
            conn.connect();//连接
            flg=conn.authenticateWithPassword(username, password);//认证
            if(flg){
                logger.info("=========登录成功========="+conn);
                return conn;
            }
        } catch (IOException e) {
            logger.error("=========登录失败========="+e.getMessage());
            e.printStackTrace();
        }
        return conn;
    }

    public String execute(Connection conn,String command){
        String result="";
        try {
            if(conn !=null){
                Session session= conn.openSession();//打开一个会话
                session.requestPTY("bash");
                session.execCommand(command);//执行命令
                //session.startShell();
                result = processStdout(session.getStdout(),DEFAULTCHART);
                if(StringUtils.isBlank(result)){
                    logger.info("得到标准输出为空,链接conn:"+conn+",执行的命令："+ command);
                    //result=processStdout(session.getStderr(),DEFAULTCHART);
                }else{
                    logger.info("执行命令成功,链接conn:"+conn+",执行的命令："+ command);
                }
                conn.close();
                session.close();
            }
        } catch (IOException e) {
            logger.info("执行命令失败,链接conn:" + conn+",执行的命令："+ command + "  "+e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public Session createSession(Connection connection){
        Session session = null;
        try {
            session = connection.openSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return session;
    }

    public void closeSesion(Session session){
        if(session != null){
            session.close();
        }
    }
    public void closeConnection(Connection connection){
        if(connection != null){
            connection.close();
        }
    }
    public String execute(Connection conn){
        String result="";
        try {
            if(conn !=null){
                Session session= conn.openSession();//打开一个会话
                session.requestPTY("bash");
                session.execCommand(command);//执行命令
                //session.startShell();
                result = processStdout(session.getStdout(),DEFAULTCHART);
                /*PrintWriter out = new PrintWriter(session.getStdin());
                out.println(cmd);
                out.println("exit");
                // 6. 关闭输入流
                out.close();*/
                //如果为得到标准输出为空，说明脚本执行出错了
                if(StringUtils.isBlank(result)){
                    logger.info("得到标准输出为空,链接conn:"+conn+",执行的命令："+ command);
                    //result=processStdout(session.getStderr(),DEFAULTCHART);
                }else{
                    logger.info("执行命令成功,链接conn:"+conn+",执行的命令："+ command);
                }
                conn.close();
                session.close();
            }
        } catch (IOException e) {
            logger.info("执行命令失败,链接conn:" + conn+",执行的命令："+ command + "  "+e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    private String processStdout(InputStream in, String charset){
        InputStream  stdout = new StreamGobbler(in);
        StringBuffer buffer = new StringBuffer();;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(stdout,charset));
            String line=null;
            while((line=br.readLine()) != null){
                buffer.append(line+"\n");
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("解析脚本出错："+e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("解析脚本出错："+e.getMessage());
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
