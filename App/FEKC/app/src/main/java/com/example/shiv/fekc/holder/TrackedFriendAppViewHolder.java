package com.example.shiv.fekc.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shiv.fekc.R;


/**
 * Created by shiv on 7/4/16.
 */
public class TrackedFriendAppViewHolder extends RecyclerView.ViewHolder{


    private TextView textView;
    private ImageView imageView;

    public TrackedFriendAppViewHolder(View view) {
        super(view);
        this.textView = (TextView)view.findViewById(R.id.tracked_friend_app_name_text_view);
        this.imageView = (ImageView)view.findViewById(R.id.tracked_friend_app_row_image_view);
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
