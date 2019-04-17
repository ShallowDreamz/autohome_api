package com.autohome_api.service.serviceImpl;

import com.autohome_api.service.SftpService;
import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

@Service
public class SftpServiceImpl implements SftpService {
    public static final Logger logger = LoggerFactory.getLogger(BatchProcessingServiceImpl.class);
    @Value("${host}")
    private String host;
    @Value("${port}")
    private int port;
    @Value("${username}")
    private String username;
    @Value("${password}")
    private String password;
    @Value("${directory}")
    private String directory;
    private Session session;
    private Channel channel;
    private ChannelSftp channelSftp;
    private static Date last_push_date = null;

    @Override
    public void createChannel() {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("userauth.gssapi-with-mic", "no");
            //为session对象设置properties,第一次访问服务器时不用输入yes
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            //获取sftp通道
            channel = session.openChannel("sftp");
            channel.connect();
            channelSftp = (ChannelSftp) channel;
            logger.info("连接ftp成功!" + session);
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeChannel() {
        if (null != channel) {
            try {
                channel.disconnect();
            } catch (Exception e) {
                logger.error("关闭SFTP通道发生异常:", e);
            }
        }
        if (null != session) {
            try {
                session.disconnect();
            } catch (Exception e) {
                logger.error("SFTP关闭 session异常:", e);
            }
        }
    }

    @Override
    public boolean isConnected() {
        return null != channel && channel.isConnected();
    }
    @Override
    public boolean uploadFile(String uploadFile) {
        try {
            channelSftp.cd(directory);
            File file = new File(uploadFile);
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            channelSftp.put(inputStream, file.getName());
            if (inputStream != null){
                inputStream.close();
                return true;
            }
            if (file.exists()) {
                //boolean b = file.delete();
                logger.info(file.getName() + "文件上传完毕!");
            }
        } catch (SftpException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void uploadFiles(String uploadFile){
        try {
            channelSftp.ls(directory);//执行列表展示ls 命令
            channelSftp.cd(directory);//执行盘符切换cd 命令
            List<File> files = getFiles(uploadFile, new ArrayList<File>());
            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                InputStream input = new BufferedInputStream(new FileInputStream(file));
                channelSftp.put(input, file.getName());
                try {
                    if (input != null) input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(file.getName() + "关闭文件时.....异常!" + e.getMessage());
                }
                if (file.exists()) {
                    //boolean b = file.delete();
                    logger.info(file.getName() + "文件上传完毕!");
                }
            }
        }catch (Exception e) {
            logger.error("【子目录创建中】：",e);
            //创建子目录
            try {
                channelSftp.mkdir(directory);
            } catch (SftpException e1) {
                e1.printStackTrace();
            }
        }

    }
    //获取文件
    public List<File> getFiles(String realpath, List<File> files) {
        File realFile = new File(realpath);
        if (realFile.isDirectory()) {
            File[] subfiles = realFile.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    if (null == last_push_date ) {
                        return true;
                    } else {
                        long modifyDate = file.lastModified();
                        return modifyDate > last_push_date.getTime();
                    }
                }
            });
            for (File file : subfiles) {
                if (file.isDirectory()) {
                    getFiles(file.getAbsolutePath(), files);
                } else {
                    files.add(file);
                }
                if (null == last_push_date) {
                    last_push_date = new Date(file.lastModified());
                } else {
                    long modifyDate = file.lastModified();
                    if (modifyDate > last_push_date.getTime()) {
                        last_push_date = new Date(modifyDate);
                    }
                }
            }
        }
        return files;
    }

    @Override
    public Vector vector(String directory) throws SftpException {
        createChannel();
        channelSftp.cd("/tmp");
        Vector vector = channelSftp.ls(directory);
        return vector;
    }
}
