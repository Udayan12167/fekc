package com.example.shiv.fekc.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shiv.fekc.R;

/**
 * Created by shiv on 6/4/16.
 */
public class AppListDurationHolder extends AppListViewHolder {

    private TextView appNameTextView;
    private TextView appTimeTextView;
    private ImageView appIconImageView;

    public AppListDurationHolder(View view) {
        super(view);
        this.appNameTextView = (TextView)view.findViewById(R.id.app_list_duration_row_app_name_text_view);
        this.appTimeTextView = (TextView)view.findViewById(R.id.app_list_duration_row_app_time_text_view);
        this.appIconImageView = (ImageView)view.findViewById(R.id.app_list_duration_row_image_view);
    }

    @Override
    public TextView getAppNameTextView() {
        return appNameTextView;
    }

    @Override
    public void setAppNameTextView(TextView appNameTextView) {
        this.appNameTextView = appNameTextView;
    }

    public TextView getAppTimeTextView() {
        return appTimeTextView;
    }

    public void setAppTimeTextView(TextView appDurationTextView) {
        this.appTimeTextView = appDurationTextView;
    }

    public ImageView getAppIconImageView() {
        return appIconImageView;
    }

    public void setAppIconImageView(ImageView appIconImageView) {
        this.appIconImageView = appIconImageView;
    }
}
