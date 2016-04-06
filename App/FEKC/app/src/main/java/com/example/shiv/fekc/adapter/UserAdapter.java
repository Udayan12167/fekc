package com.example.shiv.fekc.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shiv.fekc.R;
import com.example.shiv.fekc.holder.UserViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

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
        //System.out.println("URL: "+url);
        Log.e("URL: ",url);
        try {
            Picasso.with(context).load(url).into(holder.getCircleImageView());
        } catch(IllegalArgumentException e) {
            int max=4;
            int min=1;
            Log.e("In catch for url: ",url);
            Random rand = new Random();
            int randomNum = rand.nextInt((max - min) + 1) + min;
            int imageResource = context.getResources().getIdentifier("drawable/contact"+randomNum,null,context.getPackageName()); //"drawable/contact" + randomNum + ".jpg", null, getPackageName());
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imageResource);
            CircleImageView civ = holder.getCircleImageView(); // new CircleImageView(context);
            civ.setImageBitmap(bitmap);
            //holder.setCircleImageView(civ);
        }
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
