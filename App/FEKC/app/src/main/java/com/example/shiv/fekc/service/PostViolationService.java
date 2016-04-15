package com.example.shiv.fekc.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Window;

import com.example.shiv.fekc.adapter.DBAdapter;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.item.TaskItem;
import com.example.shiv.fekc.item.ViolationItem;
import com.example.shiv.fekc.rest.response.ViolationObjectResponse;
import com.example.shiv.fekc.rest.service.BackendAPIServiceClient;
import com.facebook.AccessToken;
import com.google.gson.Gson;

import java.sql.Date;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class PostViolationService extends IntentService {

    private BackendAPIServiceClient backendAPIServiceClient;
    private SharedPreferences sharedPreferences;
    private DBAdapter dbAdapter ;

    private Gson gson;

    private TaskItem taskItem;

    public PostViolationService(){
        super("PostViolationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        backendAPIServiceClient = new BackendAPIServiceClient();
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        dbAdapter = new DBAdapter();
        gson = new Gson();
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            taskItem = gson.fromJson(intent.getExtras().getString(Constants.STRING_EXTRA_JSON), TaskItem.class);
            if(intent.getExtras().getInt(Constants.STRING_EXTRA_VIOLATION_CODE) == Constants.VIOLATION_CODE){
                postViolation();
            }else{
                postWin();
            }
        }
    }

    private void postViolation(){
        ViolationItem violationItem = new ViolationItem();
        violationItem.setTaskID(taskItem.getTaskID());
        violationItem.setViolationType(Constants.VIOLATION_CODE);
        violationItem.setDate(new Date(System.currentTimeMillis()));
        dbAdapter.insertIntoTaskViolation(violationItem);

        String taskId = taskItem.getTaskServerId();
        String user_id = sharedPreferences.getString(Constants.USER_ACCESS_TOKEN, "");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(Constants.JSON_PARAMETER_USER_ID, user_id);
        parameters.put(Constants.JSON_PARAMETER_TASK_ID, taskId);
        parameters.put(Constants.JSON_PARAMETER_FB_TOKEN, AccessToken.getCurrentAccessToken().getToken());

        backendAPIServiceClient.getService().postViolation(parameters, new Callback<ViolationObjectResponse>() {
            @Override
            public void success(ViolationObjectResponse violationObjectResponse, Response response) {
                Log.d(getClass().toString(), "Successfully created violation");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(getClass().toString(), "Failed to create violation");
            }
        });
    }

    private void postWin(){
        ViolationItem violationItem = new ViolationItem();
        violationItem.setTaskID(taskItem.getTaskID());
        violationItem.setViolationType(Constants.WIN_CODE);
        violationItem.setDate(new Date(System.currentTimeMillis()));
        dbAdapter.insertIntoTaskViolation(violationItem);

        String taskId = taskItem.getTaskServerId();
        String user_id = sharedPreferences.getString(Constants.USER_ACCESS_TOKEN, "");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(Constants.JSON_PARAMETER_USER_ID, user_id);
        parameters.put(Constants.JSON_PARAMETER_TASK_ID, taskId);
        parameters.put(Constants.JSON_PARAMETER_FB_TOKEN, AccessToken.getCurrentAccessToken().getToken());

        backendAPIServiceClient.getService().postWinWin(parameters, new Callback<ViolationObjectResponse>() {
            @Override
            public void success(ViolationObjectResponse violationObjectResponse, Response response) {
                Log.d(getClass().toString(), "Successfully created win");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(getClass().toString(), "Failed to create win");
            }
        });
    }

}
