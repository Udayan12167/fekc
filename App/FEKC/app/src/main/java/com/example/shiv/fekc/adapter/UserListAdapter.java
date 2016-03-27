package com.example.shiv.fekc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.filter.AppItemFilter;
import com.example.shiv.fekc.filter.UserItemFilter;
import com.example.shiv.fekc.holder.UserListViewHolder;
import com.example.shiv.fekc.item.AppItem;
import com.example.shiv.fekc.item.UserItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shiv on 11/3/16.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListViewHolder> implements Filterable {

    private ArrayList<UserItem> originalList;
    private ArrayList<UserItem> filteredList = new ArrayList<UserItem>();
    private Context context;

    public UserListAdapter(Context context, ArrayList<UserItem> userItemList){
        this.context = context;
        this.originalList = userItemList;
    }

    public ArrayList<UserItem> getOriginalList() {
        return originalList;
    }

    public void setOriginalList(ArrayList<UserItem> originalList) {
        this.originalList = originalList;
    }

    @Override
    public UserListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_list_row, null);
        UserListViewHolder userListViewHolder = new UserListViewHolder(view);
        return userListViewHolder;
    }

    @Override
    public void onBindViewHolder(final UserListViewHolder holder, final int position) {
        final UserItem userItem = filteredList.get(position);
        holder.getUserNameTextView().setText(userItem.getName());
        holder.getSelectedCheckBox().setChecked(userItem.isSelected());
        holder.getFavoriteCheckBox().setChecked(userItem.isFavorite());
        Picasso.with(context).load(userItem.getImageUrl()).into(holder.getCircleImageView());

        holder.getFavoriteCheckBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userItem.isFavorite()){
                    userItem.setIsFavorite(false);
                    holder.getFavoriteCheckBox().setChecked(false);
                }else{
                    userItem.setIsFavorite(true);
                    holder.getFavoriteCheckBox().setChecked(true);
                }
                updateItemFilteredList(userItem, position);
                updateItemOriginalList(userItem);
            }
        });

        holder.getSelectedCheckBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userItem.isSelected()){
                    userItem.setIsSelected(false);
                    holder.getSelectedCheckBox().setChecked(false);
                }else{
                    userItem.setIsSelected(true);
                    holder.getSelectedCheckBox().setChecked(true);
                }
                updateItemFilteredList(userItem, position);
                updateItemOriginalList(userItem);
            }
        });


    }

    @Override
    public int getItemCount() {
        if(filteredList != null){
            return filteredList.size();
        }else{
            return 0;
        }
    }

    @Override
    public Filter getFilter() {
        return new UserItemFilter(this, originalList, filteredList);
    }

    public void add(UserItem userItem){
        originalList.add((userItem));
        filteredList.add(userItem);
        notifyDataSetChanged();
    }

    public void remove(AppItem appItem){
        int position = filteredList.indexOf(appItem);
        originalList.remove(appItem);
        filteredList.remove(appItem);
        notifyDataSetChanged();
        notifyItemRemoved(position);
    }

    public void clearFilteredList(){
        int originalSize = filteredList.size();
        filteredList.clear();
        notifyDataSetChanged();
        notifyItemRangeRemoved(0, originalSize);
    }

    public void setFilteredList(ArrayList<UserItem> userItemList){
        clearFilteredList();
        filteredList.addAll(userItemList);
        notifyDataSetChanged();
        notifyItemRangeInserted(0, filteredList.size());
    }

    private void updateItemFilteredList(UserItem userItem, int position){
        filteredList.set(position, userItem);
        notifyDataSetChanged();
        notifyItemChanged(position);
    }

    private void updateItemOriginalList(UserItem userItem){
        for(int i = 0 ; i < originalList.size() ; i++){
            if(userItem.getName().equals(originalList.get(i).getName())){
                originalList.set(i, userItem);
                break;
            }
        }
    }
}
