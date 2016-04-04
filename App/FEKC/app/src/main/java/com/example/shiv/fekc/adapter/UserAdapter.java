package com.example.shiv.fekc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.holder.UserViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shiv on 4/4/16.
 */
public class UserAdapter extends RecyclerView.Adapter<UserViewHolder>{

    private ArrayList<String> list;
    private Context context;

    public UserAdapter(Context context, ArrayList<String> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row_layout, null);
        UserViewHolder userViewHolder = new UserViewHolder(view);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        String url = list.get(position);
        Picasso.with(context).load(url).into(holder.getCircleImageView());
    }

    @Override
    public int getItemCount() {
        if(list == null){
            return 0;
        }else {
            return list.size();
        }
    }

    public void add(String url){
        list.add(url);
        notifyDataSetChanged();
    }
}
