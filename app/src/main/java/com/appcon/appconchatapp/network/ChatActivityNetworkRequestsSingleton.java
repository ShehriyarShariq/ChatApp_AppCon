package com.appcon.appconchatapp.network;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.appcon.appconchatapp.model.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ChatActivityNetworkRequestsSingleton {

    StorageReference firebaseStorage;
    DatabaseReference firebaseDatabase;

    private MutableLiveData<HashMap<String, String>> downloadUrl = new MutableLiveData<>();
    private MutableLiveData<String> messageSent = new MutableLiveData<>();

    HashMap<String, String> downloadURLMap;

    ChatActivityNetworkRequestsSingleton(){
        downloadUrl.setValue(new HashMap<String, String>());

        setDownloadUrl(new HashMap<String, String>());
        setMessageSent("none");

        downloadURLMap = new HashMap<>();

        firebaseStorage = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private static ChatActivityNetworkRequestsSingleton holder = new ChatActivityNetworkRequestsSingleton();
    public static ChatActivityNetworkRequestsSingleton getInstance(){
        return holder;
    }

    public MutableLiveData<HashMap<String, String>> getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(HashMap<String, String> downloadUrl) {
        this.downloadUrl.postValue(downloadUrl);
    }

    public MutableLiveData<String> getMessageSent() {
        return messageSent;
    }

    public void setMessageSent(String messageSent) {
        this.messageSent.postValue(messageSent);
    }

    public void sendMessage(String chatID, final String messageID, HashMap<String, String> message){
        firebaseDatabase.child("messages").child(chatID).child(messageID).setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    setMessageSent(messageID);
                } else {
                    setMessageSent("failed");
                }
            }
        });
    }

    public void uploadFiles(Uri fileUri, final String messageID, String type, String uid, String extension){
        StorageReference ref = firebaseStorage.child(type + "/" + uid + "_" + System.currentTimeMillis() + extension);
        ref.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> resultUri = taskSnapshot.getStorage().getDownloadUrl();
                while (!resultUri.isComplete());

                downloadURLMap.put(messageID, resultUri.getResult().toString());
                setDownloadUrl(downloadURLMap);
            }
        });
    }

}
