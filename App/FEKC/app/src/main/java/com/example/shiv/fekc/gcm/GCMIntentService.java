package com.example.shiv.fekc.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.rest.response.UpdateGCMTokenResponse;
import com.example.shiv.fekc.rest.service.BackendAPIServiceClient;
import com.facebook.AccessToken;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GCMIntentService extends IntentService {

    private SharedPreferences sharedPreferences;

    private BackendAPIServiceClient backendAPIServiceClient;

    public GCMIntentService() {
        super("GCMIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        backendAPIServiceClient = new BackendAPIServiceClient();
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String key = intent.getStringExtra(Constants.STRING_EXTRA_KEY_GCM);
        String userId = intent.getStringExtra(Constants.STRING_EXTRA_USER_ID);
        if (key == null || userId == null) {
            return;
        }
        registerGCM(userId);
    }

    private void registerGCM(String userId) {
        String token = null;
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            token = instanceID.getToken(getString(R.string.gcm_credential_project_id),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.e(getClass().toString(), "GCM Registration Token: " + token);
            sendRegistrationToServer(token);
        } catch (Exception e) {
            Log.e(getClass().toString(), "Failed to complete token refresh", e);
        }

        Intent registrationComplete = new Intent(Constants.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        HashMap<String, String> parameters = new HashMap<>();
        final String id = sharedPreferences.getString(Constants.USER_ACCESS_TOKEN, "");
        Log.d(getClass().toString(), "The user id  is : " + id);
        parameters.put(Constants.JSON_PARAMETER_FACEBOOK_ID, AccessToken.getCurrentAccessToken().getUserId());
        parameters.put(Constants.JSON_PARAMETER_GCM_TOKEN, token);
        parameters.put(Constants.JSON_PARAMETER_FB_TOKEN, AccessToken.getCurrentAccessToken().getToken());
        backendAPIServiceClient.getService().updateUserGCMToken(id, parameters, new Callback<UpdateGCMTokenResponse>() {
            @Override
            public void success(UpdateGCMTokenResponse updateGCMTokenResponse, Response response) {
                Log.d(getClass().toString(), "Sent GCM token to server for user : " + id);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                Log.d(getClass().toString(), "Unable to send GCM token to server for user : " + id);
            }
        });
    }
}
