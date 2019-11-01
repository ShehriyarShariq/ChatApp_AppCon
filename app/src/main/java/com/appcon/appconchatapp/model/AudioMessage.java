package com.appcon.appconchatapp.model;

import java.util.HashMap;
import java.util.HashSet;

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

    public void setAudioURL(String audioURL) {
        this.audioURL = audioURL;
    }

    public HashMap<String, String> getMap(){
        HashMap<String, String> map = new HashMap<>();

        map.put("content", audioLength + "_" + size + "_" + audioURL);
        map.put("sentBy", super.senderID);
        map.put("timeStamp", super.time);
        map.put("type", "audio");

        return map;
    }
}
