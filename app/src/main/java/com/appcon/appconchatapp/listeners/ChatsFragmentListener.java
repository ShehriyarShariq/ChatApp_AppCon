package com.appcon.appconchatapp.listeners;

public interface ChatsFragmentListener {

    void OnChatLongPress(int pos);

    void OnClearSelectionBtnPress();

    void updateSelected();
}
