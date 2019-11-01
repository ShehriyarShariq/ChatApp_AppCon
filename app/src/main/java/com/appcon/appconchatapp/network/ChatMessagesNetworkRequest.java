package com.appcon.appconchatapp.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.appcon.appconchatapp.model.Chat;
import com.appcon.appconchatapp.model.Message;
import com.appcon.appconchatapp.model.MessageDB;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatMessagesNetworkRequest {

    DatabaseReference firebaseDatabase;
    FirebaseAuth firebaseAuth;

    private MutableLiveData<MessageDB> message = new MutableLiveData<>();
    private MutableLiveData<Chat> chat = new MutableLiveData<>();

    public ChatMessagesNetworkRequest() {
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private static ChatMessagesNetworkRequest holder = new ChatMessagesNetworkRequest();
    public static ChatMessagesNetworkRequest getInstance(){return holder;}

    public MutableLiveData<MessageDB> getMessage() {
        return message;
    }

    public MutableLiveData<Chat> getChat() {
        return chat;
    }

    private void setMessage(MessageDB message) {
        this.message.postValue(message);
    }

    private void setChat(Chat chat) {
        this.chat.postValue(chat);
    }

    public void addChatsListener(String chatID){
        firebaseDatabase.child("conversations").child(chatID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String chatID = dataSnapshot.getKey();
                HashMap<String, Object> map = (HashMap<String, Object>) dataSnapshot.getValue();

                Chat chat = new Chat(
                        chatID,
                        map.get("displayName").toString(),
                        map.get("displayPicture").toString(),
                        map.get("creationDate").toString(),
                        map.get("description").toString(),
                        "messageID",
                        "none",
                        "none",
                        "none",
                        false,
                        false,
                        (HashMap<String, String>) map.get("permissions"),
                        (ArrayList<String>) map.get("admins"),
                        (ArrayList<String>) map.get("members")
                );

                setChat(chat);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addConversationListener(String chatID){
        firebaseDatabase.child("messages").child(chatID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String messageID = dataSnapshot.getKey();
                if(!messageID.equals("messageID")){
                    HashMap<String, String> msgMap = (HashMap<String, String>) dataSnapshot.getValue();
                    String chatID = dataSnapshot.getRef().getParent().getKey();
                    msgMap.put("chatID", chatID);
                    msgMap.put("messageID", dataSnapshot.getKey());

                    MessageDB message = new MessageDB(
                            dataSnapshot.getKey(),
                            chatID,
                            msgMap.get("sentBy"),
                            msgMap.get("type"),
                            msgMap.get("content"),
                            msgMap.get("timeStamp"));

                    setMessage(message);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
