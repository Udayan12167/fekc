package com.example.shiv.fekc.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.commons.Functions;
import com.example.shiv.fekc.fragment.StatsFragment;
import com.example.shiv.fekc.fragment.TaskListFragment;
import com.example.shiv.fekc.fragment.TrackedFriendsFragment;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CircleImageView circleImageView;
    private TextView nameTextView;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Functions.facebookSDKInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Log.e("Nav", "onCreateCalled!");

        toolbar = (Toolbar) findViewById(R.id.app_bar_nav_home_toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.app_bar_nav_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.app_bar_nav_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        setUpTabIcons();
        Boolean checkNotif = getIntent().getBooleanExtra(Constants.NOTIFICATION_INTENT_IDENTIFIER, false);
        if(checkNotif){
            TabLayout.Tab tab = tabLayout.getTabAt(2);
            tab.select();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.app_bar_nav_floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavActivity.this, AddTaskActivity.class);
                startActivity(intent);
                finish();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_nav_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.activity_nav_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        createNavigationHeader();
    }

    private void createNavigationHeader() {

        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_nav, null);
        navigationView.addHeaderView(header);

        circleImageView = (CircleImageView) header.findViewById(R.id.nav_header_nav_circular_image_view);
        nameTextView = (TextView) header.findViewById(R.id.nav_header_name_text_view);
        getUserName();
        getUserDPUrl(AccessToken.getCurrentAccessToken().getUserId());

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(this, AppListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(this, UserListActivity.class);
            startActivity(intent);
        }else if(id == R.id.nav_app_duration){
            Intent intent = new Intent(this, AppDurationListActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_nav_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TaskListFragment(), getResources().getString(R.string.tasks));
        adapter.addFragment(new StatsFragment(), getResources().getString(R.string.stats));
        adapter.addFragment(new TrackedFriendsFragment(), getResources().getString(R.string.tracked_friends));
        viewPager.setAdapter(adapter);


    }

    private void setUpTabIcons() {
        LinearLayout tabOne = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        ImageView imageView = (ImageView) tabOne.findViewById(R.id.custom_layout_image_view);
        imageView.setImageDrawable(getDrawable(R.drawable.ic_view_list));
        tabLayout.getTabAt(0).setCustomView(tabOne);

        LinearLayout tabTwo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        imageView = (ImageView) tabTwo.findViewById(R.id.custom_layout_image_view);
        imageView.setImageDrawable(getDrawable(R.drawable.ic_assessment));
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        LinearLayout tabThree = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        imageView = (ImageView) tabThree.findViewById(R.id.custom_layout_image_view);
        imageView.setImageDrawable(getDrawable(R.drawable.ic_group));
        tabLayout.getTabAt(2).setCustomView(tabThree);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void getUserDPUrl(final String facebookId) {
        Log.e("Nav", "getUserDPUrl called!");
        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                Constants.SLASH + facebookId + Constants.FACEBOOK_USER_PROFILE_PICTURE_EDGE,
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            Log.d(getClass().toString(), response.getJSONObject().toString());
                            JSONObject jsonObject = new JSONObject(response.getJSONObject().getString(Constants.FACEBOOK_JSON_DATA));
                            String url = jsonObject.getString(Constants.FACEBOOK_JSON_URL);
                            Log.d(getClass().toString(), url);
                            Picasso.with(NavActivity.this).load(url).into(circleImageView);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch(NullPointerException e) {
                            int max=4;
                            int min=1;
                            Random rand = new Random();
                            int randomNum = rand.nextInt((max - min) + 1) + min;
                            int imageResource = getResources().getIdentifier("drawable/contact"+randomNum,null,getPackageName()); //"drawable/contact" + randomNum + ".jpg", null, getPackageName());
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imageResource);
                            circleImageView.setImageBitmap(bitmap);

                            //circleImageView.setImageDrawable((getResources().getDrawable(R.drawable.contact1)));
                        }
                    }
                }

        ).executeAsync();
    }

    private void getUserName(){
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String name = object.getString(Constants.FACEBOOK_JSON_NAME);
                            nameTextView.setText(name);

                            File cacheDir = new File(NavActivity.this.getCacheDir(), "FEKC");
                            Log.e("cachedir: ",NavActivity.this.getCacheDir().toString());
                            cacheDir.mkdir();
                            FileOutputStream fos = new FileOutputStream(new File(cacheDir, "appUserName"));
                            fos.write(name.getBytes());
                            fos.close();
                            Log.e("cachedir: ", "DOne");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            File cacheDir = new File(NavActivity.this.getCacheDir(), "FEKC");
                            //Log.e("cachedir: ",NavActivity.this.getCacheDir().toString());
                            //cacheDir.mkdir();
                            try {
                                BufferedReader br = new BufferedReader(new FileReader(cacheDir + "/appUserName"));
                                String name;
                                if((name=br.readLine())!=null) {
                                    nameTextView.setText(name);
                                }
                            } catch (FileNotFoundException fe){
                                e.printStackTrace();
                            } catch (IOException ie) {
                                e.printStackTrace();
                            }


                            Log.e("cachedir: ", "DOne");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
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

        if (id == R.id.custom_toolbar_chart) {
            Intent intent = new Intent(NavActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
