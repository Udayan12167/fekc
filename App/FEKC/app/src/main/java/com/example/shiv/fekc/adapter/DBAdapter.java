package com.example.shiv.fekc.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.example.shiv.fekc.item.TaskItem;

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

    public DBAdapter(Context context) {
        //Create database if not exists
        String[] rv = getStorageDirectories();
        Log.e("Path to database:",rv[0]);

        File fekcFolderCreate = new File(rv[0] + "/FEKC");
        fekcFolderCreate.mkdirs();
        //Environment.getExternalStorageDirectory().getAbsolutePath() + "
        db = SQLiteDatabase.openDatabase(rv[0]+"/FEKC/FEKCDB.db", null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //Create tables if not exists
        db.execSQL("CREATE TABLE IF NOT EXISTS TaskInfo(task_ID INT,task_name VARCHAR,task_type INT,end_date VARCHAR, start_time VARCHAR, end_time VARCHAR, duration VARCHAR, activity_name VARCHAR, activity_start_flag INT, activity_stop_flag INT, app VARCHAR, friends VARCHAR);");
    }

    public void insertIntoTaskInfo(TaskItem task) {
        String friends = "";
        for (String user:task.getFriends()) {
            friends=friends+":"+user;
        }
        Log.e("End date---",task.getEndDate());
        for (String app : task.getApps()) {
            db.execSQL("INSERT INTO TaskInfo VALUES(" + task.getTaskID() + ",'" + task.getTaskName() + "'," + task.getTaskType() + ",'" + task.getEndDate() + "','" + task.getStartTime() + "','" + task.getEndTime() + "','" +  task.getDuration() + "','" + task.getActivityName() + "',"+task.getActivityStartFlag()+","+task.getActivityStopFlag()+",'" + app + "','" + friends + "');");
        }

    }

    public Integer getMaxTaskIDFromTaskInfo() {
        Cursor result = db.rawQuery("SELECT COALESCE(MAX(task_ID),0) FROM TaskInfo",null);
        Log.e("Result:", result.toString());
        result.moveToFirst();
        return result.getInt(0);
    }
    public static String[] getStorageDirectories()
    {
        // Final set of paths
        final HashSet<String> rv = new HashSet<String>();
        // Primary physical SD-CARD (not emulated)
        final String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
        // All Secondary SD-CARDs (all exclude primary) separated by ":"
        final String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
        // Primary emulated SD-CARD
        final String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
        if(TextUtils.isEmpty(rawEmulatedStorageTarget))
        {
            // Device has physical external storage; use plain paths.
            if(TextUtils.isEmpty(rawExternalStorage))
            {
                // EXTERNAL_STORAGE undefined; falling back to default.
                rv.add("/storage/sdcard0");
            }
            else
            {
                rv.add(rawExternalStorage);
            }
        }
        else
        {
            // Device has emulated storage; external storage paths should have
            // userId burned into them.
            final String rawUserId;
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
            {
                rawUserId = "";
            }
            else
            {
                final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                final String[] folders = DIR_SEPORATOR.split(path);
                final String lastFolder = folders[folders.length - 1];
                boolean isDigit = false;
                try
                {
                    Integer.valueOf(lastFolder);
                    isDigit = true;
                }
                catch(NumberFormatException ignored)
                {
                }
                rawUserId = isDigit ? lastFolder : "";
            }
            // /storage/emulated/0[1,2,...]
            if(TextUtils.isEmpty(rawUserId))
            {
                rv.add(rawEmulatedStorageTarget);
            }
            else
            {
                rv.add(rawEmulatedStorageTarget + File.separator + rawUserId);
            }
        }
        // Add all secondary storages
        if(!TextUtils.isEmpty(rawSecondaryStoragesStr))
        {
            // All Secondary SD-CARDs splited into array
            final String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
            Collections.addAll(rv, rawSecondaryStorages);
        }
        return rv.toArray(new String[rv.size()]);
    }

}
