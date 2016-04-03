package com.example.shiv.fekc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.filter.UserItemFilter;
import com.example.shiv.fekc.holder.UserListViewHolder;
import com.example.shiv.fekc.item.AppItem;
import com.example.shiv.fekc.item.UserListItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shiv on 11/3/16.
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListViewHolder> implements Filterable {

    private ArrayList<UserListItem> originalList;
    private ArrayList<UserListItem> filteredList = new ArrayList<UserListItem>();
    private Context context;
    private ArrayList<String> selectedUsers = new ArrayList<String>();
    public UserListAdapter(Context context, ArrayList<UserListItem> userItemList){
        this.context = context;
        this.originalList = userItemList;
    }

    public ArrayList<UserListItem> getOriginalList() {
        return originalList;
    }

    public void setOriginalList(ArrayList<UserListItem> originalList) {
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
        final UserListItem userItem = filteredList.get(position);
        holder.getUserNameTextView().setText(userItem.getName());
        holder.getSelectedCheckBox().setChecked(userItem.isSelected());
        holder.getFavoriteCheckBox().setChecked(userItem.isFavorite());
        Picasso.with(context).load(userItem.getImageUrl()).into(holder.getCircleImageView());
        holder.getProgressBar().setVisibility(View.GONE);

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
                    selectedUsers.remove(userItem.getId());
                }else{
                    userItem.setIsSelected(true);
                    holder.getSelectedCheckBox().setChecked(true);
                    selectedUsers.add(userItem.getId());
                }
                updateItemFilteredList(userItem, position);
                updateItemOriginalList(userItem);
            }
        });


    }

    public ArrayList<String> getSelectedUsers(){
        return selectedUsers;
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

    public void add(UserListItem userItem){
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

    public void setFilteredList(ArrayList<UserListItem> userItemList){
        clearFilteredList();
        filteredList.addAll(userItemList);
        notifyDataSetChanged();
        notifyItemRangeInserted(0, filteredList.size());
    }

    private void updateItemFilteredList(UserListItem userItem, int position){
        filteredList.set(position, userItem);
        notifyDataSetChanged();
        notifyItemChanged(position);
    }

    private void updateItemOriginalList(UserListItem userItem){
        for(int i = 0 ; i < originalList.size() ; i++){
            if(userItem.getName().equals(originalList.get(i).getName())){
                originalList.set(i, userItem);
                break;
            }
        }
    }
}
