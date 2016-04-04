package com.example.shiv.fekc.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.adapter.AppAdapter;
import com.example.shiv.fekc.adapter.UserAdapter;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.item.TaskItem;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TimeBasedTaskReviewActivity extends AppCompatActivity {

    private TextView taskNameTextView;
    private TextView durationTextView;
    private TextView endDateTextView;

    private TaskItem task;
    private Gson gson;

    private RecyclerView appListRecyclerView;
    private RecyclerView userListRecyclerView;

    private AppAdapter appAdapter;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_based_task_review);

        gson = new Gson();

        taskNameTextView = (TextView)findViewById(R.id.activity_duration_based_task_task_name_text_view);
        durationTextView = (TextView)findViewById(R.id.activity_duration_based_task_duration_text_view);
        endDateTextView = (TextView)findViewById(R.id.activity_duration_based_task_end_date_text_view);


        task = gson.fromJson(getIntent().getExtras().getString(Constants.STRING_EXTRA_JSON), TaskItem.class);

        taskNameTextView.setText(task.getTaskName());
        endDateTextView.setText(task.getEndDate());
        durationTextView.setText(task.getDuration());

        appAdapter = new AppAdapter(this, task.getApps());
        userAdapter = new UserAdapter(this, new ArrayList<String>());

        appListRecyclerView = (RecyclerView)findViewById(R.id.activity_duration_based_task_review_app_recycler_view);
        userListRecyclerView = (RecyclerView)findViewById(R.id.activity_duration_based_task_review_user_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        appListRecyclerView.setLayoutManager(linearLayoutManager);
        appListRecyclerView.setAdapter(appAdapter);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);

        userListRecyclerView.setLayoutManager(linearLayoutManager1);
        userListRecyclerView.setAdapter(userAdapter);

        getFriendDPs();
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
}
