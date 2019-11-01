package com.appcon.appconchatapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

public class Chat implements Parcelable {

    private String chatID, displayName, displayPicture, creationDate, description, lastMessageSeenID, lastMessageSeen, time, lastMessageSeenType;
    private boolean muted, pinned;
    private HashMap<String, String> permissions;
    private ArrayList<String> admins, otherUsers;

    public Chat(String chatID, String displayName, String displayPicture, String creationDate, String description, String lastMessageSeenID, String lastMessageSeen, String time, String lastMessageSeenType, boolean muted, boolean pinned, HashMap<String, String> permissions, ArrayList<String> admins, ArrayList<String> otherUsers) {
        this.chatID = chatID;
        this.displayName = displayName;
        this.displayPicture = displayPicture;
        this.creationDate = creationDate;
        this.description = description;
        this.lastMessageSeenID = lastMessageSeenID;
        this.lastMessageSeen = lastMessageSeen;
        this.time = time;
        this.lastMessageSeenType = lastMessageSeenType;
        this.muted = muted;
        this.pinned = pinned;
        this.permissions = permissions;
        this.admins = admins;
        this.otherUsers = otherUsers;
    }

    protected Chat(Parcel in) {
        chatID = in.readString();
        displayName = in.readString();
        displayPicture = in.readString();
        creationDate = in.readString();
        description = in.readString();
        lastMessageSeenID = in.readString();
        lastMessageSeen = in.readString();
        time = in.readString();
        lastMessageSeenType = in.readString();
        admins = in.readArrayList(String.class.getClassLoader());
        otherUsers = in.readArrayList(String.class.getClassLoader());
        permissions = in.readHashMap(String.class.getClassLoader());
        time = in.readString();
        muted = in.readByte() != 0;
        pinned = in.readByte() != 0;
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

    public String getChatID() {
        return chatID;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDisplayPicture() {
        return displayPicture;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getDescription() {
        return description;
    }

    public String getLastMessageSeenID() {
        return lastMessageSeenID;
    }

    public String getLastMessageSeen() {
        return lastMessageSeen;
    }

    public String getTime() {
        return time;
    }

    public String getLastMessageSeenType() {
        return lastMessageSeenType;
    }

    public boolean isMuted() {
        return muted;
    }

    public boolean isPinned() {
        return pinned;
    }

    public HashMap<String, String> getPermissions() {
        return permissions;
    }

    public ArrayList<String> getAdmins() {
        return admins;
    }

    public ArrayList<String> getOtherUsers() {
        return otherUsers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chatID);
        dest.writeString(displayName);
        dest.writeString(displayPicture);
        dest.writeString(creationDate);
        dest.writeString(description);
        dest.writeString(lastMessageSeenID);
        dest.writeString(lastMessageSeen);
        dest.writeString(time);
        dest.writeString(lastMessageSeenType);
        dest.writeStringList(admins);
        dest.writeStringList(otherUsers);
        dest.writeSerializable(permissions);
        dest.writeString(time);
        dest.writeByte((byte) (muted ? 1 : 0));
        dest.writeByte((byte) (pinned ? 1 : 0));
    }

    public void setLastMessage(MessageDB message) {
        this.lastMessageSeenID = message.getMessageID();
        this.lastMessageSeen = message.getContent();
        this.time = message.getTimeStamp();
    }

    public HashMap<String, Object> getDBMap(){
        HashMap<String, Object> dbMap = new HashMap<>();

        dbMap.put("displayName", displayName);
        dbMap.put("displayPicture", displayPicture);
        dbMap.put("creationDate", creationDate);
        dbMap.put("description", description);
        dbMap.put("lastMessageSeen", lastMessageSeenID);

        dbMap.put("members", otherUsers);

        ArrayList<String> admins = new ArrayList<>();
        admins.add("userID");
        dbMap.put("admins", admins);

        HashMap<String, String> permissions = new HashMap<>();
        permissions.put("adminWriteOnly", "false");
        permissions.put("adminSettingsEditOnly", "false");

        dbMap.put("permissions", permissions);

        return dbMap;
    }
}
