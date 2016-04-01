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

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.item.TaskItem;
import com.example.shiv.fekc.rest.response.TrackedFriendsTaskResponse;
import com.example.shiv.fekc.rest.service.BackendAPIServiceClient;
import com.facebook.AccessToken;
import com.google.android.gms.gcm.Task;

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

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_tracked_friends_recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.fragment_tracked_friends_swipe_refresh_view);
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

    }

    @Override
    public void onResume() {
        super.onResume();
        getTrackedTasks();
    }

    private void getTrackedTasks(){
        Log.d(getClass().toString(), "Calling get tracked tasks");
        HashMap<String, String> parameters = new HashMap<>();
        String id = sharedPreferences.getString(Constants.USER_ACCESS_TOKEN, "");
        parameters.put(Constants.JSON_PARAMETER_FB_TOKEN, AccessToken.getCurrentAccessToken().getToken());
        backendAPIServiceClient.getService().getTrackedTasks(id, parameters, new Callback<TrackedFriendsTaskResponse>() {
            @Override
            public void success(TrackedFriendsTaskResponse trackedFriendsTaskResponse, Response response) {
//                Log.d(getClass().toString(), string);
                swipeRefreshLayout.setRefreshing(false);
                Log.d(getClass().toString() , "Successfully got tracked tasks");
////                if(trackedFriendsTaskResponse.getTasks() != null) {
////                    for (TaskItem taskItem : trackedFriendsTaskResponse.getTasks()) {
////                        Log.d(getClass().toString(), taskItem.getActivityName());
////                    }
//                }
            }

            @Override
            public void failure(RetrofitError error) {
                swipeRefreshLayout.setRefreshing(false);
                error.printStackTrace();
                Log.d(getClass().toString(), "Failed");
            }
        });

    }
}
