package com.appcon.appconchatapp.model;

import java.util.HashMap;

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

    public HashMap<String, String> getMap(){
        HashMap<String, String> map = new HashMap<>();

        map.put("content", text);
        map.put("sentBy", super.senderID);
        map.put("timeStamp", super.time);
        map.put("type", "text");

        return map;
    }
}
