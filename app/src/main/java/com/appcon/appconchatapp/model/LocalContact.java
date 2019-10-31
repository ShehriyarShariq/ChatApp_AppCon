package com.appcon.appconchatapp.model;

import java.util.ArrayList;

public class LocalContact {

    private String displayName, phoneNumber;

    public LocalContact(String displayName, String phoneNumber) {
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
