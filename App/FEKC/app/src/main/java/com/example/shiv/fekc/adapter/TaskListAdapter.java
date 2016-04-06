package com.example.shiv.fekc.adapter;

/**
 * Created by Mrinalk on 30-Mar-16.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.activity.UserListActivity;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.item.DataObject;
import com.example.shiv.fekc.item.TaskItem;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaskListAdapter extends RecyclerView
        .Adapter<TaskListAdapter
        .DataObjectHolder> {

    private static String LOG_TAG = "TaskListAdapter";
    private ArrayList<TaskItem> tasks;
    private static MyClickListener myClickListener;
    private static Context context;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {

        ImageView taskIcon;
        TextView taskName;
        TextView info;
        TextView taskNameFull;
        TextView taskTypeField;
        TextView taskTypeFieldData;
        ImageView deleteButton;
        RecyclerView appListRecyclerView;
        RecyclerView userListRecyclerView;

        RelativeLayout llExpandArea;

        public DataObjectHolder(View itemView) {
            super(itemView);
            taskIcon = (ImageView) itemView.findViewById(R.id.task_type_icon);
            taskName = (TextView) itemView.findViewById(R.id.task_name);
            info = (TextView) itemView.findViewById(R.id.info);
            taskNameFull = (TextView) itemView.findViewById(R.id.task_name_full);
            taskTypeField = (TextView) itemView.findViewById(R.id.task_type_field);
            taskTypeFieldData = (TextView) itemView.findViewById(R.id.task_type_field_data);
            llExpandArea = (RelativeLayout) itemView.findViewById(R.id.llExpandArea);
            appListRecyclerView = (RecyclerView)itemView.findViewById(R.id.task_view_row_app_recycler_view);
            userListRecyclerView = (RecyclerView)itemView.findViewById(R.id.task_view_row_user_recycler_view);
            deleteButton = (ImageView)itemView.findViewById(R.id.delete_icon);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public TaskListAdapter(ArrayList<TaskItem> tasks, Context context) {
        this.tasks = tasks;
        this.context = context;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_view_row, null);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {

        /*
        ImageView taskIcon;
        TextView taskName;
        TextView info;
        TextView taskNameFull;
        TextView appsTextView;
        TextView friendsTextView;
        TextView taskTypeField;
        TextView taskTypeFieldData;*/
        final int position2 = position;
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Perform action on click
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Alert!");
                alert.setMessage("Are you sure you want to delete the task?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do your work here

                        DBAdapter db = new DBAdapter();
                        boolean returnValue = db.deleteTask(tasks.get(position2).getTaskID()) ;
                        //tasks.remove(position2);
                        deleteItem(position2);
                        dialog.dismiss();
                        if(returnValue==true){
                            Toast.makeText(context, "Task Deleted!" , Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();


            }
        });


        if(tasks.get(position).getTaskType()== Constants.ACTIVITY_BASED_TASK) {
            holder.taskIcon.setImageResource(R.drawable.ic_stay_current_portrait_black_48dp);
            holder.taskTypeField.setText("Activity Description");
            holder.taskTypeFieldData.setText(tasks.get(position).getActivityName());
            holder.info.setText(tasks.get(position).getActivityName());
        }
        else if(tasks.get(position).getTaskType()==Constants.SCHEDULE_BASED_TASK) {
            holder.taskIcon.setImageResource(R.drawable.ic_date_range_black_48dp);
            holder.taskTypeField.setText("Schedule");
            holder.taskTypeFieldData.setText("From " + tasks.get(position).getStartTime() + "   To " + tasks.get(position).getEndTime());
            holder.info.setText("From " + tasks.get(position).getStartTime() + "   To " + tasks.get(position).getEndTime());
        }
        else {
            holder.taskIcon.setImageResource(R.drawable.ic_hourglass_empty_black_48dp);
            holder.taskTypeField.setText("Duration");
            String[] durationString = tasks.get(position).getDuration().split(":");
            String duration = durationString[0].trim()+" hours "+durationString[1].trim()+" minutes";
            holder.taskTypeFieldData.setText(duration);
            holder.info.setText(duration);
        }
        if(tasks.get(position).getTaskName().length()>23) {
            holder.taskName.setText(tasks.get(position).getTaskName().substring(0,23)+"...");
        }
        else {
            holder.taskName.setText(tasks.get(position).getTaskName());
        }
        holder.taskNameFull.setText(tasks.get(position).getTaskName());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        AppAdapter appAdapter = new AppAdapter(context, tasks.get(position).getApps());
        holder.appListRecyclerView.setLayoutManager(linearLayoutManager);
        holder.appListRecyclerView.setAdapter(appAdapter);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(context);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        UserAdapter userAdapter = new UserAdapter(context, new ArrayList<String>());
        holder.userListRecyclerView.setAdapter(userAdapter);
        holder.userListRecyclerView.setLayoutManager(linearLayoutManager1);

        getFriendDPs(tasks.get(position).getFriends(), userAdapter);

    }

    private void getFriendDPs(ArrayList<String> list , UserAdapter userAdapter) {

        for (String string : list) {
            getUserDPUrl(string, userAdapter);
        }

    }

    private void getUserDPUrl(final String facebookId, final UserAdapter userAdapter) {
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
                            JSONObject jsonObject = new JSONObject(response.getJSONObject().getString(Constants.FACEBOOK_JSON_DATA));
                            String url = jsonObject.getString(Constants.FACEBOOK_JSON_URL);
                            userAdapter.add(url);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch(NullPointerException e) {
                            userAdapter.add(" ");
                            e.printStackTrace();
                        }
                    }
                }

        ).executeAsync();
    }


    public void onClick(View view, int position) {
        //  DataObjectHolder holder = (DataObjectHolder) view.getTag();
        DataObjectHolder holder = new DataObjectHolder(view);
        // String theString = mDataset.get(holder.getAdapterPosition());
        if(holder.llExpandArea.getVisibility()==View.VISIBLE)
        {
            holder.llExpandArea.setVisibility(View.GONE);
        }
        else {
            holder.llExpandArea.setVisibility(View.VISIBLE);
        }

        //Toast.makeText(mContext, "Clicked: " , Toast.LENGTH_SHORT).show();
    }


    public void addItem(TaskItem dataObj, int index) {
        tasks.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        tasks.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}