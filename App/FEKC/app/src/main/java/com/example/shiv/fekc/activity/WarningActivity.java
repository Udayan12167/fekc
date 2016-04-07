package com.example.shiv.fekc.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.adapter.DBAdapter;
import com.example.shiv.fekc.adapter.WarningMessageAdapter;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.commons.Functions;
import com.example.shiv.fekc.item.TaskItem;
import com.example.shiv.fekc.item.ViolationItem;
import com.example.shiv.fekc.item.WarningMessageItem;
import com.example.shiv.fekc.rest.response.TaskMessage;
import com.example.shiv.fekc.rest.response.TaskMessageResponse;
import com.example.shiv.fekc.rest.response.ViolationObjectResponse;
import com.example.shiv.fekc.rest.service.BackendAPIServiceClient;
import com.example.shiv.fekc.service.CheckViolationService;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WarningActivity extends AppCompatActivity {

    private Button goToAppButton;
    private Button stopAppButton;
    private RecyclerView recyclerView;
    private ImageView imageView;

    private ProgressBar progressBar;

    private WarningMessageAdapter warningMessageAdapter;

    private BackendAPIServiceClient backendAPIServiceClient;
    private SharedPreferences sharedPreferences;

    private DBAdapter dbAdapter ;

    private TaskItem taskItem;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Functions.facebookSDKInitialize(getApplicationContext());
        setContentView(R.layout.activity_warning);
        Log.d(getClass().toString(), "Opened warning activity");

        backendAPIServiceClient = new BackendAPIServiceClient();
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        warningMessageAdapter = new WarningMessageAdapter(this, new ArrayList<WarningMessageItem>());

        dbAdapter = new DBAdapter();
        gson = new Gson();

        taskItem = gson.fromJson(getIntent().getExtras().getString(Constants.STRING_EXTRA_JSON), TaskItem.class);

        progressBar = (ProgressBar) findViewById(R.id.warning_activity_progressBar);
        progressBar.setVisibility(View.GONE);

        imageView = (ImageView)findViewById(R.id.activity_warning_image_view);
        imageView.setVisibility(View.GONE);

        goToAppButton = (Button) findViewById(R.id.activity_warning_go_to_app_button);
        stopAppButton = (Button) findViewById(R.id.activity_warning_stop_app_button);

        stopAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopApp();
            }
        });

        goToAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToApp();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = (RecyclerView) findViewById(R.id.acrivity_warning_recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(warningMessageAdapter);

        getWarningMessages();
    }

    private void stopApp() {
        ViolationItem violationItem = new ViolationItem();
        violationItem.setTaskID(taskItem.getTaskID());
        violationItem.setViolationType(Constants.WIN_CODE);
        violationItem.setDate(new Date(System.currentTimeMillis()));
        dbAdapter.insertIntoTaskViolation(violationItem);

        postWin();

        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
    }

    private void goToApp() {
        ViolationItem violationItem = new ViolationItem();
        violationItem.setTaskID(taskItem.getTaskID());
        violationItem.setViolationType(Constants.VIOLATION_CODE);
        violationItem.setDate(new Date(System.currentTimeMillis()));
        dbAdapter.insertIntoTaskViolation(violationItem);

        postViolation();

        CheckViolationService.setGoToButtonForPackage(CheckViolationService.getViolatedPackage());
        Log.e("FOREGROUND", CheckViolationService.getViolatedPackage());
        Intent startPackage = getPackageManager().getLaunchIntentForPackage(CheckViolationService.getViolatedPackage());
        startActivity(startPackage);
        finish();
    }

    private void getWarningMessages() {
        progressBar.setVisibility(View.VISIBLE);
        String taskId = getIntent().getExtras().getString(Constants.STRING_EXTRA_TASK_SERVER_ID);
        final String id = sharedPreferences.getString(Constants.USER_ACCESS_TOKEN, "");
        HashMap<String, String> parameters = new HashMap<>();
        Log.d(getClass().toString(), "The taskId is : " + taskId + " and user id : " + id);
        parameters.put(Constants.JSON_PARAMETER_TASK_ID, taskId);
        parameters.put(Constants.JSON_PARAMETER_FB_TOKEN, AccessToken.getCurrentAccessToken().getToken());

        backendAPIServiceClient.getService().getUserTaskMessages(id, parameters, new Callback<TaskMessageResponse>() {
            @Override
            public void success(TaskMessageResponse taskMessageResponse, Response response) {
                progressBar.setVisibility(View.GONE);
                if (taskMessageResponse.getMessages() == null) {
                    return;
                } else {
                    for (TaskMessage taskMessage : taskMessageResponse.getMessages()) {
                        WarningMessageItem warningMessageItem = new WarningMessageItem(taskMessage);
                        getUserDPUrl(warningMessageItem);
                    }
                }
                Log.d(getClass().toString(), "Successfully got messages");
            }

            @Override
            public void failure(RetrofitError error) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getUserDPUrl(final WarningMessageItem warningMessageItem) {
        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                Constants.SLASH + warningMessageItem.getFriendId() + Constants.FACEBOOK_USER_PROFILE_PICTURE_EDGE,
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.getJSONObject().getString(Constants.FACEBOOK_JSON_DATA));
                            String imageurl = jsonObject.getString(Constants.FACEBOOK_JSON_URL);
                            warningMessageItem.setImageUrl(imageurl);
                            getUserName(warningMessageItem);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

        ).executeAsync();
    }

    private void getUserName(final WarningMessageItem warningMessageItem) {
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                Constants.SLASH + warningMessageItem.getFriendId(),
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
//                            Log.d(getClass().toString() , response.getJSONObject().toString());
                            JSONObject jsonObject = response.getJSONObject();
                            Log.d(getClass().toString(), jsonObject.toString());
                            String name = jsonObject.getString(Constants.FACEBOOK_JSON_NAME);
                            Log.d(getClass().toString(), name);
                            warningMessageItem.setName(name);
                            warningMessageAdapter.add(warningMessageItem);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        request.executeAsync();
    }

    private void postViolation(){
        String taskId = getIntent().getExtras().getString(Constants.STRING_EXTRA_TASK_SERVER_ID);
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
        String taskId = getIntent().getExtras().getString(Constants.STRING_EXTRA_TASK_SERVER_ID);
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
