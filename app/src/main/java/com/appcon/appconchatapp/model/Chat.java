package com.appcon.appconchatapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;

public class Chat implements Parcelable {

    private String chatID, displayName, lastMessageSeen, otherUser, time;
    private boolean muted, pinned;

    public Chat(String chatID, String displayName, String lastMessageSeen, boolean muted, boolean pinned, String otherUser) {
        this.chatID = chatID;
        this.displayName = displayName;
        this.lastMessageSeen = lastMessageSeen;
        this.muted = muted;
        this.pinned = pinned;
        this.otherUser = otherUser;
    }

    protected Chat(Parcel in) {
        chatID = in.readString();
        displayName = in.readString();
        lastMessageSeen = in.readString();
        otherUser = in.readString();
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

    public String getLastMessageSeen() {
        return lastMessageSeen;
    }

    public String getOtherUser() {
        return otherUser;
    }

    public String getTime() {
        return time;
    }

    public boolean isMuted() {
        return muted;
    }

    public boolean isPinned() {
        return pinned;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chatID);
        dest.writeString(displayName);
        dest.writeString(lastMessageSeen);
        dest.writeString(otherUser);
        dest.writeString(time);
        dest.writeByte((byte) (muted ? 1 : 0));
        dest.writeByte((byte) (pinned ? 1 : 0));
    }
}
