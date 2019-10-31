package com.appcon.appconchatapp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {

    String userID, displayName, phoneNumber, profilePicture, about, dateOfCreation, status;
    List<String> partOfGroups;
    boolean muted;
    int score;

    public User(String userID, String displayName, String phoneNumber, String profilePicture, int score, String about, String status) {
        this.userID = userID;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
        this.score = score;
        this.about = about;
        partOfGroups = new ArrayList<>();
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public int getScore() {
        return score;
    }

    public String getAbout() {
        return about;
    }

    public List<String> getPartOfGroups() {
        return partOfGroups;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void toggleMute() {
        this.muted = !this.muted;
    }

    public void addGroup(String groupID) {
        partOfGroups.add(groupID);
    }

    public HashMap<String, String> getAuthMap(){
        HashMap<String, String> authMap = new HashMap<>();
        authMap.put("phoneNumber", phoneNumber);
        authMap.put("displayName", displayName);
        return authMap;
    }

    public HashMap<String, Object> getDBMap(){
        HashMap<String, Object> dbMap = new HashMap<>();

        dbMap.put("displayName", displayName);
        dbMap.put("phoneNumber", phoneNumber);
        dbMap.put("displayPicture", profilePicture);
        dbMap.put("creationDate", dateOfCreation);
        dbMap.put("score", score);
        dbMap.put("about", about);

        HashMap<String, Object> chats = new HashMap<>();
        HashMap<String, String> dummyChat = new HashMap<>();
        dummyChat.put("chatID", "lastMsgSeenID");
        dbMap.put("chats", dummyChat);

        dbMap.put("status", status);

        return dbMap;
    }

    public HashMap<String, Object> newUserMaps(){
        HashMap<String, Object> newUserMap = new HashMap<>();
        newUserMap.put("auth", getAuthMap());
        newUserMap.put("db", getDBMap());
        return newUserMap;
    }

}
