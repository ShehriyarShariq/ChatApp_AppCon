package com.appcon.appconchatapp.viewmodels;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.appcon.appconchatapp.model.Message;
import com.appcon.appconchatapp.network.ChatActivityNetworkRequestsSingleton;

import java.util.ArrayList;

public class ChatActivityViewModel extends AndroidViewModel {

    ChatActivityNetworkRequestsSingleton chatActivityNetworkRequestsSingleton = ChatActivityNetworkRequestsSingleton.getInstance();

    private MutableLiveData<String> downloadUrl = chatActivityNetworkRequestsSingleton.getDownloadUrl();
    private MutableLiveData<Boolean> startAudioRecord = new MutableLiveData<>();

    private MutableLiveData<ArrayList<Message>> messages = new MutableLiveData<>();

    public ChatActivityViewModel(@NonNull Application application) {
        super(application);

        startAudioRecord.postValue(false);
    }

    public void sendMessage(Message message){

    }

    public void uploadFile(Uri fileUri, String type, String uid, String extension){
        chatActivityNetworkRequestsSingleton.uploadFiles(fileUri, type, uid, extension);
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

    public MutableLiveData<String> getDownloadUrl() {
        return downloadUrl;
    }
}
