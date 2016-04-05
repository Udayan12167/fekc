package com.example.shiv.fekc.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
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
import com.example.shiv.fekc.item.AppItem;

import java.util.ArrayList;
import java.util.List;

public class AppListActivity extends AppCompatActivity {

    private PackageManager packageManager;
    private ArrayList<AppItem> appItemList = new ArrayList<AppItem>();
    private RecyclerView recyclerView;
    private AppListAdapter appListAdapter;
    private EditText searchEditText;
    private ProgressBar progressBar;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        if (savedInstanceState != null) {
            Log.d(getClass().toString(), "Main activity has saved instance");
            return;
        }
        packageManager = getPackageManager();
        mToolbar = (Toolbar) findViewById(R.id.app_list_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Select Apps");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = (RecyclerView)findViewById(R.id.app_list_activity_recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        appListAdapter = new AppListAdapter(this, appItemList);
        recyclerView.setAdapter(appListAdapter);
        recyclerView.setVisibility(View.GONE);


        progressBar = (ProgressBar)findViewById(R.id.activity_app_list_progressBar);
        progressBar.setVisibility(View.VISIBLE);

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

    public void onSave()
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
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                handleMenuSearch();
                return true;
            case R.id.app_list_save_button:
                onSave();
                return true;
            case android.R.id.home:
                AppListActivity.this.onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar

        if(isSearchOpened){ //test if the search is open


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
            action.setCustomView(R.layout.app_list_search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

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


            searchEditText.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);


            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_close));

            isSearchOpened = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_app_list, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
