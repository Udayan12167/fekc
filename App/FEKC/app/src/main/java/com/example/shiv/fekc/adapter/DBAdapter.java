package com.example.shiv.fekc.adapter;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by Home on 28-03-2016.
 */
public class DBAdapter {

    private SQLiteDatabase db;

    public SQLiteDatabase getDB() {
        return db;
    }

    public DBAdapter() {
        //Create database if not exists
        File fekcFolderCreate = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/FEKC");
        fekcFolderCreate.mkdir();
        db = SQLiteDatabase.openDatabase(Environment.getExternalStorageDirectory().getAbsolutePath() + "/FEKC/FEKCDB.db", null, SQLiteDatabase.CREATE_IF_NECESSARY);
        //Create tables if not exists
        db.execSQL("CREATE TABLE IF NOT EXISTS TaskInfo(task_ID INT,task_name VARCHAR,task_type INT,end_date VARCHAR, start_time VARCHAR, end_time VARCHAR, duration VARCHAR, activity_name VARCHAR, app VARCHAR, friends VARCHAR);");
    }

    public void insertIntoTaskInfo(Integer task_ID, String task_name, Integer task_type, String end_date, String start_time, String end_time, String duration, String activity_name, String app, String friends)
    {
        db.execSQL("INSERT INTO TaskInfo VALUES("+task_ID+",'"+task_name+"',"+ task_type+",'"+end_date+"','"+start_time+"','"+end_time+"','"+duration+"','"+activity_name+"','"+app+"','"+friends+ "');");
    }

    public Integer getMaxTaskIDFromTaskInfo() {
        Cursor result = db.rawQuery("SELECT COALESCE(MAX(task_ID),0) FROM TaskInfo",null);
        Log.e("Result:", result.toString());
        result.moveToFirst();
        return result.getInt(0);
    }

}
