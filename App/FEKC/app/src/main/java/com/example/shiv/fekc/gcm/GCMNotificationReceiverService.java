package com.example.shiv.fekc.gcm;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.commons.NotificationUtils;
import com.google.android.gms.gcm.GcmListenerService;

public class GCMNotificationReceiverService extends GcmListenerService {

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        String message = bundle.getString(Constants.NOTIFICATION_MESSAGE);
        Intent resultIntent= null;
        Log.d(getClass().toString(), "NOTIFICATION RECEIVED " + message );
////        Intent resultIntent = new Intent(getApplicationContext(), QuizActivity.class);
////        resultIntent.putExtra(Constants.STRING_EXTRA_JSON, message);
//        showNotificationMessage(getApplicationContext(), message, resultIntent);
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String message, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(message, intent);
    }
}
