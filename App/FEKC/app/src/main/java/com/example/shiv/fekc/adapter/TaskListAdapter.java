package com.example.shiv.fekc.adapter;

/**
 * Created by Mrinalk on 30-Mar-16.
 */
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.activity.UserListActivity;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.item.DataObject;
import com.example.shiv.fekc.item.TaskItem;

import java.util.ArrayList;

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
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        /*
        ImageView taskIcon;
        TextView taskName;
        TextView info;
        TextView taskNameFull;
        TextView appsTextView;
        TextView friendsTextView;
        TextView taskTypeField;
        TextView taskTypeFieldData;*/
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

        ArrayList<String> apps = tasks.get(position).getApps();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        Integer height = displaymetrics.heightPixels; //432
        Integer width = displaymetrics.widthPixels;
        Integer scaleFactor = 10;
        Log.e("h and w",height+" ,"+width);
        int i=0;
        for(String packageName:apps) {
            try
            {
                Drawable image = context.getPackageManager().getApplicationIcon(packageName);
                ImageView imageView = new ImageView(context);
                //image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80,60));

                imageView.setImageDrawable(image);
                imageView.setMaxHeight(15);
                imageView.setMaxWidth(15);
               // imageView.setTop(holder.llExpandArea.);
             //   RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
              //          ViewGroup.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(50,50);
                //RelativeLayout.MarginLayoutParams margin = new RelativeLayout.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        //ViewGroup.LayoutParams.WRAP_CONTENT);

                p.addRule(RelativeLayout.BELOW, R.id.apps_view);
                p.setMargins(i * 55, 5, 0, 0);
                //margin.setMargins(i*20,0,0,0);
                i++;
                imageView.setLayoutParams(p);
                    // Adds the view to the layout
                 holder.llExpandArea.addView(imageView);
            }
            catch (PackageManager.NameNotFoundException e)
            {

            }
        }

        ArrayList<String> friends = tasks.get(position).getFriends();
        i=0;
        for(String ID:friends) {
                CircleImageView imageView = UserListActivity.getUserDP(ID,context);
                //image.setLayoutParams(new android.view.ViewGroup.LayoutParams(80,60));
                imageView.setMaxHeight(15);
                imageView.setMaxWidth(15);
                // imageView.setTop(holder.llExpandArea.);
                //   RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                //          ViewGroup.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(50,50);
                //RelativeLayout.MarginLayoutParams margin = new RelativeLayout.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                //ViewGroup.LayoutParams.WRAP_CONTENT);

                p.addRule(RelativeLayout.BELOW, R.id.friends_view);

                p.setMargins(i * 55, 5, 0, 0);
                //margin.setMargins(i*20,0,0,0);
                i++;
                imageView.setLayoutParams(p);
                // Adds the view to the layout
                holder.llExpandArea.addView(imageView);
        }

       // int colorIndex = randy.nextInt(bgColors.length);
       // holder.tvTitle.setText(mDataset.get(position));
     //   holder.label.setBackgroundColor(bgColors[colorIndex]);
      //  holder.dateTime.setBackgroundColor(sbgColors[colorIndex]);
        /*
        if (position == expandedPosition) {
            holder.llExpandArea.setVisibility(View.VISIBLE);
        } else {
            holder.llExpandArea.setVisibility(View.GONE);
        }*/
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
