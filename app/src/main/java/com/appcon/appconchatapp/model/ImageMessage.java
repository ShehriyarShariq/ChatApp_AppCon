package com.appcon.appconchatapp.model;

import java.util.HashMap;

public class ImageMessage extends Message {

    String imageURL;
    String text;
    long size;

    public ImageMessage(String messageID, String senderID, String senderName, String senderPhoneNumber, String time, String imageURL, String text, long size) {
        super(messageID, senderID, senderName, senderPhoneNumber, time);
        this.imageURL = imageURL;
        this.text = text;
        this.size = size;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getText() {
        return text;
    }

    public long getSize() {
        return size;
    }

    public HashMap<String, String> getMap(){
        HashMap<String, String> map = new HashMap<>();

        map.put("content", text + "_" + size + "_" + imageURL );
        map.put("sentBy", super.senderID);
        map.put("timeStamp", super.time);
        map.put("type", "image");

        return map;
    }
}
