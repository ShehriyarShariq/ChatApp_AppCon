package com.appcon.appconchatapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.annotations.NotNull;

@Entity(tableName = "chat_table")
public class ChatDB {

    @PrimaryKey @NonNull private String chatID;
    @NonNull private String displayName;
    @NonNull private String displayPicture;
    @NonNull private String creationDate;
    @NonNull private String description;
    @NonNull private String lastMessageSeenID;
    @NonNull private String muted;
    @NonNull private String pinned;
    @NonNull private String adminWriteOnly;
    @NonNull private String adminSettingsEditOnly;
    @NonNull private String admins;
    @NonNull private String otherUsers;

    public ChatDB(String chatID, String displayName, String displayPicture, String creationDate, String description, String lastMessageSeenID, String muted, String pinned, String adminWriteOnly, String adminSettingsEditOnly, String admins, String otherUsers) {
        this.chatID = chatID;
        this.displayName = displayName;
        this.displayPicture = displayPicture;
        this.creationDate = creationDate;
        this.description = description;
        this.lastMessageSeenID = lastMessageSeenID;
        this.muted = muted;
        this.pinned = pinned;
        this.adminWriteOnly = adminWriteOnly;
        this.adminSettingsEditOnly = adminSettingsEditOnly;
        this.admins = admins;
        this.otherUsers = otherUsers;
    }

    @NonNull
    public String getChatID() {
        return chatID;
    }

    @NonNull
    public String getDisplayName() {
        return displayName;
    }

    @NonNull
    public String getDisplayPicture() {
        return displayPicture;
    }

    @NonNull
    public String getCreationDate() {
        return creationDate;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getLastMessageSeenID() {
        return lastMessageSeenID;
    }

    @NonNull
    public String getMuted() {
        return muted;
    }

    @NonNull
    public String getPinned() {
        return pinned;
    }

    @NonNull
    public String getAdminWriteOnly() {
        return adminWriteOnly;
    }

    @NonNull
    public String getAdminSettingsEditOnly() {
        return adminSettingsEditOnly;
    }

    @NonNull
    public String getAdmins() {
        return admins;
    }

    @NonNull
    public String getOtherUsers() {
        return otherUsers;
    }

    public void setLastMessageSeenID(String lastMessageSeenID) {
        this.lastMessageSeenID = lastMessageSeenID;
    }
}
