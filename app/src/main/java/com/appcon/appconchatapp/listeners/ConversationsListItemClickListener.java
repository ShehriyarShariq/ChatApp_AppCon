package com.appcon.appconchatapp.listeners;

public interface ConversationsListItemClickListener {

    void OnConversationClicked(int pos);

    void OnLongPress(int pos);

    void updateSelected();

    void OnChatVideoBtnClicked(int pos);

    void OnChatAudioBtnClicked(int pos);

    void OnChatMuteBtnClicked(int pos);

    void OnChatDeleteBtnClicked(int pos);

}
