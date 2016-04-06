package com.example.shiv.fekc.filter;

import android.util.Log;
import android.widget.Filter;

import com.example.shiv.fekc.adapter.AppListAdapter;
import com.example.shiv.fekc.adapter.AppListDurationAdapter;
import com.example.shiv.fekc.item.AppDurationItem;
import com.example.shiv.fekc.item.AppItem;

import java.util.ArrayList;

/**
 * Created by shiv on 6/4/16.
 */
public class AppDurationItemFilter extends Filter {


    private AppListDurationAdapter appListDurationAdapter;
    private ArrayList<AppDurationItem> originalList = new ArrayList<AppDurationItem>();
    private ArrayList<AppDurationItem> filteredList = new ArrayList<AppDurationItem>();

    public AppDurationItemFilter(AppListDurationAdapter appListDurationAdapter, ArrayList<AppDurationItem> originalList, ArrayList<AppDurationItem> filteredList){
        this.appListDurationAdapter = appListDurationAdapter;
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
            for (AppDurationItem appDurationItem : originalList) {
                if (appDurationItem.getAppName().toLowerCase().startsWith(filterPattern)) {
                    Log.d(getClass().toString(), appDurationItem.getAppName());
                    filteredList.add(appDurationItem);
                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        appListDurationAdapter.setFilteredList((ArrayList<AppDurationItem>)results.values);
        appListDurationAdapter.notifyDataSetChanged();
    }
}
