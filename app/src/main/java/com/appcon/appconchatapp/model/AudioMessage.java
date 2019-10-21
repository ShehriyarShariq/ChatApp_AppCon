package com.appcon.appconchatapp.model;

public class AudioMessage extends Message {

    String audioURL;
    String audioLength;
    long size;

    public AudioMessage(String messageID, String senderID, String senderName, String senderPhoneNumber, String time, String audioURL, String audioLength, long size) {
        super(messageID, senderID, senderName, senderPhoneNumber, time);
        this.audioURL = audioURL;
        this.audioLength = audioLength;
        this.size = size;
    }

    public String getAudioURL() {
        return audioURL;
    }

    public String getAudioLength() {
        return audioLength;
    }

    public long getSize() {
        return size;
    }
}
