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
import android.widget.TextView;
import android.widget.Toast;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.adapter.DBAdapter;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.item.TaskItem;

import java.util.Calendar;

public class ActivityBasedTaskActivity extends AppCompatActivity {
    TaskItem task = new TaskItem();
    DBAdapter dbAdapter;
    private TextView saveTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_activity_based);
        dbAdapter = new DBAdapter();
        saveTextView = (TextView) findViewById(R.id.add_task_save_text);
        saveTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });
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
            int mYear = year;
            int mMonth = monthOfYear;
            int mDay = dayOfMonth;

            EditText editText = (EditText) findViewById(R.id.date);
            editText.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(mDay).append("/")
                    .append(mMonth + 1).append("/")
                    .append(mYear).append(" "));
            System.out.println(editText.getText().toString());
            task.setEndDate(editText.getText().toString());
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==1)
        {
            task.setApps(data.getStringArrayListExtra("SelectedApps"));
            Log.e("Select apps:", task.getApps().toString());

        }
        if(requestCode==2)
        {
            task.setFriends(data.getStringArrayListExtra("SelectedUsers"));
            Log.e("Select users:", task.getFriends().toString());


        }
    }
    public void onSave()
    {
        EditText name = (EditText)findViewById(R.id.taskname);
        EditText description = (EditText)findViewById(R.id.activity_description);
        task.setTaskType(Constants.ACTIVITY_BASED_TASK);
        task.setDuration("NA");

        task.setStartTime("NA");
        task.setEndTime("NA");
        task.setActivityStartFlag(0);
        task.setActivityStopFlag(0);
        task.setTaskID(dbAdapter.getMaxTaskIDFromTaskInfo() + 1);

        int flag = 1;
        try {
            task.setTaskName(name.getText().toString());
        } catch(NullPointerException npe) {

        }
        if(task.getTaskName().length()<=0 || task.getTaskName().length()>=255) {
            Toast.makeText(getApplicationContext(), "Task name should be between 1 to 255 characters.", Toast.LENGTH_SHORT).show();
            flag = 0;
        }

        if(flag==1 && task.getEndDate().length()<=0)
        {
            Toast.makeText(getApplicationContext(),"End date cannot be blank!",Toast.LENGTH_SHORT).show();
            flag = 0;
        }
        if(flag==1 && task.getEndDate().length()>0)
        {
            Calendar calendar = Calendar.getInstance();
            String[] setDate = task.getEndDate().trim().split("/");
            Integer currYear = calendar.get(Calendar.YEAR);
            Integer currMonth = calendar.get(Calendar.MONTH)+ 1;
            Integer currDay = calendar.get(Calendar.DAY_OF_MONTH);
            Log.e("GHUSSAAA", "HEREEEE");
            Log.e("Calender current", currYear.toString() + currMonth.toString() + currDay.toString());
            Log.e("Stored date:",setDate[2].trim()+setDate[1].trim()+setDate[0].trim());
            if(Integer.parseInt(setDate[2].trim())<currYear) {

                Toast.makeText(getApplicationContext(),"End date cannot be older than the current date!",Toast.LENGTH_SHORT).show();
                flag = 0;
            }
            else if(Integer.parseInt(setDate[2].trim())==currYear && Integer.parseInt(setDate[1].trim())<currMonth) {
                Toast.makeText(getApplicationContext(),"End date cannot be older than the current date!",Toast.LENGTH_SHORT).show();
                flag = 0;
            }
            else if(Integer.parseInt(setDate[2].trim())==currYear && Integer.parseInt(setDate[1].trim())==currMonth && Integer.parseInt(setDate[0].trim())<currDay) {
                Toast.makeText(getApplicationContext(),"End date cannot be older than the current date!",Toast.LENGTH_SHORT).show();
                flag = 0;
            }

        }
        if(flag==1 && task.getApps().size()==0) {
            Toast.makeText(getApplicationContext(),"Please add at least one app!",Toast.LENGTH_SHORT).show();
            flag = 0;
        }
        if(flag==1 && task.getFriends().size()==0) {
            Toast.makeText(getApplicationContext(),"Please add at least one friend!",Toast.LENGTH_SHORT).show();
            flag = 0;
        }
        try {
            task.setActivityName(description.getText().toString());
        } catch(NullPointerException npe) {

        }
        if(task.getActivityName().length()<=0 || task.getActivityName().length()>=255) {
            Toast.makeText(getApplicationContext(), "Activity name should be between 1 to 255 characters.", Toast.LENGTH_SHORT).show();
            flag = 0;
        }
        if(flag==1) {
            dbAdapter.insertIntoTaskInfo(task);
            Toast.makeText(getApplicationContext(),"Task Added!",Toast.LENGTH_SHORT).show();
        }


    }
}
