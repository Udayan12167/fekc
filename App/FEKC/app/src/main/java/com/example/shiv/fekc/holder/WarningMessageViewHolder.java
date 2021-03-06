package com.example.shiv.fekc.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shiv.fekc.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by shiv on 2/4/16.
 */
public class WarningMessageViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView imageView;
    private TextView nameTextView;
    private TextView messageTextView;

    public WarningMessageViewHolder(View view) {
        super(view);
        this.imageView = (CircleImageView)view.findViewById(R.id.warning_message_row_image_view);
        this.nameTextView = (TextView)view.findViewById(R.id.warning_message_row_name_text_view);
        this.messageTextView = (TextView)view.findViewById(R.id.warning_message_row_message_text_view);
    }

    public CircleImageView getImageView() {
        return imageView;
    }

    public void setImageView(CircleImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getNameTextView() {
        return nameTextView;
    }

    public void setNameTextView(TextView nameTextView) {
        this.nameTextView = nameTextView;
    }

    public TextView getMessageTextView() {
        return messageTextView;
    }

    public void setMessageTextView(TextView messageTextView) {
        this.messageTextView = messageTextView;
    }
}
