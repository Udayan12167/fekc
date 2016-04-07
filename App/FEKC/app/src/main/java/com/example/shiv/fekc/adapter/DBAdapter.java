package com.example.shiv.fekc.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.commons.Functions;
import com.example.shiv.fekc.item.TaskItem;
import com.example.shiv.fekc.item.ViolationItem;
import com.google.android.gms.gcm.Task;

import java.lang.reflect.Array;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Home on 28-03-2016.
 */
public class DBAdapter {

    private SQLiteDatabase db;
    private static final Pattern DIR_SEPORATOR = Pattern.compile("/");

    public SQLiteDatabase getDB() {
        return db;
    }

    public File getDatabaseDirectory() {
        File sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File directory = new File(sdCard.getAbsolutePath() + File.separator + Constants.APP_NAME);
        if (!directory.exists()) {
            Log.d(getClass().toString(), "Created directory " + directory.getAbsolutePath());
            directory.mkdir();
        }
        return directory;
    }

    public DBAdapter() {

        File dataBaseDirectory = getDatabaseDirectory();
        db = SQLiteDatabase.openDatabase(dataBaseDirectory.getAbsolutePath() + File.separator + Constants.APP_DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        db.execSQL("CREATE TABLE IF NOT EXISTS TaskInfo(task_ID INT,task_name VARCHAR,task_type INT,end_date VARCHAR, start_time VARCHAR, end_time VARCHAR, duration VARCHAR, activity_name VARCHAR, activity_start_flag INT, activity_stop_flag INT, app VARCHAR, friends VARCHAR, task_server_id VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS TaskViolation(task_ID INT,date datetime, violation_type INT);");
    }

    public void insertIntoTaskInfo(TaskItem task) {
        String friends = "";
        for (String user : task.getFriends()) {
            friends = friends + ":" + user;
        }
        Log.e("End date---", task.getEndDate());
        for (String app : task.getApps()) {
            db.execSQL("INSERT INTO TaskInfo VALUES(" + "'" + task.getTaskID() + "'" + "," + "'" + task.getTaskName() + "'" + "," + task.getTaskType() + "," + "'" + task.getEndDate() + "'" + "," + "'" + task.getStartTime() + "'" + "," + "'" + task.getEndTime() + "'" + "," + "'" + task.getDuration() + "'" + "," + "'" + task.getActivityName() + "'" + "," + task.getActivityStartFlag() + "," + task.getActivityStopFlag() + "," + "'" + app + "'" + "," + "'" + friends + "'" + "," + "'" + task.getTaskServerId() + "'" + ");");
        }

    }

    public void insertIntoTaskViolation(ViolationItem violationItem) {
        db.execSQL("INSERT INTO TaskViolation VALUES(" + "'" + violationItem.getTaskID() + "'" + "," + "'" + violationItem.getDate() + "'" + "," + violationItem.getViolationType() + ")");
    }

    public ArrayList<ViolationItem> getAllViolationsMatchingTaskId(Integer taskId) {
        ArrayList<ViolationItem> list = new ArrayList<>();
        Log.d(getClass().toString(), "Calling raw query");
        Cursor result = db.rawQuery("SELECT * FROM TaskViolation WHERE task_ID='" + taskId + "';", null);
        while (result.moveToNext()) {
            ViolationItem violationItem = new ViolationItem();
            violationItem.setTaskID(result.getInt(result.getColumnIndex("task_ID")));
            violationItem.setViolationType(result.getInt(result.getColumnIndex("violation_type")));
            try {
                violationItem.setDate(Functions.getDateFromString(result.getString(result.getColumnIndex("date"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            list.add(violationItem);
        }
        return list;
    }

    public ArrayList<ViolationItem> getAllViolations() {
        ArrayList<ViolationItem> list = new ArrayList<>();
        Log.d(getClass().toString(), "Calling raw query");
        Cursor result = db.rawQuery("SELECT * FROM TaskViolation;", null);
        while (result.moveToNext()) {
            ViolationItem violationItem = new ViolationItem();
            violationItem.setTaskID(result.getInt(result.getColumnIndex("task_ID")));
            violationItem.setViolationType(result.getInt(result.getColumnIndex("violation_type")));
            try {
                violationItem.setDate(Functions.getDateFromString(result.getString(result.getColumnIndex("date"))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            list.add(violationItem);
        }
        return list;
    }


    public Integer getMaxTaskIDFromTaskInfo() {
        Cursor result = db.rawQuery("SELECT COALESCE(MAX(task_ID),0) FROM TaskInfo", null);
        Log.e("Result:", result.toString());
        result.moveToFirst();
        return result.getInt(0);
    }

    public boolean deleteTask(int taskID){
        return db.delete("TaskInfo","task_ID="+taskID,null)>0;
    }
    public ArrayList<TaskItem> getAllTasksMatchingPackageFromTaskInfo(String packageName) {
        Cursor result = db.rawQuery("SELECT * FROM TaskInfo WHERE app='" + packageName + "';", null);
        ArrayList<TaskItem> tasks = new ArrayList<>();
        while (result.moveToNext()) {
            TaskItem task = new TaskItem();
            task.setTaskID(result.getInt(result.getColumnIndex("task_ID")));
            Log.e("TaskID", task.getTaskID().toString());
            task.setTaskName(result.getString(result.getColumnIndex("task_name")));
            Log.e("Task Name", task.getTaskName());
            task.setTaskType(result.getInt(result.getColumnIndex("task_type")));
            Log.e("Task Type", task.getTaskType().toString());
            task.setEndDate(result.getString(result.getColumnIndex("end_date")));
            Log.e("Task end date", task.getEndDate());
            task.setStartTime(result.getString(result.getColumnIndex("start_time")));
            Log.e("Task start time", task.getStartTime());
            task.setEndTime(result.getString(result.getColumnIndex("end_time")));
            Log.e("Task end time", task.getEndTime());
            task.setDuration(result.getString(result.getColumnIndex("duration")));
            Log.e("Task Duration", task.getDuration());
            task.setActivityName(result.getString(result.getColumnIndex("activity_name")));
            Log.e("Task activity name", task.getActivityName());
            task.setActivityStartFlag(result.getInt(result.getColumnIndex("activity_start_flag")));
            Log.e("Task ctivity start flag", task.getActivityStartFlag().toString());
            task.setActivityStopFlag(result.getInt(result.getColumnIndex("activity_stop_flag")));
            Log.e("Task activity stop flag", task.getActivityStopFlag().toString());
            ArrayList<String> apps = new ArrayList<>();
            apps.add(result.getString(result.getColumnIndex("app")));
            task.setApps(apps);
            Log.e("Task apps", task.getApps().toString());
            String[] friends = result.getString(result.getColumnIndex("friends")).split(":");
            ArrayList<String> friendsArray = new ArrayList<>();
            for (String friend : friends) {
                if (!friend.trim().isEmpty()) {
                    friendsArray.add(friend.trim());
                }
            }
            task.setFriends(friendsArray);
            Log.e("Task Friends", task.getFriends().toString());
            task.setTaskServerId(result.getString(result.getColumnIndex("task_server_id")));
            tasks.add(task);
        }

        result.close();
        return tasks;
    }

    public ArrayList<TaskItem> getAllTasksFromTaskInfo() {
        Cursor result = db.rawQuery("SELECT * FROM TaskInfo ;", null);
        ArrayList<TaskItem> tasks = new ArrayList<>();
        HashMap<Integer, ArrayList<String>> taskIDToApp = new HashMap<Integer, ArrayList<String>>();
        while (result.moveToNext()) {
            if (!taskIDToApp.containsKey(result.getInt(result.getColumnIndex("task_ID")))) {
                TaskItem task = new TaskItem();
                task.setTaskID(result.getInt(result.getColumnIndex("task_ID")));
                //Log.e("TaskID", task.getTaskID().toString());
                task.setTaskName(result.getString(result.getColumnIndex("task_name")));
                //Log.e("Task Name", task.getTaskName());
                task.setTaskType(result.getInt(result.getColumnIndex("task_type")));
                //Log.e("Task Type", task.getTaskType().toString());
                task.setEndDate(result.getString(result.getColumnIndex("end_date")));
                //Log.e("Task end date", task.getEndDate());
                task.setStartTime(result.getString(result.getColumnIndex("start_time")));
                //Log.e("Task start time", task.getStartTime());
                task.setEndTime(result.getString(result.getColumnIndex("end_time")));
                //Log.e("Task end time", task.getEndTime());
                task.setDuration(result.getString(result.getColumnIndex("duration")));
                //Log.e("Task Duration", task.getDuration());
                task.setActivityName(result.getString(result.getColumnIndex("activity_name")));
                //Log.e("Task activity name", task.getActivityName());
                task.setActivityStartFlag(result.getInt(result.getColumnIndex("activity_start_flag")));
                //Log.e("Task ctivity start flag", task.getActivityStartFlag().toString());
                task.setActivityStopFlag(result.getInt(result.getColumnIndex("activity_stop_flag")));
                //Log.e("Task activity stop flag", task.getActivityStopFlag().toString());
                task.setTaskServerId(result.getString(result.getColumnIndex("task_server_id")));
                String[] friends = result.getString(result.getColumnIndex("friends")).split(":");
                ArrayList<String> friendsArray = new ArrayList<>();
                for (String friend : friends) {
                    if (!friend.trim().isEmpty()) {
                        friendsArray.add(friend.trim());
                    }
                }
                task.setFriends(friendsArray);
                //Log.e("Task Friends", task.getFriends().toString());
                ArrayList<String> apps = new ArrayList<String>();
                apps.add(result.getString(result.getColumnIndex("app")));
                taskIDToApp.put(task.getTaskID(), apps);
                tasks.add(task);
            } else {
                ArrayList<String> apps = taskIDToApp.get(result.getInt(result.getColumnIndex("task_ID")));
                apps.add(result.getString(result.getColumnIndex("app")));
                taskIDToApp.put(result.getInt(result.getColumnIndex("task_ID")), apps);
            }

        }

        for (TaskItem task : tasks) {
            task.setApps(taskIDToApp.get(task.getTaskID()));
        }

       /* for(TaskItem task:tasks) {
            Log.e("TaskID", task.getTaskID().toString());
            Log.e("Task Name", task.getTaskName());
            Log.e("Task Type", task.getTaskType().toString());
            Log.e("Task end date", task.getEndDate());
            Log.e("Task start time", task.getStartTime());
            Log.e("Task end time", task.getEndTime());
            Log.e("Task Duration", task.getDuration());
            Log.e("Task activity name", task.getActivityName());
            Log.e("Task ctivity start flag", task.getActivityStartFlag().toString());
            Log.e("Task activity stop flag", task.getActivityStopFlag().toString());
            Log.e("Task Friends", task.getFriends().toString());
            Log.e("Task Apps", task.getApps().toString());
        }*/

        result.close();
        return tasks;
    }

    public void updateStartActivity(int taskID){
        ContentValues values = new ContentValues();
        values.put("activity_start_flag",1);
        values.put("activity_stop_flag",0);
        db.update("TaskInfo", values, "task_ID=" + taskID, null);
    }
    public void updateStopActivity(int taskID){
        ContentValues values = new ContentValues();
        values.put("activity_start_flag",0);
        values.put("activity_stop_flag",1);
        db.update("TaskInfo",values,"task_ID="+taskID,null);
    }
    private static String[] getStorageDirectories() {
        // Final set of paths
        final HashSet<String> rv = new HashSet<String>();
        // Primary physical SD-CARD (not emulated)
        final String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
        // All Secondary SD-CARDs (all exclude primary) separated by ":"
        final String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
        // Primary emulated SD-CARD
        final String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
        if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
            // Device has physical external storage; use plain paths.
            if (TextUtils.isEmpty(rawExternalStorage)) {
                // EXTERNAL_STORAGE undefined; falling back to default.
                rv.add("/storage/sdcard0");
            } else {
                rv.add(rawExternalStorage);
            }
        } else {
            // Device has emulated storage; external storage paths should have
            // userId burned into them.
            final String rawUserId;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                rawUserId = "";
            } else {
                final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                final String[] folders = DIR_SEPORATOR.split(path);
                final String lastFolder = folders[folders.length - 1];
                boolean isDigit = false;
                try {
                    Integer.valueOf(lastFolder);
                    isDigit = true;
                } catch (NumberFormatException ignored) {
                }
                rawUserId = isDigit ? lastFolder : "";
            }
            // /storage/emulated/0[1,2,...]
            if (TextUtils.isEmpty(rawUserId)) {
                rv.add(rawEmulatedStorageTarget);
            } else {
                rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
            }
        }
        // Add all secondary storages
        if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
            // All Secondary SD-CARDs splited into array
            final String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
            Collections.addAll(rv, rawSecondaryStorages);
        }
        return rv.toArray(new String[rv.size()]);
    }
}
