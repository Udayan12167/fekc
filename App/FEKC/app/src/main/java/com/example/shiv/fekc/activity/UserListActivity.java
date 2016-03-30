package com.example.shiv.fekc.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.adapter.UserListAdapter;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.item.UserListItem;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    private ArrayList<UserListItem> userItemList = new ArrayList<UserListItem>();
    private RecyclerView recyclerView;
    private UserListAdapter userListAdapter;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        gson = new Gson();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) findViewById(R.id.user_list_activity_recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        userListAdapter = new UserListAdapter(this, userItemList);
        recyclerView.setAdapter(userListAdapter);

        getFriendList();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void getFriendList() {
        getUserFriends();
    }

    private void getUserFriends() {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                Constants.FACEBOOK_USER_FRIENDS_EDGE,
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONArray jsonArray = response.getJSONObject().getJSONArray(Constants.FACEBOOK_JSON_DATA);
                            UserListItem[] array = gson.fromJson(jsonArray.toString(), UserListItem[].class);
                            Log.d(getClass().toString(), jsonArray.toString());
                            for (UserListItem userItem : array) {
                                Log.d(getClass().toString(), userItem.getId());
                                getUserDPUrl(userItem);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    public void onSave(View view){
        ArrayList<String> selectedUsers = userListAdapter.getSelectedUsers();

        Intent intent=new Intent();
        intent.putExtra("SelectedUsers",selectedUsers);
        setResult(2,intent);
        finish();
    }
    private void getUserDPUrl(final UserListItem userItem) {
        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                Constants.SLASH + userItem.getId() + Constants.FACEBOOK_USER_PROFILE_PICTURE_EDGE,
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            Log.d(getClass().toString(), response.getJSONObject().toString());
                            JSONObject jsonObject = new JSONObject(response.getJSONObject().getString(Constants.FACEBOOK_JSON_DATA));
                            String url = jsonObject.getString(Constants.FACEBOOK_JSON_URL);
                            Log.d(getClass().toString(), userItem.getName());
                            userItem.setImageUrl(url);
                            userListAdapter.add(userItem);
//                            Log.d(getClass().toString(), data);
                            Log.d(getClass().toString(), url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }
}
