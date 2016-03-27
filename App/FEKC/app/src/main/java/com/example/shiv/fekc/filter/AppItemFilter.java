package com.example.shiv.fekc.filter;

import android.util.Log;
import android.widget.Filter;

import com.example.shiv.fekc.adapter.AppListAdapter;
import com.example.shiv.fekc.item.AppItem;

import java.util.ArrayList;

/**
 * Created by shiv on 10/3/16.
 */
public class AppItemFilter extends Filter{

    private AppListAdapter appListAdapter;
    private ArrayList<AppItem> originalList = new ArrayList<AppItem>();
    private ArrayList<AppItem> filteredList = new ArrayList<AppItem>();

    public AppItemFilter(AppListAdapter appListAdapter, ArrayList<AppItem> originalList, ArrayList<AppItem> filteredList){
        this.appListAdapter = appListAdapter;
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
            for (AppItem appItem : originalList) {
                if (appItem.getAppName().toLowerCase().startsWith(filterPattern)) {
                    Log.d(getClass().toString(), appItem.getAppName());
                    filteredList.add(appItem);
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
        appListAdapter.setFilteredList((ArrayList<AppItem>)results.values);
        appListAdapter.notifyDataSetChanged();
    }


}
