package com.example.shiv.fekc.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.holder.AppViewHolder;

import java.util.ArrayList;

/**
 * Created by shiv on 4/4/16.
 */
public class AppAdapter extends RecyclerView.Adapter<AppViewHolder> {

    private ArrayList<String> list;
    private Context context;

    public AppAdapter(Context context, ArrayList<String> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.app_row_layout, null);
        AppViewHolder appViewHolder = new AppViewHolder(view);
        return appViewHolder;
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        String icon = list.get(position);
        try {
            holder.getImageView().setImageDrawable(context.getPackageManager().getApplicationIcon(icon));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(list == null){
            return 0;
        }else{
            return list.size();
        }
    }
}
