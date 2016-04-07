package com.example.shiv.fekc.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.adapter.TaskListAdapter;
import com.example.shiv.fekc.adapter.TrackedFriendListAdapter;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.item.TaskItem;
import com.example.shiv.fekc.item.WarningMessageItem;
import com.example.shiv.fekc.rest.response.TaskCreateResponse;
import com.example.shiv.fekc.rest.response.TaskMessageResponse;
import com.example.shiv.fekc.rest.response.TrackedFriendsTask;
import com.example.shiv.fekc.rest.response.TrackedFriendsTaskResponse;
import com.example.shiv.fekc.rest.response.UpdateUserMessageResponse;
import com.example.shiv.fekc.rest.service.BackendAPIServiceClient;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.android.gms.gcm.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrackedFriendsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrackedFriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackedFriendsFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    private BackendAPIServiceClient backendAPIServiceClient;
    private SharedPreferences sharedPreferences;

    private TrackedFriendListAdapter trackedFriendListAdapter;
    private ArrayList<TrackedFriendsTask> trackedFriendsTaskList;

    public TrackedFriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        backendAPIServiceClient = new BackendAPIServiceClient();
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tracked_friends, container, false);
        trackedFriendsTaskList = new ArrayList<TrackedFriendsTask>();
        trackedFriendListAdapter = new TrackedFriendListAdapter(getContext(), trackedFriendsTaskList);
        trackedFriendListAdapter.setMyClickListener(new TaskListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(getClass().toString(), " Clicked on Item " + position);
                trackedFriendListAdapter.onClick(v, position);
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_tracked_friends_recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(trackedFriendListAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_tracked_friends_swipe_refresh_view);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTrackedTasks();
            }
        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getTrackedTasks();
    }

    private void getTrackedTasks() {
        swipeRefreshLayout.setRefreshing(true);
        Log.d(getClass().toString(), "Calling get tracked tasks");
        HashMap<String, String> parameters = new HashMap<>();
        String id = sharedPreferences.getString(Constants.USER_ACCESS_TOKEN, "");
        parameters.put(Constants.JSON_PARAMETER_FB_TOKEN, AccessToken.getCurrentAccessToken().getToken());
        backendAPIServiceClient.getService().getTrackedTasks(id, parameters, new Callback<TrackedFriendsTaskResponse>() {
            @Override
            public void success(TrackedFriendsTaskResponse trackedFriendsTaskResponse, Response response) {
                swipeRefreshLayout.setRefreshing(false);
                trackedFriendListAdapter.removeAll();
                Log.d(getClass().toString(), "Successfully got tracked tasks");
                if (trackedFriendsTaskResponse.getTasks() == null) {
                    Log.d(getClass().toString(), "Tasks are null :/");
                    return;
                }
                for (TrackedFriendsTask trackedFriendsTask : trackedFriendsTaskResponse.getTasks()) {
                    getUserDPUrl(trackedFriendsTask);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                swipeRefreshLayout.setRefreshing(false);
                error.printStackTrace();
                Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserDPUrl(final TrackedFriendsTask trackedFriendsTask) {
        Bundle params = new Bundle();
        params.putBoolean("redirect", false);
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                Constants.SLASH + trackedFriendsTask.getTrackingFriendId() + Constants.FACEBOOK_USER_PROFILE_PICTURE_EDGE,
                params,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.getJSONObject().getString(Constants.FACEBOOK_JSON_DATA));
                            String imageUrl = jsonObject.getString(Constants.FACEBOOK_JSON_URL);
                            Log.d(getClass().toString(), imageUrl);
                            trackedFriendsTask.setFriendImageUrl(imageUrl);
                            getUserName(trackedFriendsTask);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

        ).executeAsync();
    }

    private void getUserName(final TrackedFriendsTask trackedFriendsTask) {
        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                Constants.SLASH + trackedFriendsTask.getTrackingFriendId(),
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        try {
//                            Log.d(getClass().toString() , response.getJSONObject().toString());
                            JSONObject jsonObject = response.getJSONObject();
                            Log.d(getClass().toString(), jsonObject.toString());
                            String name = jsonObject.getString(Constants.FACEBOOK_JSON_NAME);
                            Log.d(getClass().toString(), name);
                            trackedFriendsTask.setFriendName(name);
                            trackedFriendListAdapter.add(trackedFriendsTask);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        request.executeAsync();
    }

}
