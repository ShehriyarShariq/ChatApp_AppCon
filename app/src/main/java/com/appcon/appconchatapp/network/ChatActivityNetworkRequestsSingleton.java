package com.appcon.appconchatapp.network;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ChatActivityNetworkRequestsSingleton {

    StorageReference firebaseStorage;

    private MutableLiveData<HashMap<String, String>> downloadUrl = new MutableLiveData<>();

    HashMap<String, String> downloadURLMap;

    ChatActivityNetworkRequestsSingleton(){
        downloadUrl.setValue(new HashMap<String, String>());

        downloadURLMap = new HashMap<>();

        firebaseStorage = FirebaseStorage.getInstance().getReference();
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
