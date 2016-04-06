package com.example.shiv.fekc.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shiv.fekc.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by shiv on 11/3/16.
 */
public class UserListViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView circleImageView;
    private TextView userNameTextView;
    private CheckBox selectedCheckBox;
    private ProgressBar progressBar;

    public UserListViewHolder(View view) {
        super(view);
        this.circleImageView = (CircleImageView)view.findViewById(R.id.user_list_row_image_view);
        this.userNameTextView = (TextView)view.findViewById(R.id.user_list_row_app_name_text_view);
        this.selectedCheckBox = (CheckBox)view.findViewById(R.id.user_list_row_selected_checkbox);
        this.progressBar = (ProgressBar)view.findViewById(R.id.user_list_row_progress_bar);
    }

    public TextView getUserNameTextView() {
        return userNameTextView;
    }

    public void setUserNameTextView(TextView userNameTextView) {
        this.userNameTextView = userNameTextView;
    }

    public CheckBox getSelectedCheckBox() {
        return this.selectedCheckBox;
    }

    public void setSelectedCheckBox(CheckBox selectedCheckBox) {
        this.selectedCheckBox = selectedCheckBox;
    }

    public CircleImageView getCircleImageView() {
        return circleImageView;
    }

    public void setCircleImageView(CircleImageView circleImageView) {
        this.circleImageView = circleImageView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}
