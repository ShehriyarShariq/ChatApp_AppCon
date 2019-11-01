package com.appcon.appconchatapp.model;

import java.util.HashMap;

public class FileMessage extends Message{

    String fileURL;
    long fileSize;

    public FileMessage(String messageID, String senderID, String senderName, String senderPhoneNumber, String time, String fileURL, long fileSize) {
        super(messageID, senderID, senderName, senderPhoneNumber, time);
        this.fileURL = fileURL;
        this.fileSize = fileSize;
    }

    public String getFileURL() {
        return fileURL;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public HashMap<String, String> getMap(){
        HashMap<String, String> map = new HashMap<>();

        map.put("content", fileSize + "_" + fileURL);
        map.put("sentBy", super.senderID);
        map.put("timeStamp", super.time);
        map.put("type", "file");

        return map;
    }
}
