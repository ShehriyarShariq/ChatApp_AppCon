package com.appcon.appconchatapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "message_table")
public class MessageDB {

    @PrimaryKey @NonNull private String messageID;
    @NonNull private String chatID;
    @NonNull private String senderID;
    @NonNull private String type;
    @NonNull private String content;
    @NonNull private String timeStamp;

    public MessageDB(String messageID, String chatID, String senderID, String type, String content, String timeStamp) {
        this.messageID = messageID;
        this.chatID = chatID;
        this.senderID = senderID;
        this.type = type;
        this.content = content;
        this.timeStamp = timeStamp;
    }

    @NonNull
    public String getMessageID() {
        return messageID;
    }

    @NonNull
    public String getChatID() {
        return chatID;
    }

    @NonNull
    public String getSenderID() {
        return senderID;
    }

    @NonNull
    public String getType() {
        return type;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    @NonNull
    public String getTimeStamp() {
        return timeStamp;
    }
}
