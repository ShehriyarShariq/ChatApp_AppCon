package com.appcon.appconchatapp.model;

public class FileMessage extends Message{

    String fileURL, fileSize;

    public FileMessage(String messageID, String senderID, String senderName, String senderPhoneNumber, String time, String fileURL, String fileSize) {
        super(messageID, senderID, senderName, senderPhoneNumber, time);
        this.fileURL = fileURL;
        this.fileSize = fileSize;
    }

    public String getFileURL() {
        return fileURL;
    }

    public String getFileSize() {
        return fileSize;
    }
}
