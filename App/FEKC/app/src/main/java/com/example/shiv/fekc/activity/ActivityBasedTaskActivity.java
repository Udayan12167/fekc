package com.example.shiv.fekc.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.shiv.fekc.R;

import java.util.Calendar;

public class ActivityBasedTaskActivity extends AppCompatActivity {
    String end_date;
    String task_name;
    String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_based_task);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_based_task, menu);
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
        DatePickerDialog dialog = new DatePickerDialog(ActivityBasedTaskActivity.this,
                new mDateSetListener(), mYear, mMonth, mDay);
        dialog.show();




    }
/*
    public void setDuration(View view) {
        // TODO Auto-generated method stub
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TextClockDialog mTimePicker;
        mTimePicker = new TimePickerDialog(TimeBasedTaskActivity.this, new TimeClockDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                EditText editText2 = (EditText) findViewById(R.id.start_time_edit_text);
                editText2.setText( selectedHour + ":" + selectedMinute);
                start_time = selectedHour + ":" + selectedMinute;
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }*/


    public void addAppsButton(View view) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, AppListActivity.class);
        startActivity(intent);
    }

    public void addFriendsButton(View view) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this, UserListActivity.class);
        startActivity(intent);
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
    public void onSave(View view)
    {
        EditText mName = (EditText)findViewById(R.id.taskname);
        EditText mDescription = (EditText)findViewById(R.id.activity_description);
        description = mDescription.getText().toString();
        task_name = mName.getText().toString();
        Log.e("Task Name:", task_name);
        Log.e("End Date:",end_date);
        Log.e("Description:",description);
        SQLiteDatabase task_info_db = openOrCreateDatabase("task_info", MODE_PRIVATE, null);
        task_info_db.execSQL("CREATE TABLE IF NOT EXISTS task_info(task_name VARCHAR,end_date VARCHAR, start_time VARCHAR, end_time VARCHAR, duration VARCHAR, activity_name VARCHAR, app VARCHAR, friends VARCHAR);");
        // mydatabase.execSQL("INSERT INTO TutorialsPoint VALUES('admin','admin');");

    }
}
