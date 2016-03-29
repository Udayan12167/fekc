package com.example.shiv.fekc.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.service.CheckViolationService;

import java.util.Calendar;

public class WarningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);
        Log.d(getClass().toString(), "Opened warning activity");

    }

    public void stopApp(View v) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
    }

    public void goToApp(View v) {
        //Commons.fromGoToAppButtonFlag.add(packageOpened);
        CheckViolationService.setGoToButtonForPackage(CheckViolationService.getViolatedPackage());
        Log.e("FOREGROUND",CheckViolationService.getViolatedPackage());
        Intent startPackage = getPackageManager().getLaunchIntentForPackage(CheckViolationService.getViolatedPackage());
        startActivity(startPackage);
        finish();
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

}
