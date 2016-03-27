package com.example.shiv.fekc.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.commons.Functions;
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
        }else{
            Log.d(getClass().toString(), "Credentials present");
        }
    }
}
