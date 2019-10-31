package com.appcon.appconchatapp.utils;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.appcon.appconchatapp.model.ChatDB;
import com.appcon.appconchatapp.model.MessageDB;

@Database(entities = {MessageDB.class, ChatDB.class}, version = 1, exportSchema = false)
public abstract class ChatAppRoomDatabase extends RoomDatabase {

    public abstract MessageDBDao getMessageDBDao();
    public abstract ChatDBDao getChatDBDao();

    private static volatile ChatAppRoomDatabase INSTANCE;

    static ChatAppRoomDatabase getDatabase(Context context){
        if(INSTANCE == null){
            synchronized (ChatAppRoomDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            ChatAppRoomDatabase.class,
                            "chat_app_database"
                    ).build();
                }
            }
        }

        return INSTANCE;
    }

}
