package com.example.shiv.fekc.adapter;

import android.content.Context;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.commons.Functions;
import com.example.shiv.fekc.filter.AppItemFilter;
import com.example.shiv.fekc.holder.AppListViewHolder;
import com.example.shiv.fekc.item.AppItem;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by shiv on 10/3/16.
 */
public class AppListAdapter extends RecyclerView.Adapter<AppListViewHolder> implements Filterable {

    private ArrayList<AppItem> originalList;
    private ArrayList<AppItem> filteredList = new ArrayList<AppItem>();
    private HashSet<String> selectedAppsHashSet = new HashSet<String>();
    private Context context;

    public AppListAdapter(Context context, ArrayList<AppItem> appItemList) {
        this.context = context;
        this.originalList = appItemList;
    }

    public ArrayList<AppItem> getOriginalList() {
        return originalList;
    }

    public void setOriginalList(ArrayList<AppItem> originalList) {
        this.originalList = originalList;
    }

    @Override
    public AppListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.app_list_row, null);
        AppListViewHolder appListViewHolder = new AppListViewHolder(view);
        return appListViewHolder;
    }

    @Override
    public void onBindViewHolder(final AppListViewHolder holder, final int position) {
        final AppItem appItem = filteredList.get(position);
        holder.getAppNameTextView().setText(appItem.getAppName());

        holder.getImageView().setImageDrawable(appItem.getAppIcon());
        holder.getSelectedCheckBox().setChecked(appItem.isSelected());

        if(appItem.isSelected()){
            selectedAppsHashSet.add(appItem.getPackageName());
        }

        holder.getSelectedCheckBox().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appItem.isSelected()) {
                    appItem.setIsSelected(false);
                    holder.getSelectedCheckBox().setChecked(false);
                    selectedAppsHashSet.remove(appItem.getPackageName());
                } else {
                    appItem.setIsSelected(true);
                    holder.getSelectedCheckBox().setChecked(true);
                    selectedAppsHashSet.add(appItem.getPackageName());
                }
                updateItemFilteredList(appItem, position);
                updateItemOriginalList(appItem);
            }
        });

    }


    public HashSet<String> getSelectedApps() {
        return selectedAppsHashSet;
    }

    @Override
    public int getItemCount() {
        if (filteredList != null) {
            return filteredList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Filter getFilter() {
        return new AppItemFilter(this, originalList, filteredList);
    }

    public void add(AppItem appItem) {
        originalList.add((appItem));
        filteredList.add(appItem);
//        notifyDataSetChanged();
    }

    public void remove(AppItem appItem) {
        int position = filteredList.indexOf(appItem);
        originalList.remove(appItem);
        filteredList.remove(appItem);
        notifyDataSetChanged();
        notifyItemRemoved(position);
    }

    public void clearFilteredList() {
        int originalSize = filteredList.size();
        filteredList.clear();
        notifyDataSetChanged();
        notifyItemRangeRemoved(0, originalSize);
    }

    public void setFilteredList(ArrayList<AppItem> appItemList) {
        clearFilteredList();
        filteredList.addAll(appItemList);
        notifyDataSetChanged();
        notifyItemRangeInserted(0, filteredList.size());
    }

    private void updateItemFilteredList(AppItem appItem, int position) {
        filteredList.set(position, appItem);
        notifyDataSetChanged();
        notifyItemChanged(position);
    }

    private void updateItemOriginalList(AppItem appItem) {
        for (int i = 0; i < originalList.size(); i++) {
            if (appItem.getAppName().equals(originalList.get(i).getAppName())) {
                originalList.set(i, appItem);
                break;
            }
        }
    }

}
