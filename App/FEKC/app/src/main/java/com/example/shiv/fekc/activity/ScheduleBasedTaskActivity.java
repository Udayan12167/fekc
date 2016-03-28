package com.example.shiv.fekc.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.shiv.fekc.adapter.DBAdapter;

import com.example.shiv.fekc.R;

import java.util.ArrayList;
import java.util.Calendar;

public class ScheduleBasedTaskActivity extends AppCompatActivity {
    String end_date;
    ArrayList<String> selectedApps;
    ArrayList<String> selectedUsers;
    String task_name;
    String start_time;
    String end_time;

    DBAdapter dbAdapter = new DBAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_based_task);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule_based_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        DatePickerDialog dialog = new DatePickerDialog(ScheduleBasedTaskActivity.this,
                new mDateSetListener(), mYear, mMonth, mDay);
        dialog.show();




    }

    public void setStartTime(View view) {
        // TODO Auto-generated method stub
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ScheduleBasedTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                EditText editText2 = (EditText) findViewById(R.id.start_time_edit_text);
                editText2.setText( selectedHour + ":" + selectedMinute);
                start_time = selectedHour + ":" + selectedMinute;
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }
    public void setEndTime(View view) {
        // TODO Auto-generated method stub
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(ScheduleBasedTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                EditText editText2 = (EditText) findViewById(R.id.end_time_edit_text);
                editText2.setText( selectedHour + ":" + selectedMinute);
                end_time = selectedHour + ":" + selectedMinute;
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
       // end_time = selectedHour + ":" + selectedMinute;

    }

    public void addAppsButton(View view) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, AppListActivity.class);
        startActivityForResult(intent, 1);
    }

    public void addFriendsButton(View view) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, UserListActivity.class);
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

            EditText editText = (EditText) findViewById(R.id.date);
            editText.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mMonth + 1).append("/").append(mDay).append("/")
                    .append(mYear).append(" "));
            System.out.println(editText.getText().toString());
            end_date = editText.getText().toString();


        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==1)
        {
            selectedApps=data.getStringArrayListExtra("SelectedApps");
            Log.e("Select apps:",selectedApps.toString());

        }
        if(requestCode==2)
        {
            selectedUsers=data.getStringArrayListExtra("SelectedUsers");
            Log.e("Select users:",selectedUsers.toString());


        }
    }
    public void onSave(View view)
    {
        EditText name = (EditText)findViewById(R.id.taskname);
        task_name = name.getText().toString();
        Log.e("Task Name:", task_name);
      //  Log.e("End Date:",end_date);
       // Log.e("Start Time:",start_time);
        //Log.e("End Time:",end_time);
        String friends = "";
        for (String user:selectedUsers) {
            friends=friends+":"+user;
        }
        Integer task_type = 2;
        Integer task_ID=1;
        String duration = "NA";
        String activity_name = "NA";
        /*File sdcard = Environment.getExternalStorageDirectory();
        SQLiteDatabase taskInfo = openOrCreateDatabase(sdcard.getAbsolutePath() + "/FEKC/TaskInfo.db", MODE_PRIVATE, null);
        taskInfo.execSQL("CREATE TABLE IF NOT EXISTS TaskInfo(task_ID INT,task_name VARCHAR,task_type INT,end_date VARCHAR, start_time VARCHAR, end_time VARCHAR, duration VARCHAR, activity_name VARCHAR, app VARCHAR, friends VARCHAR);");
        Cursor result = taskInfo.rawQuery("SELECT COALESCE(MAX(task_ID),0) FROM TaskInfo",null);
        Log.e("Result:",result.toString());
        if(result.getCount()>0) {
            result.moveToFirst();
            Integer last_ID = result.getInt(0);
            task_ID = last_ID +1;
        }*/
        task_ID = dbAdapter.getMaxTaskIDFromTaskInfo()+1;
        for (String app:selectedApps)
        {
           // taskInfo.execSQL("INSERT INTO TaskInfo VALUES("+taskID+",'"+task_name+"',"+ task_type+",'"+end_date+"','"+start_time+"','"+end_time+"','"+duration+"','"+activity_name+"','"+app+"','"+friends+ "');");
            //insertIntoDB(taskInfo,task_ID,task_name,task_type,end_date,start_time,end_time,duration,activity_name,app,friends);
            dbAdapter.insertIntoTaskInfo(task_ID,task_name,task_type,end_date,start_time,end_time,duration,activity_name,app,friends);
        }
        Toast.makeText(getApplicationContext(),"Task Added!",Toast.LENGTH_SHORT).show();

    }
}
