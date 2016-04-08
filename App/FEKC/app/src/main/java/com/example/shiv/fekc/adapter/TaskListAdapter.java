package com.example.shiv.fekc.adapter;

/**
 * Created by Mrinalk on 30-Mar-16.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.Image;
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
import com.example.shiv.fekc.commons.Functions;
import com.example.shiv.fekc.item.DataObject;
import com.example.shiv.fekc.item.TaskItem;
import com.example.shiv.fekc.rest.response.TaskDeleteResponse;
import com.example.shiv.fekc.rest.response.TaskMessageResponse;
import com.example.shiv.fekc.rest.service.BackendAPIServiceClient;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TaskListAdapter extends RecyclerView
        .Adapter<TaskListAdapter
        .DataObjectHolder> {

    private static String LOG_TAG = "TaskListAdapter";
    private ArrayList<TaskItem> tasks;
    private static MyClickListener myClickListener;
    private static Context context;
    private ImageView dropDownButton;
    private TextView infoDescr;

    private BackendAPIServiceClient backendAPIServiceClient;
    private SharedPreferences sharedPreferences;

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
        Button startActivityButton;
        Button stopActivityButton;

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
            startActivityButton = (Button)itemView.findViewById(R.id.task_view_row_start_button);
//            stopActivityButton = (Button)itemView.findViewById(R.id.task_view_row_stop_button);
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
        backendAPIServiceClient = new BackendAPIServiceClient();
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
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
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {

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
                    public void onClick(final DialogInterface dialog, int which) {
                        //do your work here
                        if(!Functions.isInternetEnabled(context)){
                            Toast.makeText(context, "No internet connection" , Toast.LENGTH_SHORT).show();
                        }

                        String user_id = sharedPreferences.getString(Constants.USER_ACCESS_TOKEN, "");
                        HashMap<String, String> parameters = new HashMap<>();
                        parameters.put(Constants.JSON_PARAMETER_USER_ID, user_id);
                        parameters.put(Constants.JSON_PARAMETER_FB_TOKEN, AccessToken.getCurrentAccessToken().getToken());

                        String taskServerId = tasks.get(position2).getTaskServerId();
                        Log.d(getClass().toString(), "The task server id is : " + taskServerId);

                        backendAPIServiceClient.getService().deleteTask(taskServerId, parameters, new Callback<TaskDeleteResponse>() {
                            @Override
                            public void success(TaskDeleteResponse taskDeleteResponse, Response response) {
                                Log.d(getClass().toString() , "Deleted task on server");
                                DBAdapter db = new DBAdapter();
                                boolean returnValue = db.deleteTask(tasks.get(position2).getTaskID());
                                //tasks.remove(position2);
                                deleteItem(position2);
                                dialog.dismiss();
                                if (returnValue == true) {
                                    Toast.makeText(context, "Task Deleted!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
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


        holder.startActivityButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Perform action on click
                DBAdapter db = new DBAdapter();
                if(tasks.get(position2).getActivityStartFlag()==1) {
                    db.updateStopActivity(tasks.get(position2).getTaskID()) ;
                    holder.startActivityButton.setText("Start Activity");
                    holder.startActivityButton.setBackgroundColor(v.getResources().getColor(R.color.colorGreen));
//                    holder.startActivityButton.setEnabled(false);
                    tasks.get(position2).setActivityStartFlag(0);
                } else {
                    db.updateStartActivity(tasks.get(position2).getTaskID());
//                    holder.startActivityButton.setEnabled(true);
                    holder.startActivityButton.setText("Stop Activity");
                    holder.startActivityButton.setBackgroundColor(v.getResources().getColor(R.color.colorRed));
//                    holder.startActivityButton.setEnabled(true);
                    tasks.get(position2).setActivityStartFlag(1);
                }
                Log.e("Start flag val: ",tasks.get(position2).getActivityStartFlag().toString());

//                tasks.get(position2).setActivityStartFlag(1);
//                tasks.get(position2).setActivityStopFlag(0);
            }
        });
//        holder.stopActivityButton.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                // Perform action on click
//                DBAdapter db = new DBAdapter();
//                db.updateStopActivity(tasks.get(position2).getTaskID()) ;
//                holder.startActivityButton.setEnabled(true);
//                holder.stopActivityButton.setEnabled(false);
//                tasks.get(position2).setActivityStartFlag(0);
//                tasks.get(position2).setActivityStopFlag(1);
//            }
//        });


        if(tasks.get(position).getTaskType()== Constants.ACTIVITY_BASED_TASK) {

            holder.taskIcon.setImageResource(R.drawable.ic_phone_android);
            holder.taskTypeField.setText("Activity Description");
            holder.taskTypeFieldData.setText(tasks.get(position).getActivityName());
            holder.info.setText(tasks.get(position).getActivityName());
            int startFlag = tasks.get(position).getActivityStartFlag();
            Log.e("Start flag value------:"," "+startFlag);
            if(startFlag==1) {
                holder.startActivityButton.setText("Stop Activity");
                holder.startActivityButton.setBackgroundColor(holder.startActivityButton.getResources().getColor(R.color.colorRed));
                //   Log.e("In hereeeeeeeee ",tasks.get(position).getTaskName());
//                holder.startActivityButton.setEnabled(false);
//                holder.stopActivityButton.setEnabled(true);
            }
            else {
                holder.startActivityButton.setText("Start Activity");
                holder.startActivityButton.setBackgroundColor(holder.startActivityButton.getResources().getColor(R.color.colorGreen));
                // Log.e("WTFFFFFffffff ",tasks.get(position).getTaskName());
//                holder.startActivityButton.setEnabled(true);
//                holder.stopActivityButton.setEnabled(false);
            }


        }
        else if(tasks.get(position).getTaskType()==Constants.SCHEDULE_BASED_TASK) {
            holder.taskIcon.setImageResource(R.drawable.ic_date_range);
            holder.taskTypeField.setText("Schedule");
            holder.taskTypeFieldData.setText("From " + tasks.get(position).getStartTime() + "   To " + tasks.get(position).getEndTime());
            holder.info.setText("From " + tasks.get(position).getStartTime() + "   To " + tasks.get(position).getEndTime());
//            holder.stopActivityButton.setVisibility(View.GONE);
            holder.startActivityButton.setVisibility(View.GONE);
        }
        else {
            holder.taskIcon.setImageResource(R.drawable.ic_hourglass_empty);
            holder.taskTypeField.setText("Duration");
            String[] durationString = tasks.get(position).getDuration().split(":");
            String duration = durationString[0].trim()+" hours "+durationString[1].trim() + " minutes";
            holder.taskTypeFieldData.setText(duration);
            holder.info.setText(duration);
            //holder.stopActivityButton.setVisibility(View.GONE);
            holder.startActivityButton.setVisibility(View.GONE);
        }
        if(tasks.get(position).getTaskName().length()>15) {
            holder.taskName.setText(tasks.get(position).getTaskName().substring(0,15)+"...");
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
        dropDownButton = (ImageView) view.findViewById(R.id.dropdown_icon);
        infoDescr = (TextView) view.findViewById(R.id.More_Info);
        // String theString = mDataset.get(holder.getAdapterPosition());
        if(holder.llExpandArea.getVisibility()==View.VISIBLE)
        {
            holder.llExpandArea.setVisibility(View.GONE);
            dropDownButton.setImageResource(R.drawable.ic_keyboard_arrow_down);
            infoDescr.setText("MORE\nINFO");
        }
        else {
            holder.llExpandArea.setVisibility(View.VISIBLE);
            dropDownButton.setImageResource(R.drawable.ic_keyboard_arrow_up);
            infoDescr.setText("LESS\nINFO");
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