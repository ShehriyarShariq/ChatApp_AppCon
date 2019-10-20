package com.appcon.appconchatapp.model;

public abstract class Message {

    String messageID;
    String senderID;
    String senderName;
    String senderPhoneNumber;
    String time;

    public Message(String messageID, String senderID, String senderName, String senderPhoneNumber, String time) {
        this.messageID = messageID;
        this.senderID = senderID;
        this.senderName = senderName;
        this.senderPhoneNumber = senderPhoneNumber;
        this.time = time;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderPhoneNumber() {
        return senderPhoneNumber;
    }

    public String getTime() {
        return time;
    }
}
