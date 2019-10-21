package com.appcon.appconchatapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.appcon.appconchatapp.model.Message;

import java.util.ArrayList;

public class ChatActivityViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> startAudioRecord = new MutableLiveData<>();

    private MutableLiveData<ArrayList<Message>> messages = new MutableLiveData<>();

    public ChatActivityViewModel(@NonNull Application application) {
        super(application);

        startAudioRecord.postValue(false);
    }

    public void sendMessage(Message message){

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
}
