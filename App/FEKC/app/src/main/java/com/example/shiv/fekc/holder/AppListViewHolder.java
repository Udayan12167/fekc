package com.example.shiv.fekc.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shiv.fekc.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by shiv on 11/3/16.
 */
public class AppListViewHolder extends RecyclerView.ViewHolder {

    private ImageView imageView;
    private TextView appNameTextView;private CheckBox selectedCheckBox;

    public AppListViewHolder(View view) {
        super(view);
        this.imageView = (ImageView)view.findViewById(R.id.app_list_row_image_view);
        this.appNameTextView = (TextView)view.findViewById(R.id.app_list_row_app_name_text_view);
        this.selectedCheckBox = (CheckBox)view.findViewById(R.id.app_list_row_selected_checkbox);
    }

    public CheckBox getSelectedCheckBox() {
        return this.selectedCheckBox;
    }

    public void setSelectedCheckBox(CheckBox selectedCheckBox) {
        this.selectedCheckBox = selectedCheckBox;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(CircleImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getAppNameTextView() {
        return appNameTextView;
    }

    public void setAppNameTextView(TextView appNameTextView) {
        this.appNameTextView = appNameTextView;
    }
}