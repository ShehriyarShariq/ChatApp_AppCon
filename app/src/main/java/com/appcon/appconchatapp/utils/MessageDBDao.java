package com.appcon.appconchatapp.utils;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.appcon.appconchatapp.model.MessageDB;

import java.util.List;

@Dao
public interface MessageDBDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(MessageDB message);

    @Query("SELECT * FROM message_table")
    LiveData<List<MessageDB>> getMessages();

    @Query("SELECT * FROM message_table WHERE chatID = :chatID")
    LiveData<List<MessageDB>> getChatMessages(String chatID);

    @Query("SELECT * FROM message_table WHERE messageID = :messageID")
    MessageDB getMessage(String messageID);

    @Query("DELETE FROM message_table WHERE messageID = :messageID")
    void deleteMessage(String messageID);

    @Query("DELETE FROM message_table WHERE chatID = :chatID")
    void deleteChat(String chatID);

}
