package com.example.shiv.fekc.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.gcm.GCMIntentService;
import com.example.shiv.fekc.rest.client.BackendAPI;
import com.example.shiv.fekc.rest.response.RegisterUserResponse;
import com.example.shiv.fekc.rest.service.BackendAPIServiceClient;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
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

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        backendAPIServiceClient = new BackendAPIServiceClient();

        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);

        Log.d(getClass().toString(), "Called facebook login activity");
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_facebook_login);
        LoginButton loginButton = (LoginButton) findViewById(R.id.faceebook_login_activity_login_button);
        loginButton.setReadPermissions(Constants.FACEBOOK_PERMISSION_USER_FRIENDS);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                registerUser();
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
        parameters.put(Constants.JSON_PARAMETER_FACEBOOK_ID, accessToken.getUserId());
        parameters.put(Constants.JSON_PARAMETER_FB_TOKEN, accessToken.getToken());
        Log.d(getClass().toString(), "Trying to register user : " + accessToken.getUserId() );
        backendAPIServiceClient.getService().registerUser(parameters, new Callback<RegisterUserResponse>() {
            @Override
            public void success(RegisterUserResponse registerUserResponse, Response response) {
                Log.d(getClass().toString(), "Registered user " + registerUserResponse.getOid());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.USER_ACCESS_TOKEN, registerUserResponse.getOid());
                editor.commit();
                Log.d(getClass().toString(), "The user id  is : " + registerUserResponse.getOid());
                if(registerUserResponse.getUserStatusCode() == Constants.CODE_NEW_USER){
                    registerGCM();
                }
                Intent tabActivityIntent = new Intent(FacebookLoginActivity.this, NavActivity.class);
                startActivity(tabActivityIntent);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(getClass().toString(), "Failed to register user : " + Profile.getCurrentProfile().getName());
            }
        });
    }
}
