package com.example.shiv.fekc.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.activity.NavActivity;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.commons.NotificationUtils;
import com.google.android.gms.gcm.GcmListenerService;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit.RestAdapter;

public class GCMNotificationReceiverService extends GcmListenerService {

    private NotificationUtils notificationUtils;
    int mNotificationId = 1;

    @Override
    public void onMessageReceived(String from, Bundle bundle) {
        for (String key: bundle.keySet())
        {
            Log.d ("myApplication", key + " is a key in the bundle");
        }
        Log.d ("Message is:", bundle.getString("message"));
        String message;
        if(bundle.getString("message").equals("Task violated")){
            message = Constants.VIOLATION_MESSAGE;
        }
        else{
            message = Constants.NOTIFICATION_MESSAGE;
        }
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(Constants.NOTIFICATION_TITLE)
                .setLargeIcon(getBitmapFromURL(bundle.getString(Constants.NOTIFICATION_FRIEND_FBID)))
                .setContentText(message)
                .setAutoCancel(true)
                .setLights(Color.BLUE, 1000, 500)
                .setVibrate(new long[]{300, 300, 300})
                .setSound(uri);

        Intent navIntent = new Intent(this, NavActivity.class);
        navIntent.putExtra(Constants.NOTIFICATION_INTENT_IDENTIFIER, true);

        PendingIntent navPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        navIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(navPendingIntent);
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        mNotificationId++;
    }

    public Bitmap getBitmapFromURL(String userID) {
        try {
            URL url = new URL("https://graph.facebook.com/"+userID+"/picture?type=large");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
