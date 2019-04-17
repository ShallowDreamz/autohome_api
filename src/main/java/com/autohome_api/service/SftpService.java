package com.autohome_api.service;

import com.jcraft.jsch.SftpException;

import java.util.Vector;

public interface SftpService {
    void createChannel();
    void closeChannel();
    boolean uploadFile(String uploadFile);
    boolean isConnected();
    void uploadFiles(String uploadFile);
    Vector vector(String directory) throws SftpException;
}
