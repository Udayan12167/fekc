package com.example.shiv.fekc.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.commons.Constants;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GCMIntentService extends IntentService {


    public GCMIntentService() {
        super("GCMIntentService");
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
            sendRegistrationToServer(token, userId);
        } catch (Exception e) {
            Log.e(getClass().toString(), "Failed to complete token refresh", e);
        }

        Intent registrationComplete = new Intent(Constants.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token, String userId) {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(Constants.JSON_PARAMETER_USER_ID, userId);
        parameters.put(Constants.JSON_PARAMETER_GCM_TOKEN, token);
//        backendAPIClient.getService().addStudentGCMToken(parameters, new Callback<String>() {
//            @Override
//            public void success(String s, Response response) {
//                Log.d(getClass().toString(), "Successfully sent gcm token");
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.d(getClass().toString(), "Failed to send gcm token");
//            }
//        });

    }
}
