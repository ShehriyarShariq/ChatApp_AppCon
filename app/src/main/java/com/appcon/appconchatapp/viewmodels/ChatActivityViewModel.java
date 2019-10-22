package com.appcon.appconchatapp.viewmodels;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.appcon.appconchatapp.model.AudioMessage;
import com.appcon.appconchatapp.model.ImageMessage;
import com.appcon.appconchatapp.model.Message;
import com.appcon.appconchatapp.model.TextMessage;
import com.appcon.appconchatapp.network.ChatActivityNetworkRequestsSingleton;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivityViewModel extends AndroidViewModel {

    ChatActivityNetworkRequestsSingleton chatActivityNetworkRequestsSingleton = ChatActivityNetworkRequestsSingleton.getInstance();

    private MutableLiveData<HashMap<String, String>> downloadUrl = chatActivityNetworkRequestsSingleton.getDownloadUrl();
    private MutableLiveData<Boolean> startAudioRecord = new MutableLiveData<>();

    private MutableLiveData<ArrayList<Message>> messages = new MutableLiveData<>();

    public ChatActivityViewModel(@NonNull Application application) {
        super(application);

        startAudioRecord.postValue(false);
    }

    public void sendMessage(Message message){
        if(message instanceof TextMessage){

        } else if(message instanceof AudioMessage){

        } else if(message instanceof ImageMessage){

        } else
    }

    public void uploadFile(Uri fileUri, String messageID, String type, String uid, String extension){
        chatActivityNetworkRequestsSingleton.uploadFiles(fileUri, messageID, type, uid, extension);
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
}
