package com.appcon.appconchatapp.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    String userID, displayName, phoneNumber, profilePicture, about;
    List<String> partOfGroups;
    boolean muted;
    int score;

    public User(String userID, String displayName, String phoneNumber, String profilePicture, int score, String about) {
        this.userID = userID;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
        this.score = score;
        this.about = about;
        partOfGroups = new ArrayList<>();
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

}
