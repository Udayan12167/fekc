package com.example.shiv.fekc.activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;

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
        updateAppList();

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
        Log.d(getClass().toString(), applicationInfo.loadLabel(packageManager).toString());
        appItem.setAppIcon(applicationInfo.loadIcon(packageManager));
        return appItem;
    }

}
