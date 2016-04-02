package com.example.shiv.fekc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.holder.WarningMessageViewHolder;
import com.example.shiv.fekc.item.WarningMessageItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shiv on 2/4/16.
 */
public class WarningMessageAdapter extends RecyclerView.Adapter<WarningMessageViewHolder>{

    private Context context;
    private ArrayList<WarningMessageItem> warningMessageList;

    public WarningMessageAdapter(Context context , ArrayList<WarningMessageItem> warningMessageList){
        this.context = context;
        this.warningMessageList = warningMessageList;
    }

    @Override
    public WarningMessageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.warning_message_row, null);
        WarningMessageViewHolder warningMessageViewHolder = new WarningMessageViewHolder(view);
        return warningMessageViewHolder;
    }

    @Override
    public void onBindViewHolder(WarningMessageViewHolder holder, int position) {
        WarningMessageItem warningMessageItem = warningMessageList.get(position);
        Picasso.with(context).load(warningMessageItem.getImageUrl()).into(holder.getImageView());
        holder.getNameTextView().setText(warningMessageItem.getName());
        holder.getMessageTextView().setText(warningMessageItem.getMessage());
    }

    @Override
    public int getItemCount() {
        if(warningMessageList == null){
            return 0;
        }else{
            return warningMessageList.size();
        }
    }

    public void add(WarningMessageItem warningMessageItem){
        warningMessageList.add(warningMessageItem);
        notifyDataSetChanged();
    }
}
