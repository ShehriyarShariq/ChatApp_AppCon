package com.appcon.appconchatapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.appcon.appconchatapp.model.Message;

import java.util.ArrayList;

public class ChatActivityViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Message>> messages = new MutableLiveData<>();

    public ChatActivityViewModel(@NonNull Application application) {
        super(application);
    }

    public void sendMessage(Message message){

    }
}
