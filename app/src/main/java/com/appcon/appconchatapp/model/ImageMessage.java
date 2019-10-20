package com.appcon.appconchatapp.model;

public class ImageMessage extends Message {

    String imageURL;
    String text;
    int size;
    boolean downloaded;

    public ImageMessage(String messageID, String senderID, String senderName, String senderPhoneNumber, String time, String imageURL, String text, int size, boolean downloaded) {
        super(messageID, senderID, senderName, senderPhoneNumber, time);
        this.imageURL = imageURL;
        this.text = text;
        this.size = size;
        this.downloaded = downloaded;
    }

    public String getImageURL() {
        return imageURL;
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
}
