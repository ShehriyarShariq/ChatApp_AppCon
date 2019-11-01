package com.appcon.appconchatapp.viewmodels;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.appcon.appconchatapp.model.LocalContact;
import com.appcon.appconchatapp.network.MainActivityNetworkRequestsSingleton;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivityViewModel extends AndroidViewModel {

    MainActivityNetworkRequestsSingleton mainActivityNetworkRequestsSingleton = MainActivityNetworkRequestsSingleton.getInstance();

    private MutableLiveData<ArrayList<LocalContact>> allLocalContacts = new MutableLiveData<>();
    private MutableLiveData<ArrayList<HashMap<String, String>>> allValidPhoneNumbers = mainActivityNetworkRequestsSingleton.getAllValidPhoneNumbers();

    Application application;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.application = application;

        setAllLocalContacts(new ArrayList<LocalContact>());
    }

    public void getAllValidContactsRequest(HashMap<String, Object> contacts){
        mainActivityNetworkRequestsSingleton.getAllValidContactsRequest(contacts);
    }

    public MutableLiveData<ArrayList<HashMap<String, String>>> getAllValidPhoneNumbers() {
        return allValidPhoneNumbers;
    }

    public MutableLiveData<ArrayList<LocalContact>> getAllLocalContacts() {
        return allLocalContacts;
    }

    public void setAllLocalContacts(ArrayList<LocalContact> allLocalContacts) {
        this.allLocalContacts.postValue(allLocalContacts);
    }

    public void getAllContactsFromPhone(){
        // Get all stored contacts from phone, which has a stored phone number
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<LocalContact> allLocalContactsList = new ArrayList<>();

                Cursor contactsCursor = null;
                ContentResolver contentResolver = application.getContentResolver();

                try{
                    contactsCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                } catch (Exception e){
                    e.printStackTrace();
                }

                if(contactsCursor.getCount() > 0){
                    while(contactsCursor.moveToNext()){
                        String id = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID));
                        String displayName = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        int hasPhoneNumber = Integer.parseInt(contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                        if(hasPhoneNumber > 0){
                            String singleValidPhoneNum = null;
                            Cursor phoneCursor = contentResolver.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id},
                                    null);

                            while(phoneCursor.moveToNext()){
                                String phoneNum = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "");
                                if(phoneNum.contains("+92")){
                                    singleValidPhoneNum = phoneNum;
                                    break;
                                }
                            }
                            phoneCursor.close();

                            if(singleValidPhoneNum != null){
                                allLocalContactsList.add(new LocalContact(displayName, singleValidPhoneNum));
                            }
                        }
                    }

                    contactsCursor.close();
                }

                setAllLocalContacts(allLocalContactsList);
            }
        }).start();
    }
}
