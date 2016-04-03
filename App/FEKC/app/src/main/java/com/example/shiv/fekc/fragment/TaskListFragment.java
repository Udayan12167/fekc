package com.example.shiv.fekc.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.adapter.DBAdapter;
import com.example.shiv.fekc.adapter.TaskListAdapter;
import com.example.shiv.fekc.item.TaskItem;

import java.util.ArrayList;


public class TaskListFragment extends Fragment {

    public TaskListFragment() {
        // Required empty public constructor
    }
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Code to Add an item with default animation
        //((TaskListAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((TaskListAdapter) mAdapter).deleteItem(index);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new TaskListAdapter(getDataSet(), this.getContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter = new TaskListAdapter(getDataSet(),this.getContext());
        mRecyclerView.setAdapter(mAdapter);
        ((TaskListAdapter) mAdapter).setOnItemClickListener(new TaskListAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, " Clicked on Item " + position);
                ((TaskListAdapter) mAdapter).onClick(v, position);

            }
        });
    }

    private ArrayList<TaskItem> getDataSet() {
        DBAdapter dbAdapter = new DBAdapter();
        ArrayList<TaskItem> tasks = dbAdapter.getAllTasksFromTaskInfo();
        Log.e("Task size",tasks.size()+"");
        return tasks;
    }

}
