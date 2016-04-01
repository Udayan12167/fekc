package com.example.shiv.fekc.rest.response;

import com.example.shiv.fekc.item.TaskItem;

import java.util.ArrayList;

/**
 * Created by shiv on 31/3/16.
 */
public class TrackedFriendsTaskResponse {

    private ArrayList<TrackedFriendsTask> tasks;

    public ArrayList<TrackedFriendsTask> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<TrackedFriendsTask> tasks) {
        this.tasks = tasks;
    }
}
