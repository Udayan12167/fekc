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

    public static final String APP_PACKAGE = "com.example.shiv.fekc";

    public static final String FACEBOOK_PERMISSION_USER_FRIENDS = "user_friends";

    public static final String FACEBOOK_USER_FRIENDS_EDGE = "/me/friends";

    public static final String FACEBOOK_USER_PROFILE_PICTURE_EDGE = "/picture";

    public static final String FACEBOOK_JSON_DATA = "data";

    public static final String FACEBOOK_JSON_URL = "url";

    public static final String FACEBOOK_JSON_NAME = "name";

    public static final String STRING_EXTRA_JSON = "extra_json";

    public static final String SLASH = "/";

    public static final String STRING_EXTRA_KEY_GCM = "key";

    public static final String STRING_EXTRA_USER_ID = "user_id";

    public static final String STRING_EXTRA_SELECTED_APPS = "SelectedApps";

    public static final String STRING_EXTRA_SELECTED_USERS = "SelectedUsers";

    public static final String STRING_EXTRA_TASK_SERVER_ID = "task_server_id";

    public static final String JSON_PARAMETER_FACEBOOK_ID = "fbid";

    public static final String JSON_PARAMETER_USER_ID = "user_id";

    public static final String JSON_PARAMETER_TASK_ID = "task_id";

    public static final String JSON_PARAMETER_TASK = "task";

    public static final String JSON_PARAMETER_GCM_TOKEN = "gcmtoken";

    public static final String REGISTRATION_COMPLETE = "registration_complete";

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

    public static final String JSON_PARAMETER_MESSAGE = "message";

    public static final String BACKEND_SERVER = "http://ec2-52-35-206-73.us-west-2.compute.amazonaws.com:5000";

    public static final String REGISTER_USER_ENDPOINT = "/users";

    public static final String UPDATE_USER_GCM_ENDPOINT = "/user";

    public static final String CREATE_TASK_ENDPOINT = "/tasks";

    public static final String DELETE_TASK_ENDPOINT = "/task";

    public static final String GET_TRACKED_TASK_ENDPOINT = "/tracked_tasks";

    public static final String PUT_MESSAGE_ENDPOINT = "/message";

    public static final String GET_MESSAGES_ENDPOINT = "/messages";

    public static final String POST_VIOLATION_ENDPOINT = "/violation";

    public static final String POST_WIN_ENDPOINT = "/win";

    public static final String ID_PARAMETER = "/{id}";

    public static final String ID = "id";

    public static final int CODE_NEW_USER = 1;

    public static final int CODE_EXISTING_USER = 0;

    public static final int CODE_MESSAGE_SET = 1;

    //Notifications
    public static final String NOTIFICATION_TITLE = "FEKC";
    public static final String NOTIFICATION_MESSAGE = "Your friend created a task. Help their quest!";
    public static final String VIOLATION_MESSAGE = "Someone needs your attention!";
    public static final String NOTIFICATION_FRIEND_FBID = "gcm.notification.friendfbid";
    public static final String NOTIFICATION_INTENT_IDENTIFIER = "Notif";

    public static final int VIOLATION_CODE = 1;

    public static final int WIN_CODE = 2;

    public static final String DATE_FORMAT = "yyyy-MM-dd";



}


