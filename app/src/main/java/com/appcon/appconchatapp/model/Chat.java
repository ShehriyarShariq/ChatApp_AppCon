package com.appcon.appconchatapp.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Chat {

    private String chatID, lastMessageSeen, otherUser;
    private boolean muted;

    public Chat(String chatID, String lastMessageSeen, boolean muted, String otherUser) {
        this.chatID = chatID;
        this.lastMessageSeen = lastMessageSeen;
        this.muted = muted;
        this.otherUser = otherUser;
    }

    public String getChatID() {
        return chatID;
    }

    public String getLastMessageSeen() {
        return lastMessageSeen;
    }

    public String getOtherUser() {
        return otherUser;
    }

    public boolean isMuted() {
        return muted;
    }

    public HashMap<String, String> getDBMap(String type){
        HashMap<String, String> dbMap = new HashMap<>();

        dbMap.put("lastMessageSeen", lastMessageSeen);
        dbMap.put("muted", String.valueOf(muted));

        if(type.equals("personal")){
            dbMap.put("otherUser", otherUser);
        }

        return dbMap;
    }
}
