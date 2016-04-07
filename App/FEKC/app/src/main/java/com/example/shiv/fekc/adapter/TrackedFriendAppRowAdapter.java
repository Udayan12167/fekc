package com.example.shiv.fekc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.holder.TrackedFriendAppViewHolder;
import com.example.shiv.fekc.item.TrackedFriendAppItem;

import java.util.ArrayList;

/**
 * Created by shiv on 7/4/16.
 */
public class TrackedFriendAppRowAdapter extends RecyclerView.Adapter<TrackedFriendAppViewHolder> {

    private Context context;
    private ArrayList<TrackedFriendAppItem> trackedFriendAppItemList;

    public TrackedFriendAppRowAdapter(Context context, ArrayList<TrackedFriendAppItem> trackedFriendAppItemList){
        this.context = context;
        this.trackedFriendAppItemList = trackedFriendAppItemList;
    }

    @Override
    public TrackedFriendAppViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tracked_friend_app_row, null);
        TrackedFriendAppViewHolder trackedFriendAppViewHolder = new TrackedFriendAppViewHolder(view);
        return trackedFriendAppViewHolder;
    }

    @Override
    public void onBindViewHolder(TrackedFriendAppViewHolder holder, int position) {
        TrackedFriendAppItem trackedFriendAppItem = trackedFriendAppItemList.get(position);
        holder.getTextView().setText(trackedFriendAppItem.getAppName());
        holder.getImageView().setImageDrawable(trackedFriendAppItem.getAppImage());
    }

    @Override
    public int getItemCount() {
        if(trackedFriendAppItemList == null){
            return 0;
        }else{
            return trackedFriendAppItemList.size();
        }
    }
}
