package com.appcon.appconchatapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.appcon.appconchatapp.model.Chat;
import com.appcon.appconchatapp.model.MessageDB;
import com.appcon.appconchatapp.network.ChatMessagesNetworkRequest;
import com.appcon.appconchatapp.network.ChatsFragmentNetworkRequestSingleton;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatsFragmentViewModel extends AndroidViewModel {

    ChatsFragmentNetworkRequestSingleton chatsFragmentNetworkRequestSingleton = ChatsFragmentNetworkRequestSingleton.getInstance();
    ChatMessagesNetworkRequest chatMessagesNetworkRequest = ChatMessagesNetworkRequest.getInstance();

    private MutableLiveData<ArrayList<String>> allChats = chatsFragmentNetworkRequestSingleton.getAllChats();
    private MutableLiveData<MessageDB> message = chatMessagesNetworkRequest.getMessage();
    private MutableLiveData<Chat> chat = chatMessagesNetworkRequest.getChat();

    public ChatsFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public void getAllConversations(){
        chatsFragmentNetworkRequestSingleton.getAllConversationsRequest();
    }

    public MutableLiveData<MessageDB> getMessage() {
        return message;
    }

    public MutableLiveData<Chat> getChat() {
        return chat;
    }

    public void addChatsListener(String chatID){
        chatMessagesNetworkRequest.addChatsListener(chatID);
    }

    public void addConversationListener(String chatID){
        chatMessagesNetworkRequest.addConversationListener(chatID);
    }

    public MutableLiveData<ArrayList<String>> getAllChats() {
        return allChats;
    }
}
