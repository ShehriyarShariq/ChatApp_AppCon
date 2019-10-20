package com.appcon.appconchatapp.model;

public class TextMessage extends Message {

    String text;

    public TextMessage(String messageID, String senderID, String senderName, String senderPhoneNumber, String time, String text) {
        super(messageID, senderID, senderName, senderPhoneNumber, time);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
