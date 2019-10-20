package com.appcon.appconchatapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class Result implements Parcelable {

    String resultID;
    HashMap<String, Object> result;

    public Result(String resultID, HashMap<String, Object> result) {
        this.resultID = resultID;
        this.result = result;
    }

    protected Result(Parcel in) {
        resultID = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(resultID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isResultSuccessful(){
        return resultID.equals("202");
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public Object getMessage(){
        return result.get("message");
    }
}
