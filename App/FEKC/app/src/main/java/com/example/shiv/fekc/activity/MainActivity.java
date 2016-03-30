package com.example.shiv.fekc.activity;

import android.Manifest;

import com.example.shiv.fekc.commons.Constants;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.commons.Functions;
import com.example.shiv.fekc.service.CheckViolationService;
import com.facebook.AccessToken;

public class MainActivity extends AppCompatActivity {

    private boolean writePermissionGranted = false;
    private boolean packageStatsPermissionGranted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(getClass().toString(), "Main activity");
        Functions.facebookSDKInitialize(getApplicationContext());
        if (savedInstanceState != null) {
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startApp();
    }

    private void startApp() {
        checkPermissionsGranted();
        if (writePermissionGranted && packageStatsPermissionGranted) {
            checkUserLogIn();
        }else {
            finish();
        }

    }

    private void checkUserLogIn(){
        if (AccessToken.getCurrentAccessToken() == null) {
            Log.d(getClass().toString(), "Credentials absent");
            Intent intent = new Intent(this, FacebookLoginActivity.class);
            startActivity(intent);
        } else {
            Log.d(getClass().toString(), "Credentials present");
            Intent intent = new Intent(this, NavActivity.class);
            startActivity(intent);

            Intent checkViolationIntent = new Intent(this, CheckViolationService.class);
            startService(checkViolationIntent);
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
                    writePermissionGranted = true;
                } else {
                    Toast.makeText(MainActivity.this, "The App requires the storage permission to work", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void checkPermissionsGranted() {
        /**
         * Check for write permission
         */
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.MY_PERMISSIONS_REQUEST_STORAGE);
        } else {
            writePermissionGranted = true;
        }
        /**
         * Check for usage stats permission
         */
        AppOpsManager appOps = (AppOpsManager) MainActivity.this
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow("android:get_usage_stats", android.os.Process.myUid(), MainActivity.this.getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;
        if (!granted) {
            Intent usageStatsPermissionIntent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(usageStatsPermissionIntent);
        } else {
            packageStatsPermissionGranted = true;
        }


    }
}
