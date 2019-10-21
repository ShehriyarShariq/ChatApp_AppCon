package com.appcon.appconchatapp.utils;

import android.os.Looper;

import androidx.lifecycle.MutableLiveData;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.Calendar;

public class CurrentTimeSingleton {

    private Socket mSocket;

    private MutableLiveData<String> currentTime = new MutableLiveData<>();
    private MutableLiveData<String> currentDate = new MutableLiveData<>();

    public MutableLiveData<String> getCurrentTime(){
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime.postValue(currentTime);
    }

    public MutableLiveData<String> getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate.postValue(currentDate);
    }

    public CurrentTimeSingleton(){
        currentTime.postValue("None");
        currentDate.postValue("None");
    }

    private static CurrentTimeSingleton holder = new CurrentTimeSingleton();
    public static CurrentTimeSingleton getInstance(){
        return holder;
    }

    public void startCurrentTimeUpdates(){
        try {
            mSocket = IO.socket("https://bookies14.herokuapp.com/");
        } catch (URISyntaxException e) { }

        mSocket.connect();
        mSocket.on("dateAndTime", getTrueDateTime);
    }

    private Emitter.Listener getTrueDateTime = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (args != null) {
                        mSocket.disconnect();
                        mSocket.close();

                        long timeInMs = Long.parseLong(args[0].toString());

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(timeInMs);

                        startCurrentTimeTimer(calendar);
                    }
                }
            }).start();
        }
    };

    private void startCurrentTimeTimer(final Calendar calendar){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                while(true){

                    setCurrentTime(getCurrentTime(calendar));
                    setCurrentDate(getCurrentDate(calendar));
                    calendar.add(Calendar.SECOND, 1);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private String getCurrentTime(Calendar calendar){
        String hours = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)).length() < 2 ? "0" + calendar.get(Calendar.HOUR_OF_DAY) : String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(calendar.get(Calendar.MINUTE)).length() < 2 ? "0" + calendar.get(Calendar.MINUTE) : String.valueOf(calendar.get(Calendar.MINUTE));
        return hours + ":" + mins;
    }

    private String getCurrentDate(Calendar calendar){
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1).length() < 2 ? "0" + (calendar.get(Calendar.MONTH) + 1) : String.valueOf((calendar.get(Calendar.MONTH) + 1));
        String date = String.valueOf(calendar.get(Calendar.DATE)).length() < 2 ? "0" + calendar.get(Calendar.DATE) : String.valueOf(calendar.get(Calendar.DATE));
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        return date + "-" + month + "-" + year;
    }

}
