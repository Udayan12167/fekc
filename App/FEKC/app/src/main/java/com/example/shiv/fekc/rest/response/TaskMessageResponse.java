package com.example.shiv.fekc.rest.response;

import java.util.ArrayList;

/**
 * Created by shiv on 1/4/16.
 */
public class TaskMessageResponse {

    private ArrayList<TaskMessage> messages;

    public ArrayList<TaskMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<TaskMessage> messages) {
        this.messages = messages;
    }
}
