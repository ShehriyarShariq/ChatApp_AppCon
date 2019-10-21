package com.appcon.appconchatapp.viewmodels;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.appcon.appconchatapp.model.Conversation;

public class ConversationViewModel extends BaseObservable {

    private Conversation mConversation;

    public ConversationViewModel() {
    }

    @Bindable
    public String getDisplayName() {
        return mConversation.getDisplayName();
    }

    @Bindable
    public String getLastMessage() {
        return mConversation.getLastMessage();
    }

    @Bindable
    public String getTime() {
        return mConversation.getTime();
    }

    public void setConversation(Conversation conversation) {
        mConversation = conversation;
        notifyChange();
    }
}
