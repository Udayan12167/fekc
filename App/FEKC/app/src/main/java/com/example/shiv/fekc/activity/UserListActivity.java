package com.example.shiv.fekc.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.adapter.UserListAdapter;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.item.UserItem;
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

    private ArrayList<UserItem> userItemList = new ArrayList<UserItem>();
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
                            UserItem[] array = gson.fromJson(jsonArray.toString(), UserItem[].class);
                            Log.d(getClass().toString(), jsonArray.toString());
                            for (UserItem userItem : array) {
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

    private void getUserDPUrl(final UserItem userItem) {
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
