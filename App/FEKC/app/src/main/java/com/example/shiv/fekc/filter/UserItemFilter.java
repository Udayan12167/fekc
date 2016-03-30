package com.example.shiv.fekc.filter;

import android.util.Log;
import android.widget.Filter;

import com.example.shiv.fekc.adapter.UserListAdapter;
import com.example.shiv.fekc.item.UserListItem;

import java.util.ArrayList;

/**
 * Created by shiv on 10/3/16.
 */
public class UserItemFilter extends Filter{

    private UserListAdapter userListAdapter;
    private ArrayList<UserListItem> originalList = new ArrayList<UserListItem>();
    private ArrayList<UserListItem> filteredList = new ArrayList<UserListItem>();

    public UserItemFilter(UserListAdapter userListAdapter, ArrayList<UserListItem> originalList, ArrayList<UserListItem> filteredList){
        this.userListAdapter = userListAdapter;
        this.originalList.addAll(originalList);
        this.filteredList.addAll(filteredList);
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();
        Log.d(getClass().toString(), constraint.toString());
        if (constraint == null || constraint.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            String filterPattern = constraint.toString().toLowerCase().trim();
            Log.d(getClass().toString(), "The original list size is : " + Integer.toString(originalList.size()));
            for (UserListItem userItem : originalList) {
                if (userItem.getName().toLowerCase().startsWith(filterPattern)) {
                    filteredList.add(userItem);
                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        Log.d(getClass().toString(), "The filtered list size is : " + Integer.toString(results.count));
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        userListAdapter.setFilteredList((ArrayList<UserListItem>)results.values);
        userListAdapter.notifyDataSetChanged();
    }


}
