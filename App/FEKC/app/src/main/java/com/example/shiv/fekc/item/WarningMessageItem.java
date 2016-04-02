package com.example.shiv.fekc.item;

import com.example.shiv.fekc.rest.response.TaskMessage;

/**
 * Created by shiv on 2/4/16.
 */
public class WarningMessageItem {

    private String name;
    private String imageUrl;
    private String message;
    private String friendId;

    public WarningMessageItem(TaskMessage taskMessage){
        this.message = taskMessage.getMessage();
        this.friendId = taskMessage.getFriendId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }
}

