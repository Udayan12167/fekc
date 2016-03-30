package com.example.shiv.fekc.activity;

import android.Manifest;
import com.example.shiv.fekc.commons.Constants;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.commons.Functions;
import com.example.shiv.fekc.service.CheckViolationService;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;

import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {

    private Button appListButton;
    private Button userListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(getClass().toString(), "Main activity");
        Functions.facebookSDKInitialize(getApplicationContext());
        if(savedInstanceState != null){
            return;
        }


//        appListButton = (Button)findViewById(R.id.activity_main_app_list_button);
//        appListButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, AppListActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        userListButton = (Button)findViewById(R.id.activity_main_user_list_button);
//        userListButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, UserListActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserLogIn();
    }

    private void checkUserLogIn(){
        if(AccessToken.getCurrentAccessToken() == null) {
            Log.d(getClass().toString(), "Credentials absent");
            Intent intent = new Intent(this, FacebookLoginActivity.class);
            startActivity(intent);

            Intent intent2 = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent2);

            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.MY_PERMISSIONS_REQUEST_STORAGE);
            }

        }else{
            Log.d(getClass().toString(), "Credentials present");
            Intent intent = new Intent(this, NavActivity.class);
            startActivity(intent);

            Intent intent2 = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent2);

            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constants.MY_PERMISSIONS_REQUEST_STORAGE);
            }

            finish();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent checkViolationIntent = new Intent(this, CheckViolationService.class);
                    startService(checkViolationIntent);

                }
                return;
            }
        }
    }
}
