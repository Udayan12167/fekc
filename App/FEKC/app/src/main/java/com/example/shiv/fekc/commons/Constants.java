package com.example.shiv.fekc.commons;


import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by shiv on 10/3/16.
 */
public class Constants {

    public static final String FACEBOOK_PERMISSION_USER_FRIENDS = "user_friends";

    public static final String FACEBOOK_USER_FRIENDS_EDGE = "/me/friends";

    public static final String FACEBOOK_USER_PROFILE_PICTURE_EDGE = "/picture";

    public static final String FACEBOOK_JSON_DATA = "data";

    public static final String FACEBOOK_JSON_URL = "url";

    public static final String STRING_EXTRA_JSON = "extra_json";

    public static final String SLASH = "/";

    public static final String STRING_EXTRA_KEY_GCM = "key";

    public static final String STRING_EXTRA_USER_ID = "user_id";

    public static final String JSON_PARAMETER_USER_ID = "id";

    public static final String JSON_PARAMETER_GCM_TOKEN = "gcmtoken";

    public static final String REGISTRATION_COMPLETE = "registration_complete";

    public static final String NOTIFICATION_MESSAGE = "message";

    public static final Integer ACTIVITY_BASED_TASK = 1;

    public static final Integer SCHEDULE_BASED_TASK = 2;

    public static final Integer TIME_BASED_TASK = 3;

    public static final String SHARED_PREFS = "Shared Prefs";

    //Permission Codes
    public static final Integer MY_PERMISSIONS_REQUEST_STORAGE = 1;

    public static final Integer MY_PERMISSIONS_REQUEST_APP_STATS = 2;

    public static final String APP_NAME = "FEKC";

    public static final String APP_DB_NAME = "FEKCDB.db";

    public static final String JSON_PARAMETER_NAME = "name";

    public static final String JSON_PARAMETER_EMAIL = "email";

    public static final String JSON_PARAMETER_ACCESS_TOKEN = "accesstoken";

    public static final String USER_ACCESS_TOKEN = JSON_PARAMETER_ACCESS_TOKEN;

    public static final String JSON_PARAMETER_FB_TOKEN = "fbtoken";

    public static final String BACKEND_SERVER = "http://192.168.49.240:5000";

    public static final String REGISTER_USER_ENDPOINT = "/users";

    public static final String UPDATE_USER_GCM_ENDPOINT = "/user";

    public static final String ID_PARAMETER = "/{id}";

    public static final String ID = "id";

    private static final ArrayList<String> PERMISSION_LIST = new ArrayList<>();

    private static String ACCESS_TOKEN;

    public static String getAccessToken() {
        return ACCESS_TOKEN;
    }

    public static void setAccessToken(String accessToken) {
        ACCESS_TOKEN = accessToken;
    }

}


