package com.appcon.appconchatapp.listeners;

import com.appcon.appconchatapp.model.AudioMessage;
import com.appcon.appconchatapp.model.FileMessage;

public interface ChatMessagesItemListener {

    void OnDocumentClicked(FileMessage fileMessage);

    void OnAudioPlayPause(AudioMessage audioMessage);

}
