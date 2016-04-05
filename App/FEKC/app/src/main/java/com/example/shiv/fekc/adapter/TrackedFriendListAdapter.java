package com.example.shiv.fekc.adapter;

import android.content.Context;
import android.content.SharedPreferences;
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

    public TrackedFriendListAdapter(Context context, ArrayList<TrackedFriendsTask> trackedFriendsTaskList) {
        this.context = context;
        this.trackedFriendsTaskList = trackedFriendsTaskList;
        backendAPIServiceClient = new BackendAPIServiceClient();
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE);
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
        if (trackedFriendsTask.getMessageSet() == Constants.CODE_MESSAGE_SET) {
            holder.getMessageEditText().setText(trackedFriendsTask.getMessage());
        }
        if (trackedFriendsTask.getTaskType() == Constants.ACTIVITY_BASED_TASK) {
            holder.getTaskTextView().setText(trackedFriendsTask.getActivityName());
        } else {
            holder.getTaskTextView().setText(trackedFriendsTask.getTaskName());
        }
        Picasso.with(context).load(trackedFriendsTask.getFriendImageUrl()).into(holder.getProfileImageView());
        holder.getSendMessageImageButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.getMessageEditText().getText() != null && holder.getMessageEditText().getText().toString().length() > 0) {
                    sendMessage(trackedFriendsTask.getTrackedTaskId(), holder.getMessageEditText().getText().toString());
                } else {
                    Toast.makeText(context, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void add(TrackedFriendsTask trackedFriendsTask) {
        this.trackedFriendsTaskList.add(trackedFriendsTask);
        notifyDataSetChanged();
    }

    private void sendMessage(String taskId, String message) {
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
            }

            @Override
            public void failure(RetrofitError error) {

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
        private ImageView sendMessageImageButton;
        private RelativeLayout expandableViewRelativeLayout;
        private LinearLayout profilePicLinearLayout;


        public TrackedFriendListViewHolder(View view) {
            super(view);
            this.profileImageView = (CircleImageView) view.findViewById(R.id.tracked_friends_row_profile_circular_image_view);
            this.nameTextView = (TextView) view.findViewById(R.id.tracked_friends_name_text_view);
            this.taskTextView = (TextView) view.findViewById(R.id.tracked_friends_task_text_view);
            this.messageEditText = (EditText) view.findViewById(R.id.tracked_friends_row_message_editText);
            this.sendMessageImageButton = (ImageView) view.findViewById(R.id.tracked_friends_row_update_message_button);
            this.expandableViewRelativeLayout = (RelativeLayout) view.findViewById(R.id.tracked_friends_ExpandArea_relative_layout);
            this.profilePicLinearLayout = (LinearLayout) view.findViewById(R.id.tracked_friends_row_profile_pic_linear_layout);
            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
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

        public ImageView getSendMessageImageButton() {
            return sendMessageImageButton;
        }

        public void setSendMessageImageButton(ImageButton sendMessageImageButton) {
            this.sendMessageImageButton = sendMessageImageButton;
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
    }

}
