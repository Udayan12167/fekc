package com.example.shiv.fekc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.filter.AppDurationItemFilter;
import com.example.shiv.fekc.holder.AppListDurationHolder;
import com.example.shiv.fekc.item.AppDurationItem;

import java.util.ArrayList;

/**
 * Created by shiv on 6/4/16.
 */
public class AppListDurationAdapter extends RecyclerView.Adapter<AppListDurationHolder> implements Filterable {

    private ArrayList<AppDurationItem> originalList;
    private ArrayList<AppDurationItem> filteredList = new ArrayList<AppDurationItem>();

    private Context context;

    public  AppListDurationAdapter(Context context, ArrayList<AppDurationItem> appDurationItemList){
        this.context = context;
        this.originalList = appDurationItemList;
    }

    @Override
    public AppListDurationHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.app_list_duration_row, null);
        AppListDurationHolder appListDurationHolder = new AppListDurationHolder(view);
        return appListDurationHolder;
    }

    @Override
    public void onBindViewHolder(AppListDurationHolder holder, int position) {
        AppDurationItem appDurationItem = filteredList.get(position);
        holder.getAppNameTextView().setText(appDurationItem.getAppName());
        holder.getAppTimeTextView().setText(appDurationItem.getAppTime());
        holder.getAppIconImageView().setImageDrawable(appDurationItem.getAppIcon());
    }

    @Override
    public int getItemCount() {
        if(filteredList != null){
            return filteredList.size();
        }else{
            return 0;
        }
    }

    public void add(AppDurationItem appDurationItem){
        originalList.add((appDurationItem));
        filteredList.add(appDurationItem);
//        notifyDataSetChanged();
    }

    public void remove(AppDurationItem appDurationItem){
        int position = filteredList.indexOf(appDurationItem);
        originalList.remove(appDurationItem);
        filteredList.remove(appDurationItem);
        notifyDataSetChanged();
        notifyItemRemoved(position);
    }

    public void clearFilteredList(){
        int originalSize = filteredList.size();
        filteredList.clear();
        notifyDataSetChanged();
        notifyItemRangeRemoved(0, originalSize);
    }

    public void setFilteredList(ArrayList<AppDurationItem> appDurationItemList){
        clearFilteredList();
        filteredList.addAll(appDurationItemList);
        notifyDataSetChanged();
        notifyItemRangeInserted(0, filteredList.size());
    }

    private void updateItemFilteredList(AppDurationItem appDurationItem, int position){
        filteredList.set(position, appDurationItem);
        notifyDataSetChanged();
        notifyItemChanged(position);
    }

    private void updateItemOriginalList(AppDurationItem appDurationItem){
        for(int i = 0 ; i < originalList.size() ; i++){
            if(appDurationItem.getAppName().equals(originalList.get(i).getAppName())){
                originalList.set(i, appDurationItem);
                break;
            }
        }
    }

    @Override
    public Filter getFilter() {
        return new AppDurationItemFilter(this, originalList, filteredList);
    }
}
