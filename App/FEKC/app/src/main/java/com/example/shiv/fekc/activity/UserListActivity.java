package com.example.shiv.fekc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.adapter.UserListAdapter;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.item.UserListItem;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListActivity extends AppCompatActivity {

    private ArrayList<UserListItem> userItemList = new ArrayList<UserListItem>();
    private RecyclerView recyclerView;
    private UserListAdapter userListAdapter;
    private Gson gson;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private Toolbar mToolbar;
    private EditText edtSeach;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        gson = new Gson();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        if (savedInstanceState != null) {
            return;
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView = (RecyclerView) findViewById(R.id.user_list_activity_recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        userListAdapter = new UserListAdapter(this, userItemList);
        recyclerView.setAdapter(userListAdapter);
        mToolbar = (Toolbar) findViewById(R.id.user_list_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Select Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar)findViewById(R.id.activity_user_list_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        getFriendList();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void getFriendList() {
        getUserFriends();
        progressBar.setVisibility(View.GONE);
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

    public void onSave(){
        ArrayList<String> selectedUsers = userListAdapter.getSelectedUsers();

        Intent intent=new Intent();
        intent.putExtra("SelectedUsers",selectedUsers);
        setResult(2, intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        ArrayList<String> selectedUsers = new ArrayList<String>();

        Intent intent=new Intent();
        intent.putExtra("SelectedUsers",selectedUsers);
        setResult(2, intent);
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

    public static CircleImageView getUserDP(String userID, final Context context) {
        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        final CircleImageView image= new CircleImageView(context);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                Constants.SLASH + userID + Constants.FACEBOOK_USER_PROFILE_PICTURE_EDGE,
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            Log.d(getClass().toString(), response.getJSONObject().toString());
                            JSONObject jsonObject = new JSONObject(response.getJSONObject().getString(Constants.FACEBOOK_JSON_DATA));
                            String url = jsonObject.getString(Constants.FACEBOOK_JSON_URL);
                           // Log.d(getClass().toString(), userItem.getName());
                            //userItem.setImageUrl(url);
                            //userListAdapter.add(userItem);
//                            Log.d(getClass().toString(), data);
                            Log.d(getClass().toString(), url);

                            Picasso.with(context).load(url).into(image);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
        return image;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search_user_list);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search_user_list:
                handleMenuSearch();
                return true;
            case R.id.user_list_save_button:
                onSave();
                return true;
            case android.R.id.home:
                UserListActivity.this.onBackPressed();
                return true;
            }
        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar

        if(isSearchOpened){ //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            edtSeach.setText("");
            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_black_48dp));

            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.user_list_search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            edtSeach = (EditText)action.getCustomView().findViewById(R.id.user_list_activity_search_edit_text); //the text editor

            //this is a listener to do a search when the user clicks on search button
            edtSeach.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    userListAdapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            edtSeach.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);


            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_close));

            isSearchOpened = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_list, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
