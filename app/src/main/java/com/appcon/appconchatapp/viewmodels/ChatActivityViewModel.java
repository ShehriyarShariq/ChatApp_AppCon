package com.appcon.appconchatapp.viewmodels;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.appcon.appconchatapp.model.AudioMessage;
import com.appcon.appconchatapp.model.FileMessage;
import com.appcon.appconchatapp.model.GameMessage;
import com.appcon.appconchatapp.model.ImageMessage;
import com.appcon.appconchatapp.model.Message;
import com.appcon.appconchatapp.model.TextMessage;
import com.appcon.appconchatapp.network.ChatActivityNetworkRequestsSingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivityViewModel extends AndroidViewModel {

    ChatActivityNetworkRequestsSingleton chatActivityNetworkRequestsSingleton = ChatActivityNetworkRequestsSingleton.getInstance();

    private MutableLiveData<HashMap<String, String>> downloadUrl = chatActivityNetworkRequestsSingleton.getDownloadUrl();
    private MutableLiveData<Boolean> startAudioRecord = new MutableLiveData<>();
    private MutableLiveData<String> messageSent = chatActivityNetworkRequestsSingleton.getMessageSent();

    private MutableLiveData<ArrayList<Message>> messages = new MutableLiveData<>();

    DatabaseReference firebaseDatabase;

    String messageID;

    public ChatActivityViewModel(@NonNull Application application) {
        super(application);

        startAudioRecord.setValue(false);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public MutableLiveData<Boolean> getStartAudioRecord() {
        return startAudioRecord;
    }

    public void startAudioRecord(){
        startAudioRecord.postValue(true);
    }

    public void stopAudioRecord(){
        startAudioRecord.postValue(false);
    }

    public MutableLiveData<HashMap<String, String>> getDownloadUrl() {
        return downloadUrl;
    }

    public void setStartAudioRecord(boolean startAudioRecord) {
        this.startAudioRecord.postValue(startAudioRecord);
    }

    public MutableLiveData<String> getMessageSent() {
        return messageSent;
    }

    public void sendMessage(String chatID, Message message){
        HashMap<String, String> msgMap = new HashMap<>();

        if(message instanceof TextMessage){
            TextMessage msg = (TextMessage) message;
            msgMap = msg.getMap();
        } else if(message instanceof AudioMessage){
            AudioMessage msg = (AudioMessage) message;
            msgMap = msg.getMap();
        } else if(message instanceof ImageMessage){
            ImageMessage msg = (ImageMessage) message;
            msgMap = msg.getMap();
        } else if(message instanceof FileMessage){
            FileMessage msg = (FileMessage) message;
            msgMap = msg.getMap();
        } else if(message instanceof GameMessage){
            GameMessage msg = (GameMessage) message;
            msgMap = msg.getMap();
        }

        messageID = firebaseDatabase.push().getKey();
        chatActivityNetworkRequestsSingleton.sendMessage(chatID, messageID, msgMap);
    }

    public void uploadFile(Uri fileUri, String messageID, String type, String uid, String extension){
        chatActivityNetworkRequestsSingleton.uploadFiles(fileUri, messageID, type, uid, extension);
    }
}
