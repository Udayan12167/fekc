package com.example.shiv.fekc.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.adapter.AppListAdapter;
import com.example.shiv.fekc.item.AppItem;

import java.util.ArrayList;
import java.util.List;

public class AppListActivity extends AppCompatActivity {

    private PackageManager packageManager;
    private ArrayList<AppItem> appItemList = new ArrayList<AppItem>();
    private RecyclerView recyclerView;
    private AppListAdapter appListAdapter;
    private EditText searchEditText;
    private LinearLayout searchViewLinearLayout;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        if (savedInstanceState != null) {
            Log.d(getClass().toString(), "Main activity has saved instance");
            return;
        }
        packageManager = getPackageManager();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        searchViewLinearLayout = (LinearLayout)findViewById(R.id.app_list_activity_linear_layout);
        recyclerView = (RecyclerView)findViewById(R.id.app_list_activity_recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        appListAdapter = new AppListAdapter(this, appItemList);
        recyclerView.setAdapter(appListAdapter);
        recyclerView.setVisibility(View.GONE);


        progressBar = (ProgressBar)findViewById(R.id.activity_app_list_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        searchEditText = (EditText) findViewById(R.id.app_list_activity_search_edit_text);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                appListAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchViewLinearLayout.setVisibility(View.GONE);
        new FetchAppsAsyncTask().execute();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void updateAppList(){
        List<ApplicationInfo> applicationInfoList = packageManager.getInstalledApplications(0);
        for (ApplicationInfo applicationInfo : applicationInfoList) {
            if( packageManager.getLaunchIntentForPackage(applicationInfo.packageName) != null ){
                //This app is a non-system app
                appListAdapter.add(getAppItemFromApplicationInfo(applicationInfo));
            }
        }
    }

    private AppItem getAppItemFromApplicationInfo(ApplicationInfo applicationInfo) {
        AppItem appItem = new AppItem();
        appItem.setAppName(applicationInfo.loadLabel(packageManager).toString());
        appItem.setPackageName(applicationInfo.packageName.toString());
        Log.d(getClass().toString(), applicationInfo.loadLabel(packageManager).toString());
        appItem.setAppIcon(applicationInfo.loadIcon(packageManager));
        return appItem;
    }
    public void onSave(View view)
    {
        ArrayList<String> selectedApps = appListAdapter.getSelectedApps();

        Intent intent=new Intent();
        intent.putExtra("SelectedApps",selectedApps);
        setResult(1,intent);
        finish();
    }

    private class FetchAppsAsyncTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            updateAppList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(getClass().toString(), "Fetched all apps");
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            searchViewLinearLayout.setVisibility(View.VISIBLE);
        }
    }

}
