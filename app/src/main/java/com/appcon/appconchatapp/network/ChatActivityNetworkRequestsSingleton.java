package com.appcon.appconchatapp.network;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ChatActivityNetworkRequestsSingleton {

    StorageReference firebaseStorage;

    private MutableLiveData<String> downloadUrl = new MutableLiveData<>();

    ChatActivityNetworkRequestsSingleton(){
        downloadUrl.setValue("none");

        firebaseStorage = FirebaseStorage.getInstance().getReference();
    }

    private static ChatActivityNetworkRequestsSingleton holder = new ChatActivityNetworkRequestsSingleton();
    public static ChatActivityNetworkRequestsSingleton getInstance(){
        return holder;
    }

    public MutableLiveData<String> getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl.postValue(downloadUrl);
    }

    public void uploadFiles(Uri fileUri, String type, String uid, String extension){
        StorageReference ref = firebaseStorage.child(type + "/" + uid + "_" + System.currentTimeMillis() + extension);
        ref.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> resultUri = taskSnapshot.getStorage().getDownloadUrl();
                while (!resultUri.isComplete());
                setDownloadUrl(resultUri.getResult().toString());
            }
        });
    }

}
