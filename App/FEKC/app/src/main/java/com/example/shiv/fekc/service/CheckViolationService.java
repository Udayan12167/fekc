package com.example.shiv.fekc.service;

import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.example.shiv.fekc.activity.WarningActivity;
import com.example.shiv.fekc.adapter.DBAdapter;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.item.TaskItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class CheckViolationService extends Service {

    private static String goToButtonForPackage = "";
    private static String foregroundPackage = "";
    private static UsageStats foregroundPackageUsageStats;
    private static String violatedPackage = "";
    DBAdapter dbAdapter = new DBAdapter();
    private Gson gson = new Gson();

    public static String getGoToButtonForPackage() {
        return goToButtonForPackage;
    }

    public static void setGoToButtonForPackage(String packageName) {
        goToButtonForPackage = packageName;
    }

    public static String getViolatedPackage() {
        return violatedPackage;
    }

    public static void setViolatedPackage(String packageName) {
        violatedPackage = packageName;
    }

    public CheckViolationService() {
    }


    @Override
    public void onCreate() {


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //final String packageToCheckOpened = "com.whatsapp";

        Log.e("Service","is running");
        Timer timer  =  new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                foregroundPackage = "" ;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    UsageStatsManager mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
                    long time = System.currentTimeMillis();
                    // We get usage stats for the last 10 seconds
                    List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000*10, time);

                    // Sort the stats by the last time used
                    if(stats != null) {
                        SortedMap<Long,UsageStats> mySortedMap = new TreeMap<Long,UsageStats>();
                        for (UsageStats usageStats : stats) {
                            mySortedMap.put(usageStats.getLastTimeUsed(),usageStats);
                        }
//                        Log.e(getClass().toString(), "The app : " + foregroundPackage + " is running");
                        if(!mySortedMap.isEmpty()) {
                            foregroundPackage =  mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                            foregroundPackageUsageStats = mySortedMap.get(mySortedMap.lastKey());
//                            Log.e(getClass().toString(), "The app : " + foregroundPackage + " is running");
                        }
                    }
                }
//                Log.d(getClass().toString(),foregroundPackage+" is running");


                //Get tasks from DB with foregroundPackage as added apps
                ArrayList<TaskItem> mayBeViolatedTasks = dbAdapter.getAllTasksMatchingPackageFromTaskInfo(foregroundPackage);
                int flag = 0;
                if(mayBeViolatedTasks.size()==0) {
                    goToButtonForPackage = "";
                }
                else {
                    violatedPackage = mayBeViolatedTasks.get(0).getApps().get(0);
                    if(!goToButtonForPackage.equals(violatedPackage)) {
                        goToButtonForPackage = "";

                        Log.e("Task----","HERE 1");

                        for(TaskItem task:mayBeViolatedTasks) {
                            // if (flag == 0) {
                            //Check if schedule based task
                            if (task.getTaskType() == Constants.SCHEDULE_BASED_TASK) {
                                Calendar calendar = Calendar.getInstance();
                                task.setStartTime(cleanTime(task.getStartTime()));
                                task.setEndTime(cleanTime(task.getEndTime()));
                                String[] startTime = task.getStartTime().trim().split(":");
                                String[] endTime = task.getEndTime().trim().split(":");
                                Integer currHour = calendar.get(Calendar.HOUR_OF_DAY);
                                Integer currMin = calendar.get(Calendar.MINUTE);

                                Log.e("Task----", "HERE 2");
                                Log.e("Task----", "start: " + startTime[0] + " " + startTime[1]);
                                Log.e("Task----", "start: " + endTime[0] + " " + endTime[1]);
                                Log.e("Task----", "start: " + currHour.toString() + " " + currMin.toString());

                                //Check if violation occurred
                                if ((Integer.parseInt(startTime[0].trim()) < currHour && Integer.parseInt(endTime[0].trim()) > currHour)
                                        || (Integer.parseInt(startTime[0].trim()) == currHour && Integer.parseInt(startTime[1].trim()) <= currMin && Integer.parseInt(endTime[0].trim()) > currHour)
                                        || (Integer.parseInt(startTime[0].trim()) < currHour && Integer.parseInt(endTime[1].trim()) >= currMin && Integer.parseInt(endTime[0].trim()) == currHour)
                                        || (Integer.parseInt(startTime[0].trim()) == currHour && Integer.parseInt(startTime[1].trim()) <= currMin && Integer.parseInt(endTime[1].trim()) >= currMin && Integer.parseInt(endTime[0].trim()) == currHour)
                                        ) {
                                    flag = 1;
                                    Log.e("Task----", "HERE 3");

                                    Intent intent = new Intent(getApplicationContext(), WarningActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra(Constants.STRING_EXTRA_TASK_SERVER_ID, task.getTaskServerId());
                                    intent.putExtra(Constants.STRING_EXTRA_JSON, gson.toJson(task));
                                    Log.e(getClass().toString() , "The taskId is : " + task.getTaskServerId());
                                    startActivity(intent);

                                }
                            }

                            //Check if Activity Type Task
                            if (task.getTaskType() == Constants.ACTIVITY_BASED_TASK) {
                                Log.e("Activity val flag:",task.getActivityStartFlag()+"");

                                if (task.getActivityStartFlag() == 1) {
                                    flag = 1;
                                    Log.e("Inside check:",task.getActivityStartFlag()+"");
                                    Intent intent = new Intent(getApplicationContext(), WarningActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra(Constants.STRING_EXTRA_TASK_SERVER_ID, task.getTaskServerId());
                                    intent.putExtra(Constants.STRING_EXTRA_JSON, gson.toJson(task));
                                    Log.d(getClass().toString(), "The taskId is : " + task.getTaskServerId());
                                    startActivity(intent);
                                }
                            }

                            //Check if Time Based Task
                            if(task.getTaskType() == Constants.TIME_BASED_TASK) {

                                String[] duration = task.getDuration().split(":");
                                Integer durationInMilSec = 1000*(Integer.parseInt(duration[0].trim())*3600 + Integer.parseInt(duration[1].trim())*60);
                                Log.e("Foreground time",foregroundPackageUsageStats.getTotalTimeInForeground()+"");
                                if(durationInMilSec < foregroundPackageUsageStats.getTotalTimeInForeground()) {
                                    flag = 1;
                                    Intent intent = new Intent(getApplicationContext(), WarningActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra(Constants.STRING_EXTRA_TASK_SERVER_ID, task.getTaskServerId());
                                    intent.putExtra(Constants.STRING_EXTRA_JSON, gson.toJson(task));
                                    Log.e(getClass().toString(), "The taskId is : " + task.getTaskServerId());
                                    startActivity(intent);
                                }
                            }


                            //}
                        }
                    }
                }
                /*
                if (foregroundPackage.equals(packageToCheckOpened)) {
                    if(!Commons.fromGoToAppButtonFlag.contains(packageToCheckOpened)) {
                        Intent intent = new Intent(getApplicationContext(),WarningActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
                else { //Some other package opened => Reset flag
                    if(Commons.fromGoToAppButtonFlag.contains(packageToCheckOpened)) {
                        Commons.fromGoToAppButtonFlag.remove(packageToCheckOpened);
                    }
                }*/

            }
        }, 1, 500);
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        Log.e(getClass().toString(), "The service has been destroyed");
        super.onDestroy();
    }

    private String cleanTime(String time){
        if(time.endsWith("PM")){
            time = time.replace("PM", "");
            String arr[] = time.trim().split(":");
            arr[0] = Integer.toString(Integer.parseInt(arr[0]) + 12);
            return arr[0] + ":" + arr[1];
        }else{
            time = time.replace("AM", "");
            String arr[] = time.trim().split(":");
            if(Integer.parseInt(arr[0]) == 12){
                arr[0] = Integer.toString(Integer.parseInt(arr[0]) - 12);
            }
            return arr[0] + ":" + arr[1];
        }
    }
}
