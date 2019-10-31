package com.appcon.appconchatapp.utils;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.appcon.appconchatapp.model.ChatDB;
import com.appcon.appconchatapp.model.MessageDB;

import java.util.List;

public class ChatAppRepository {

    private MessageDBDao messageDBDao;
    private ChatDBDao chatDBDao;

    private LiveData<List<ChatDB>> allChats;
    private LiveData<List<MessageDB>> allMessages;
    private LiveData<ChatDB> chat;

    public ChatAppRepository(Application application){
        ChatAppRoomDatabase database = ChatAppRoomDatabase.getDatabase(application);
        messageDBDao = database.getMessageDBDao();
        chatDBDao = database.getChatDBDao();
        allChats = chatDBDao.getAllChats();
        allMessages = messageDBDao.getMessages();
    }

    public LiveData<List<ChatDB>> getAllChats(){
        return allChats;
    }

    public LiveData<ChatDB> getChat(String chatID){
        return chatDBDao.getChat(chatID);
    }

    public LiveData<List<MessageDB>> getAllMessages() {
        return allMessages;
    }

    public LiveData<List<MessageDB>> getChatMessages(String chatID){
        return messageDBDao.getChatMessages(chatID);
    }

    public void insertChat(ChatDB chat){
        new InsertChatAsyncTask(chatDBDao).execute(chat);
    }

    public void insertMessages(MessageDB message){
        new InsertMessagesAsyncTask(messageDBDao).execute(message);
    }

    private static class InsertChatAsyncTask extends AsyncTask<ChatDB, Void, Void>{

        private ChatDBDao chatDBDao;

        public InsertChatAsyncTask(ChatDBDao chatDBDao) {
            this.chatDBDao = chatDBDao;
        }

        @Override
        protected Void doInBackground(ChatDB... chatDBS) {
            chatDBDao.insertChat(chatDBS[0]);
            return null;
        }
    }

    private static class InsertMessagesAsyncTask extends AsyncTask<MessageDB, Void, Void>{

        private MessageDBDao messageDBDao;

        public InsertMessagesAsyncTask(MessageDBDao messageDBDao) {
            this.messageDBDao = messageDBDao;
        }

        @Override
        protected Void doInBackground(MessageDB... messageDBS) {
            messageDBDao.insertMessage(messageDBS[0]);
            return null;
        }
    }

}
