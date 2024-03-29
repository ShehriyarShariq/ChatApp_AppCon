package com.appcon.appconchatapp.model;

import java.util.HashMap;

public class VideoMessage extends Message {
    String videoURL;
    String text;
    int size;
    boolean downloaded;

    public VideoMessage(String messageID, String senderID, String senderName, String senderPhoneNumber, String time, String videoURL, String text, int size, boolean downloaded) {
        super(messageID, senderID, senderName, senderPhoneNumber, time);
        this.videoURL = videoURL;
        this.text = text;
        this.size = size;
        this.downloaded = downloaded;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getText() {
        return text;
    }

    public int getSize() {
        return size;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public HashMap<String, String> getMap(){
        HashMap<String, String> map = new HashMap<>();

        return map;
    }
}
