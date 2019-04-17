package com.test;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SSHLinux {

    public static void main(String[] args) throws IOException, JSchException {
        // TODO Auto-generated method stub
        String host = "47.105.36.116";
        int port = 22;
        String user = "root";
        String password = "Zhang01151816";
        String command = "ls";
        String res = exeCommand(host,port,user,password,command);

        System.out.println(res);

    }


    public static String exeCommand(String host, int port, String user, String password, String command){

        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(user, host, port);
        } catch (JSchException e) {
            e.printStackTrace();
        }
        session.setConfig("StrictHostKeyChecking", "no");
        //    java.util.Properties config = new java.util.Properties();
        //   config.put("StrictHostKeyChecking", "no");

        session.setPassword(password);
        try {
            session.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }

        ChannelExec channelExec = null;
        try {
            channelExec = (ChannelExec) session.openChannel("exec");
        } catch (JSchException e) {
            e.printStackTrace();
        }
        InputStream in = null;
        try {
            in = channelExec.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        channelExec.setCommand(command);
        channelExec.setErrStream(System.err);
        try {
            channelExec.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
        String out = null;
        try {
            out = IOUtils.toString(in, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        channelExec.disconnect();
        session.disconnect();

        return out;
    }

}
