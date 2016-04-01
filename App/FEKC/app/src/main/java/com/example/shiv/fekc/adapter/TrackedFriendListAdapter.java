package com.example.shiv.fekc.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shiv on 31/3/16.
 */
public class TrackedFriendListAdapter extends RecyclerView.Adapter<TrackedFriendListAdapter.TrackedFriendListAdapterHolder> {

    @Override
    public TrackedFriendListAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TrackedFriendListAdapterHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class TrackedFriendListAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TrackedFriendListAdapterHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
