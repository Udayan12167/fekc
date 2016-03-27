package com.example.shiv.fekc.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);

        getUserDPUrl(AccessToken.getCurrentAccessToken().getUserId());

        toolbar = (Toolbar) findViewById(R.id.app_bar_nav_toolbar);
        circleImageView = (CircleImageView)toolbar.findViewById(R.id.toolbar_bar_circular_image_view);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.app_bar_nav_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.app_bar_nav_tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        setUpTabIcons();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.app_bar_nav_floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_nav_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.activity_nav_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(this, AppListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

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
        imageView.setImageDrawable(getDrawable(R.drawable.ic_list_white_36dp));
        TextView textView = (TextView) tabOne.findViewById(R.id.custom_layout_text_view);
        textView.setText(getResources().getString(R.string.tasks));
        tabLayout.getTabAt(0).setCustomView(tabOne);

        LinearLayout tabTwo = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        imageView = (ImageView) tabTwo.findViewById(R.id.custom_layout_image_view);
        imageView.setImageDrawable(getDrawable(R.drawable.ic_assessment_white_36dp));
        textView = (TextView) tabTwo.findViewById(R.id.custom_layout_text_view);
        textView.setText(getResources().getString(R.string.stats));
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        LinearLayout tabThree = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        imageView = (ImageView) tabThree.findViewById(R.id.custom_layout_image_view);
        imageView.setImageDrawable(getDrawable(R.drawable.ic_group_white_36dp));
        textView = (TextView) tabThree.findViewById(R.id.custom_layout_text_view);
        textView.setText(getResources().getString(R.string.tracked_friends));
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
                            Picasso.with(NavActivity.this).load(url).into(circleImageView);
                            Log.d(getClass().toString(), url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }
}
