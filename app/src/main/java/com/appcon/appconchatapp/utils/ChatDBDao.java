package com.appcon.appconchatapp.utils;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.appcon.appconchatapp.model.ChatDB;

import java.util.List;

@Dao
public interface ChatDBDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertChats(List<ChatDB> chatDBS);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertChat(ChatDB chatDB);

    @Query("SELECT * FROM chat_table")
    LiveData<List<ChatDB>> getAllChats();

    @Query("SELECT * FROM chat_table WHERE chatID = :chatID")
    LiveData<ChatDB> getChat(String chatID);

    @Query("SELECT * FROM chat_table WHERE otherUsers IN(:uid)")
    LiveData<ChatDB> getChatByUser(String[] uid);

    @Query("DELETE FROM chat_table WHERE chatID = :chatID")
    void deleteChat(String chatID);
}
