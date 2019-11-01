package com.appcon.appconchatapp.utils;

public final class CONSTANTS {

    // SERVER BASE URL
    public static final String SERVER_BASE_URL = "https://us-central1-connectme-7d022.cloudfunctions.net/";
    public static final String AUTH_URL = SERVER_BASE_URL + "auth/";
    public static final String USER_READ_URL = SERVER_BASE_URL + "userRead/";
    public static final String USER_WRITE_URL = SERVER_BASE_URL + "userWrite/";

    // LOADER TYPES
    public static final String INFO_LOADER = "InfoLoader";
    public static final String PROCESS_LOADER = "Process";
    public static final String SEND_LOADER = "Send";
}
