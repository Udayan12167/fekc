package com.example.shiv.fekc.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.adapter.AppAdapter;
import com.example.shiv.fekc.adapter.DBAdapter;
import com.example.shiv.fekc.adapter.UserAdapter;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.item.TaskItem;
import com.example.shiv.fekc.item.User;
import com.example.shiv.fekc.rest.response.TaskCreateResponse;
import com.example.shiv.fekc.rest.service.BackendAPIServiceClient;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ScheduledBasedTaskReviewActivity extends AppCompatActivity {


    private TextView taskNameTextView;
    private TextView startTimeTextView;
    private TextView endTimeTextView;
    private TextView endDateTextView;

    private Button createTaskButton;

    private ProgressBar progressBar;

    private TaskItem task;
    private Gson gson;

    private RecyclerView appListRecyclerView;
    private RecyclerView userListRecyclerView;

    private AppAdapter appAdapter;
    private UserAdapter userAdapter;
    private Window window;

    private SharedPreferences sharedPreferences;
    private DBAdapter dbAdapter;
    private BackendAPIServiceClient backendAPIServiceClient;

    private ImageView closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_schedule_based);
        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.reviewStatus));

        closeButton = (ImageView) findViewById(R.id.review_close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        gson = new Gson();
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
        dbAdapter = new DBAdapter();
        backendAPIServiceClient = new BackendAPIServiceClient();

        taskNameTextView = (TextView)findViewById(R.id.activity_schedule_based_task_task_name_text_view);
        startTimeTextView = (TextView)findViewById(R.id.activity_schedule_based_task_start_time_text_view);
        endTimeTextView = (TextView)findViewById(R.id.activity_schedule_based_task_end_time_text_view);
        endDateTextView = (TextView)findViewById(R.id.activity_schedule_based_task_end_date_text_view);

        createTaskButton = (Button)findViewById(R.id.activity_schedule_based_task_upload_button);

        progressBar = (ProgressBar)findViewById(R.id.activity_schedule_based_task_progress_bar);
        progressBar.setVisibility(View.GONE);

        task = gson.fromJson(getIntent().getExtras().getString(Constants.STRING_EXTRA_JSON), TaskItem.class);

        taskNameTextView.setText(task.getTaskName());
        startTimeTextView.setText(task.getStartTime());
        endTimeTextView.setText(task.getEndTime());
        endDateTextView.setText(task.getEndDate());

        appAdapter = new AppAdapter(this, task.getApps());
        userAdapter = new UserAdapter(this, new ArrayList<String>());

        appListRecyclerView = (RecyclerView)findViewById(R.id.activity_schedule_based_task_review_app_recycler_view);
        userListRecyclerView = (RecyclerView)findViewById(R.id.activity_schedule_based_task_review_user_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        appListRecyclerView.setLayoutManager(linearLayoutManager);
        appListRecyclerView.setAdapter(appAdapter);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);

        userListRecyclerView.setLayoutManager(linearLayoutManager1);
        userListRecyclerView.setAdapter(userAdapter);

        getFriendDPs();

        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTaskButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                uploadTaskOnServer();
            }
        });
    }

    private void getFriendDPs(){
        for(String string : task.getFriends()){
            getUserDPUrl(string);
        }

    }

    private void getUserDPUrl(final String facebookId) {
        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                Constants.SLASH + facebookId + Constants.FACEBOOK_USER_PROFILE_PICTURE_EDGE,
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.getJSONObject().getString(Constants.FACEBOOK_JSON_DATA));
                            String url = jsonObject.getString(Constants.FACEBOOK_JSON_URL);
                            userAdapter.add(url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

        ).executeAsync();
    }

    private void uploadTaskOnServer(){
        Gson gson = new Gson();
        HashMap<String, String> parameters = new HashMap<>();
        String user_id = sharedPreferences.getString(Constants.USER_ACCESS_TOKEN, "");
        Log.d(getClass().toString(), "The user id  is : " + user_id);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        parameters.put(Constants.JSON_PARAMETER_TASK,gson.toJson(task));
        parameters.put(Constants.JSON_PARAMETER_USER_ID, user_id);
        parameters.put(Constants.JSON_PARAMETER_FB_TOKEN, accessToken.getToken());
        backendAPIServiceClient.getService().createTask(parameters, new Callback<TaskCreateResponse>() {
            @Override
            public void success(TaskCreateResponse taskCreateResponse, Response response) {
                Log.d(getClass().toString(), "Task uploaded with id " + taskCreateResponse.getTid());
                task.setTaskServerId(taskCreateResponse.getTid());
                dbAdapter.insertIntoTaskInfo(task);
                createTaskButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Task Added!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ScheduledBasedTaskReviewActivity.this, NavActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(getClass().toString(), "Unable to create task");
                error.printStackTrace();
            }
        });
    }
}
