package com.example.shiv.fekc.item;

import java.util.ArrayList;

/**
 * Created by Mrinalk on 29-Mar-16.
 */
public class TaskItem {
    private Integer taskID;
    private String taskName;
    private Integer taskType;
    private String endDate;
    private String startTime;
    private String endTime;
    private String duration;
    private String activityName;
    private ArrayList<String> apps;
    private ArrayList<String> friends;
    private Integer activityStartFlag;
    private Integer activityStopFlag;

    public TaskItem() {
        taskID = 0;
        taskName = "";
        taskType = 0;
        endDate = "";
        startTime = "";
        endTime = "";
        duration = "";
        activityName = "";
        apps = new ArrayList<String>();
        friends = new ArrayList<String>();
        activityStartFlag = 0;
        activityStopFlag = 0;

    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public ArrayList<String> getApps() {
        return apps;
    }

    public void setApps(ArrayList<String> apps) {
        this.apps = apps;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public Integer getActivityStartFlag() {
        return activityStartFlag;
    }

    public void setActivityStartFlag(Integer activityStartFlag) {
        this.activityStartFlag = activityStartFlag;
    }

    public Integer getActivityStopFlag() {
        return activityStopFlag;
    }

    public void setActivityStopFlag(Integer activityStopFlag) {
        this.activityStopFlag = activityStopFlag;
    }


}
