package com.example.shiv.fekc.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.shiv.fekc.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by shiv on 4/4/16.
 */
public class UserViewHolder extends RecyclerView.ViewHolder{

    private CircleImageView circleImageView;

    public UserViewHolder(View view) {
        super(view);
        this.circleImageView = (CircleImageView)view.findViewById(R.id.user_row_layout_image_view);
    }

    public CircleImageView getCircleImageView() {
        return circleImageView;
    }

    public void setCircleImageView(CircleImageView circleImageView) {
        this.circleImageView = circleImageView;
    }
}
