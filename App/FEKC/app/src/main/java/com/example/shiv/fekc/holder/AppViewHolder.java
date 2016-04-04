package com.example.shiv.fekc.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.shiv.fekc.R;

/**
 * Created by shiv on 4/4/16.
 */
public class AppViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;

    public AppViewHolder(View view) {
        super(view);
        this.imageView = (ImageView)view.findViewById(R.id.app_row_layout_image_view);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
