package com.example.shiv.fekc.activity;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.adapter.AppListAdapter;
import com.example.shiv.fekc.adapter.AppListDurationAdapter;
import com.example.shiv.fekc.item.AppDurationItem;
import com.example.shiv.fekc.item.AppItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

public class AppDurationListActivity extends AppCompatActivity {

    private PackageManager packageManager;
    private UsageStatsManager usageStatsManager;
    private List<UsageStats> usageStatsList;

    private ArrayList<AppDurationItem> appDurationItemList = new ArrayList<AppDurationItem>();
    private RecyclerView recyclerView;
    private AppListDurationAdapter appListDurationAdapter;
    private EditText searchEditText;
    private ProgressBar progressBar;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private Toolbar mToolbar;
    private Map<String, UsageStats> usageStatsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_duration_list);
        if (savedInstanceState != null) {
            return;
        }

        packageManager = getPackageManager();
        mToolbar = (Toolbar) findViewById(R.id.app_list_duration_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("App Usage");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = (RecyclerView) findViewById(R.id.app_list_duration_activity_recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        appListDurationAdapter = new AppListDurationAdapter(this, appDurationItemList);
        recyclerView.setAdapter(appListDurationAdapter);
        recyclerView.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.activity_app_list_duration_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);

        Calendar calendar = new GregorianCalendar();
        long currentTime = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long startTime = calendar.getTimeInMillis() ;
        usageStatsMap = usageStatsManager.queryAndAggregateUsageStats(startTime, currentTime);

        new FetchAppDurationAsyncTask().execute();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_app_duration_list, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                handleMenuSearch();
                return true;
            case android.R.id.home:
                AppDurationListActivity.this.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateAppList() {
        List<ApplicationInfo> applicationInfoList = packageManager.getInstalledApplications(0);
        for (ApplicationInfo applicationInfo : applicationInfoList) {
            if (packageManager.getLaunchIntentForPackage(applicationInfo.packageName) != null) {
                appListDurationAdapter.add(getAppDurationItemFromApplicationInfo(applicationInfo));
            }
        }
    }

    private AppDurationItem getAppDurationItemFromApplicationInfo(ApplicationInfo applicationInfo) {
        AppDurationItem appDurationItem = new AppDurationItem();
        appDurationItem.setAppName(applicationInfo.loadLabel(packageManager).toString());
        appDurationItem.setAppIcon(applicationInfo.loadIcon(packageManager));
        appDurationItem.setAppTime(getTotalForegroundTime(applicationInfo.packageName));
        Log.d(getClass().toString(), applicationInfo.packageName);
        return appDurationItem;
    }

    private String getTotalForegroundTime(String packageName) {
        if(usageStatsMap.get(packageName) != null){
            return  getFormattedUsageTime(usageStatsMap.get(packageName).getTotalTimeInForeground()/1000);
        }else {
            return getFormattedUsageTime(0L);
        }
    }

    private String getFormattedUsageTime(long timeInSeconds) {
        long hours = timeInSeconds / 3600;
        long secondsLeft = timeInSeconds - hours * 3600;
        long minutes = secondsLeft / 60;
        long seconds = secondsLeft - minutes * 60;

        String formattedTime = "";
        if (hours < 10)
            formattedTime += "0";
        formattedTime += hours + ":";

        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";

        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds;

        return formattedTime;
    }

    private class FetchAppDurationAsyncTask extends AsyncTask<Void, Void, Void> {
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
        }
    }

    private void handleMenuSearch() {
        ActionBar action = getSupportActionBar(); //get the actionbar

        if (isSearchOpened) { //test if the search is open


            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            searchEditText.setText("");
            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_black_48dp));

            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.app_list_duration_search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            searchEditText = (EditText) findViewById(R.id.user_list_activity_duration_edit_text);
            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    appListDurationAdapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            searchEditText.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);


            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_close));

            isSearchOpened = true;
        }
    }
}
