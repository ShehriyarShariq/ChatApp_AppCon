package com.appcon.appconchatapp.model;

public class Conversation {
    String displayName;
    String lastMessage;
    String time;

    public Conversation(String displayName, String lastMessage, String time) {
        this.displayName = displayName;
        this.lastMessage = lastMessage;
        this.time = time;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
