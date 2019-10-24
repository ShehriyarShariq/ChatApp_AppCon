package com.appcon.appconchatapp.model;

import java.util.HashMap;

public class GameMessage extends Message {

    public GameMessage(String messageID, String senderID, String senderName, String senderPhoneNumber, String time) {
        super(messageID, senderID, senderName, senderPhoneNumber, time);
    }

    public HashMap<String, String> getMap(){
        HashMap<String, String> map = new HashMap<>();

        return map;
    }
}
