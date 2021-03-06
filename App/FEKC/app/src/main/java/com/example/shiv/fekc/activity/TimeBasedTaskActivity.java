package com.example.shiv.fekc.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.adapter.DBAdapter;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.item.TaskItem;
import com.example.shiv.fekc.rest.response.TaskCreateResponse;
import com.example.shiv.fekc.rest.service.BackendAPIServiceClient;
import com.facebook.AccessToken;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TimeBasedTaskActivity extends AppCompatActivity {
    TaskItem task = new TaskItem();
    DBAdapter dbAdapter; // = new DBAdapter();
    private TextView saveTextView;

    private ImageView goBackButton;
    private Window window;

    private PackageManager packageManager;

    private HashSet<String> selectedAppsHashSet = new HashSet<>();
    private HashSet<String> selectedUsersHashSet = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_time_based);

        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.addTaskStatus));

        saveTextView = (TextView) findViewById(R.id.add_task_save_text);
        saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });
        goBackButton = (ImageView) findViewById(R.id.add_task_back_button);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        dbAdapter = new DBAdapter();

        packageManager = getPackageManager();
    }

    public void setDate(View view) {
/*
        DialogFragment picker = new DatePickerFragment();
        picker.show(getFragmentManager(), "datePicker");*/
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        //System.out.println("the selected " + mDay);
        DatePickerDialog dialog = new DatePickerDialog(TimeBasedTaskActivity.this,
                new mDateSetListener(), mYear, mMonth, mDay);
        dialog.show();
    }

    public void setDuration(View view) {
        // TODO Auto-generated method stub
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(TimeBasedTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                EditText editText2 = (EditText) findViewById(R.id.duration_duration);
                editText2.setText(selectedHour + ":" + selectedMinute);
                task.setDuration(selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }

    public void addAppsButton(View view) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, AppListActivity.class);
        intent.putExtra(Constants.STRING_EXTRA_SELECTED_APPS, new ArrayList<String>(selectedAppsHashSet));
        startActivityForResult(intent, 1);
    }

    public void addFriendsButton(View view) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, UserListActivity.class);
        intent.putExtra(Constants.STRING_EXTRA_SELECTED_USERS, new ArrayList<String>(selectedUsersHashSet));
        startActivityForResult(intent, 2);
    }

    class mDateSetListener implements DatePickerDialog.OnDateSetListener {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            // getCalender();
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;

            EditText editText = (EditText) findViewById(R.id.duration_date);
            editText.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mDay).append("/")
                    .append(mMonth + 1).append("/")
                    .append(mYear).append(" "));
            System.out.println(editText.getText().toString());
            task.setEndDate(editText.getText().toString());

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 1) {
            selectedAppsHashSet = new HashSet<>(data.getStringArrayListExtra(Constants.STRING_EXTRA_SELECTED_APPS));
            task.setApps(new ArrayList<String>(selectedAppsHashSet));
            Log.e(getClass().toString(), task.getApps().toString());
        }
        if (requestCode == 2) {
            selectedUsersHashSet = new HashSet<>(data.getStringArrayListExtra(Constants.STRING_EXTRA_SELECTED_USERS));
            task.setFriends(new ArrayList<String>(selectedUsersHashSet));
            Log.e(getClass().toString(), task.getFriends().toString());
        }
    }

    public void onSave() {
        EditText name = (EditText) findViewById(R.id.duration_taskname);
        task.setTaskType(Constants.TIME_BASED_TASK);
        task.setStartTime("NA");
        task.setEndTime("NA");
        task.setActivityName("NA");
        task.setActivityStartFlag(0);
        task.setActivityStopFlag(0);
        task.setTaskID(dbAdapter.getMaxTaskIDFromTaskInfo() + 1);


        // taskInfo.execSQL("INSERT INTO TaskInfo VALUES("+taskID+",'"+task_name+"',"+ task_type+",'"+end_date+"','"+start_time+"','"+end_time+"','"+duration+"','"+activity_name+"','"+app+"','"+friends+ "');");
        //insertIntoDB(taskInfo,task_ID,task_name,task_type,end_date,start_time,end_time,duration,activity_name,app,friends);
        int flag = 1;
        try {
            task.setTaskName(name.getText().toString());
        } catch (NullPointerException npe) {

        }
        if (task.getTaskName().length() <= 0 || task.getTaskName().length() >= 255) {
            Toast.makeText(getApplicationContext(), "Task name should be between 1 to 255 characters.", Toast.LENGTH_SHORT).show();
            flag = 0;
        }
        if (flag == 1 && task.getEndDate().length() <= 0) {
            Toast.makeText(getApplicationContext(), "End date cannot be blank!", Toast.LENGTH_SHORT).show();
            flag = 0;
        }
        if (flag == 1 && task.getEndDate().length() > 0) {
            Calendar calendar = Calendar.getInstance();
            String[] setDate = task.getEndDate().trim().split("/");
            Integer currYear = calendar.get(Calendar.YEAR);
            Integer currMonth = calendar.get(Calendar.MONTH) + 1;
            Integer currDay = calendar.get(Calendar.DAY_OF_MONTH);
            Log.e("Calender current", currYear.toString() + currMonth.toString() + currDay.toString());
            Log.e("Stored date:", setDate[2].trim() + setDate[1].trim() + setDate[0].trim());
            if (Integer.parseInt(setDate[2].trim()) < currYear) {

                Toast.makeText(getApplicationContext(), "End date cannot be older than the current date!", Toast.LENGTH_SHORT).show();
                flag = 0;
            } else if (Integer.parseInt(setDate[2].trim()) == currYear && Integer.parseInt(setDate[1].trim()) < currMonth) {
                Toast.makeText(getApplicationContext(), "End date cannot be older than the current date!", Toast.LENGTH_SHORT).show();
                flag = 0;
            } else if (Integer.parseInt(setDate[2].trim()) == currYear && Integer.parseInt(setDate[1].trim()) == currMonth && Integer.parseInt(setDate[0].trim()) < currDay) {
                Toast.makeText(getApplicationContext(), "End date cannot be older than the current date!", Toast.LENGTH_SHORT).show();
                flag = 0;
            }

        }
        if (flag == 1 && task.getApps().size() == 0) {
            Toast.makeText(getApplicationContext(), "Please add at least one app!", Toast.LENGTH_SHORT).show();
            flag = 0;
        }
        if (flag == 1 && task.getFriends().size() == 0) {
            Toast.makeText(getApplicationContext(), "Please add at least one friend!", Toast.LENGTH_SHORT).show();
            flag = 0;
        }
        if (flag == 1 && task.getDuration().length() == 0) {
            Toast.makeText(getApplicationContext(), "Please add a duration!", Toast.LENGTH_SHORT).show();
            flag = 0;
        }
        if (flag == 1) {
            task.setAppNames(getAppNameListFromPackageNameList(task.getApps()));
            Gson gson = new Gson();
            Intent intent = new Intent(this, TimeBasedTaskReviewActivity.class);
            intent.putExtra(Constants.STRING_EXTRA_JSON, gson.toJson(task));
            startActivity(intent);
//            uploadTaskOnServer();
        }
    }

    private ArrayList<String> getAppNameListFromPackageNameList(ArrayList<String> packageNameList){
        ArrayList<String> appNameList = new ArrayList<String>();
        for(String packageName : packageNameList){
            try {
                appNameList.add((String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)));
            }catch (PackageManager.NameNotFoundException ex){
                appNameList.add("App");
            }
        }
        return appNameList;
    }
}
