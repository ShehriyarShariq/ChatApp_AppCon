package com.appcon.appconchatapp.model;

public class Story {

    String storyID, senderID, senderName;

    public Story(String storyID, String senderID, String senderName) {
        this.storyID = storyID;
        this.senderID = senderID;
        this.senderName = senderName;
    }

    public String getStoryID() {
        return storyID;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getSenderName() {
        return senderName;
    }
}
