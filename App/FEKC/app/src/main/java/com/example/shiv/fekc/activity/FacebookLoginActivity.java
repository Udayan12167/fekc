package com.example.shiv.fekc.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.gcm.GCMIntentService;
import com.example.shiv.fekc.rest.client.BackendAPI;
import com.example.shiv.fekc.rest.service.BackendAPIServiceClient;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FacebookLoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private BackendAPIServiceClient backendAPIServiceClient;

    private String name;
    private String email;

    private ProfileTracker mProfileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        backendAPIServiceClient = new BackendAPIServiceClient();

        Log.d(getClass().toString(), "Called facebook login activity");
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_facebook_login);
        LoginButton loginButton = (LoginButton) findViewById(R.id.faceebook_login_activity_login_button);
        loginButton.setReadPermissions(Constants.FACEBOOK_PERMISSION_USER_FRIENDS);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getFacebookProfileInfo();
                Intent tabActivityIntent = new Intent(FacebookLoginActivity.this, NavActivity.class);
                startActivity(tabActivityIntent);
                finish();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        if (AccessToken.getCurrentAccessToken() != null) {
            Log.d(getClass().toString(), "Credentials present");
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        callbackManager.onActivityResult(requestCode, resultCode, intent);
        Log.d(getClass().toString(), "Called now");
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    private void registerGCM() {
        Intent intent = new Intent(this, GCMIntentService.class);
        intent.putExtra(Constants.STRING_EXTRA_KEY_GCM, "key");
        intent.putExtra(Constants.STRING_EXTRA_USER_ID, AccessToken.getCurrentAccessToken().getUserId());
        startService(intent);
    }

    private void registerUser() {
        HashMap<String, String> parameters = new HashMap<>();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        parameters.put(Constants.JSON_PARAMETER_USER_ID, accessToken.getUserId());
        parameters.put(Constants.JSON_PARAMETER_NAME, Profile.getCurrentProfile().getName());
        parameters.put(Constants.JSON_PARAMETER_FB_TOKEN, accessToken.getToken());
        Log.d(getClass().toString(), "Trying to register user : " + Profile.getCurrentProfile().getName() + " " + Profile.getCurrentProfile().getId());
        backendAPIServiceClient.getService().registerUser(parameters, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                Log.d(getClass().toString(), "Registered user " + s);
                registerGCM();
                Constants.setAccessToken(s);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(getClass().toString(), "Failed to register user : " + Profile.getCurrentProfile().getName());
            }
        });
    }

    private void getFacebookProfileInfo() {
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                            Profile.getCurrentProfile().getId();
                            registerUser();
                    }
                });
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,name,email,gender,birthday");
//        request.setParameters(parameters);
        request.executeAsync();
    }
}
