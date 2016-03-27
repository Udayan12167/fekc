package com.example.shiv.fekc.commons;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.example.shiv.fekc.R;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by shiv on 27/3/16.
 */
public class NotificationUtils {

    private Context context;

    public NotificationUtils() {

    }

    public NotificationUtils(Context context) {
        this.context = context;
    }

    public void showNotificationMessage(final String message, Intent intent) {
        if (TextUtils.isEmpty(message))
            return;

        final int icon = R.mipmap.ic_launcher;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent =
                PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context);
        showSmallNotification(mBuilder, icon, message, resultPendingIntent);
    }

    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String message, PendingIntent resultPendingIntent) {
        Gson gson = new Gson();
//        NotificationMessage notificationMessage = gson.fromJson(message, NotificationMessage.class);
//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//        Notification notification;
//        notification = mBuilder.setSmallIcon(icon).setTicker("Hello").setWhen(0)
//                .setContentTitle(context.getResources().getString(R.string.app_name))
//                .setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE )
//                .setContentIntent(resultPendingIntent)
//                .setStyle(inboxStyle)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentText(notificationMessage.getMessage())
//                .build();
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(Constants.NOTIFICATION_ID, notification);
    }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }
}