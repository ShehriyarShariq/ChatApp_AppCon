package com.appcon.appconchatapp.model;

public class AudioMessage extends Message {

    String audioURL;
    int audioLength;
    int size;
    boolean downloaded;

    public AudioMessage(String messageID, String senderID, String senderName, String senderPhoneNumber, String time, String audioURL, int audioLength, int size, boolean downloaded) {
        super(messageID, senderID, senderName, senderPhoneNumber, time);
        this.audioURL = audioURL;
        this.audioLength = audioLength;
        this.size = size;
        this.downloaded = downloaded;
    }

    public String getAudioURL() {
        return audioURL;
    }

    public int getAudioLength() {
        return audioLength;
    }

    public int getSize() {
        return size;
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }
}
