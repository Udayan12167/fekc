package com.example.shiv.fekc.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.commons.Constants;
import com.example.shiv.fekc.holder.UserListViewHolder;
import com.example.shiv.fekc.item.TrackedFriendAppItem;
import com.example.shiv.fekc.rest.response.TrackedFriendsTask;
import com.example.shiv.fekc.rest.response.UpdateUserMessageResponse;
import com.example.shiv.fekc.rest.service.BackendAPIServiceClient;
import com.facebook.AccessToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by shiv on 31/3/16.
 */
public class TrackedFriendListAdapter extends RecyclerView.Adapter<TrackedFriendListAdapter.TrackedFriendListViewHolder> {

    private ArrayList<TrackedFriendsTask> trackedFriendsTaskList;
    private Context context;
    private SharedPreferences sharedPreferences;
    private BackendAPIServiceClient backendAPIServiceClient;
    private static TaskListAdapter.MyClickListener myClickListener;
    private PackageManager packageManager;

    public TrackedFriendListAdapter(Context context, ArrayList<TrackedFriendsTask> trackedFriendsTaskList) {
        this.context = context;
        this.trackedFriendsTaskList = trackedFriendsTaskList;
        backendAPIServiceClient = new BackendAPIServiceClient();
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
        packageManager = context.getPackageManager();
    }

    @Override
    public TrackedFriendListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tracked_friends_row, null);
        TrackedFriendListViewHolder trackedFriendListViewHolder = new TrackedFriendListViewHolder(view);
        return trackedFriendListViewHolder;
    }

    @Override
    public void onBindViewHolder(final TrackedFriendListViewHolder holder, int position) {
        final TrackedFriendsTask trackedFriendsTask = trackedFriendsTaskList.get(position);
        holder.getExpandableViewRelativeLayout().setVisibility(View.GONE);
        holder.getNameTextView().setText(trackedFriendsTask.getFriendName());
        holder.getMessageTextView().setText(trackedFriendsTask.getMessage());
        holder.getEditMessageLinearLayout().setVisibility(View.GONE);
        if (trackedFriendsTask.getMessageSet() == Constants.CODE_MESSAGE_SET) {
            holder.getMessageEditText().setText(trackedFriendsTask.getMessage());
        }
        if (trackedFriendsTask.getTaskType() == Constants.ACTIVITY_BASED_TASK) {
            holder.getTaskTextView().setText(trackedFriendsTask.getActivityName());
        } else {
            holder.getTaskTextView().setText(trackedFriendsTask.getTaskName());
        }
        Picasso.with(context).load(trackedFriendsTask.getFriendImageUrl()).into(holder.getProfileImageView());
        holder.getEditMessageImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = holder.getMessageTextView().getText().toString();
                holder.getMessageLinearLayout().setVisibility(View.GONE);
                holder.getEditMessageLinearLayout().setVisibility(View.VISIBLE);
                holder.getMessageEditText().setText(message);
            }
        });
        holder.getSubmitMessageImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getMessageEditText().getText() != null && holder.getMessageEditText().getText().toString().length() > 0) {
                    sendMessage(trackedFriendsTask.getTrackedTaskId(), holder.getMessageEditText().getText().toString(), holder);
                } else {
                    Toast.makeText(context, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.getCancelEditMessageImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.getMessageLinearLayout().setVisibility(View.VISIBLE);
                holder.getEditMessageLinearLayout().setVisibility(View.GONE);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        TrackedFriendAppRowAdapter trackedFriendAppRowAdapter = new TrackedFriendAppRowAdapter(context, getTrackedFriendAppItemList(trackedFriendsTask));
        holder.getAppRecyclerView().setLayoutManager(linearLayoutManager);
        holder.getAppRecyclerView().setAdapter(trackedFriendAppRowAdapter);

    }

    public void add(TrackedFriendsTask trackedFriendsTask) {
        this.trackedFriendsTaskList.add(trackedFriendsTask);
        notifyDataSetChanged();
    }

    private void sendMessage(String taskId, final String message, final TrackedFriendListViewHolder holder) {
        final String id = sharedPreferences.getString(Constants.USER_ACCESS_TOKEN, "");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put(Constants.JSON_PARAMETER_USER_ID, id);
        parameters.put(Constants.JSON_PARAMETER_FB_TOKEN, AccessToken.getCurrentAccessToken().getToken());
        parameters.put(Constants.JSON_PARAMETER_MESSAGE, message);
        backendAPIServiceClient.getService().updateUserTaskMessage(taskId, parameters, new Callback<UpdateUserMessageResponse>() {
            @Override
            public void success(UpdateUserMessageResponse updateUserMessageResponse, Response response) {
                Log.d(getClass().toString(), "Successfully updated message");
                Toast.makeText(context, "Successfully updated message", Toast.LENGTH_SHORT).show();
                holder.getMessageLinearLayout().setVisibility(View.VISIBLE);
                holder.getEditMessageLinearLayout().setVisibility(View.GONE);
                holder.getMessageTextView().setText(message);
            }

            @Override
            public void failure(RetrofitError error) {
                holder.getMessageLinearLayout().setVisibility(View.VISIBLE);
                holder.getEditMessageLinearLayout().setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (trackedFriendsTaskList == null) {
            return 0;
        } else {
            return trackedFriendsTaskList.size();
        }
    }

    public void onClick(View view, int position) {
        Log.d(getClass().toString(), "OnClick called");
        TrackedFriendListViewHolder holder = new TrackedFriendListViewHolder(view);
        if (holder.getExpandableViewRelativeLayout().getVisibility() == View.VISIBLE) {
            holder.getExpandableViewRelativeLayout().setVisibility(View.GONE);
        } else {
            holder.getExpandableViewRelativeLayout().setVisibility(View.VISIBLE);
        }
    }

    public void setMyClickListener(TaskListAdapter.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void removeAll(){
        int size = trackedFriendsTaskList.size();
        trackedFriendsTaskList.clear();
        notifyDataSetChanged();
        notifyItemRangeRemoved(0, size);
    }

    public static class TrackedFriendListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CircleImageView profileImageView;
        private TextView nameTextView;
        private TextView taskTextView;
        private EditText messageEditText;
        private RelativeLayout expandableViewRelativeLayout;
        private LinearLayout profilePicLinearLayout;
        private TextView messageTextView;
        private ImageView editMessageImageView;
        private ImageView submitMessageImageView;
        private ImageView cancelEditMessageImageView;
        private LinearLayout editMessageLinearLayout;
        private LinearLayout messageLinearLayout;
        private RecyclerView appRecyclerView;


        public TrackedFriendListViewHolder(View view) {
            super(view);
            this.profileImageView = (CircleImageView) view.findViewById(R.id.tracked_friends_row_profile_circular_image_view);
            this.nameTextView = (TextView) view.findViewById(R.id.tracked_friends_name_text_view);
            this.taskTextView = (TextView) view.findViewById(R.id.tracked_friends_task_text_view);
            this.expandableViewRelativeLayout = (RelativeLayout) view.findViewById(R.id.tracked_friends_ExpandArea_relative_layout);
            this.profilePicLinearLayout = (LinearLayout) view.findViewById(R.id.tracked_friends_row_profile_pic_linear_layout);

            this.messageEditText = (EditText) view.findViewById(R.id.tracked_friends_row_message_editText);
            this.messageTextView = (TextView)view.findViewById(R.id.tracked_friends_row_message_text_view);
            this.editMessageImageView = (ImageView)view.findViewById(R.id.tracked_friends_row_edit_message_image_view);
            this.submitMessageImageView = (ImageView)view.findViewById(R.id.tracked_friends_row_submit_image_view);
            this.cancelEditMessageImageView = (ImageView)view.findViewById(R.id.tracked_friends_row_cancel_image_view);
            this.editMessageLinearLayout = (LinearLayout)view.findViewById(R.id.tracked_friends_row_edit_linear_layout);

            this.messageLinearLayout = (LinearLayout)view.findViewById(R.id.tracked_friends_row_message_layout);

            this.appRecyclerView = (RecyclerView)view.findViewById(R.id.tracked_friends_row_app_recycler_view);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }

        public LinearLayout getMessageLinearLayout() {
            return messageLinearLayout;
        }

        public void setMessageLinearLayout(LinearLayout messageLinearLayout) {
            this.messageLinearLayout = messageLinearLayout;
        }

        public TextView getNameTextView() {
            return nameTextView;
        }

        public void setNameTextView(TextView nameTextView) {
            this.nameTextView = nameTextView;
        }

        public TextView getTaskTextView() {
            return taskTextView;
        }

        public void setTaskTextView(TextView taskTextView) {
            this.taskTextView = taskTextView;
        }

        public EditText getMessageEditText() {
            return messageEditText;
        }

        public void setMessageEditText(EditText messageEditText) {
            this.messageEditText = messageEditText;
        }

        public RelativeLayout getExpandableViewRelativeLayout() {
            return expandableViewRelativeLayout;
        }

        public void setExpandableViewRelativeLayout(RelativeLayout expandableViewRelativeLayout) {
            this.expandableViewRelativeLayout = expandableViewRelativeLayout;
        }

        public CircleImageView getProfileImageView() {
            return profileImageView;
        }

        public void setProfileImageView(CircleImageView profileImageView) {
            this.profileImageView = profileImageView;
        }

        public LinearLayout getProfilePicLinearLayout() {
            return profilePicLinearLayout;
        }

        public void setProfilePicLinearLayout(LinearLayout profilePicLinearLayout) {
            this.profilePicLinearLayout = profilePicLinearLayout;
        }

        public TextView getMessageTextView() {
            return messageTextView;
        }

        public void setMessageTextView(TextView messageTextView) {
            this.messageTextView = messageTextView;
        }

        public ImageView getEditMessageImageView() {
            return editMessageImageView;
        }

        public void setEditMessageImageView(ImageView editMessageImageView) {
            this.editMessageImageView = editMessageImageView;
        }

        public ImageView getSubmitMessageImageView() {
            return submitMessageImageView;
        }

        public void setSubmitMessageImageView(ImageView submitMessageImageView) {
            this.submitMessageImageView = submitMessageImageView;
        }

        public ImageView getCancelEditMessageImageView() {
            return cancelEditMessageImageView;
        }

        public void setCancelEditMessageImageView(ImageView cancelEditMessageImageView) {
            this.cancelEditMessageImageView = cancelEditMessageImageView;
        }

        public LinearLayout getEditMessageLinearLayout() {
            return editMessageLinearLayout;
        }

        public void setEditMessageLinearLayout(LinearLayout editMessageLinearLayout) {
            this.editMessageLinearLayout = editMessageLinearLayout;
        }

        public RecyclerView getAppRecyclerView() {
            return appRecyclerView;
        }

        public void setAppRecyclerView(RecyclerView appRecyclerView) {
            this.appRecyclerView = appRecyclerView;
        }
    }

    private ArrayList<TrackedFriendAppItem> getTrackedFriendAppItemList(TrackedFriendsTask trackedFriendsTask){
        if(trackedFriendsTask.getAppNames() == null || trackedFriendsTask.getAppNames().size() == 0){
            return new ArrayList<TrackedFriendAppItem>();
        }
        ArrayList<TrackedFriendAppItem> trackedFriendAppItemList = new ArrayList<>();
        for(int i = 0 ; i < trackedFriendsTask.getApps().size() ; i++){
            TrackedFriendAppItem trackedFriendAppItem = new TrackedFriendAppItem();
            trackedFriendAppItem.setAppName(trackedFriendsTask.getAppNames().get(i));
            Drawable icon;
            try {
                icon = packageManager.getApplicationIcon(trackedFriendsTask.getApps().get(i));
            } catch (PackageManager.NameNotFoundException e) {
                icon = context.getDrawable(R.drawable.ic_launcher);
            }
            trackedFriendAppItem.setAppImage(icon);
            trackedFriendAppItemList.add(trackedFriendAppItem);
        }
        return trackedFriendAppItemList;
    }

}
