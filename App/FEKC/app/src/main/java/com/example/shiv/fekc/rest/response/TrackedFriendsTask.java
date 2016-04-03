package com.example.shiv.fekc.rest.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiv on 31/3/16.
 */
public class TrackedFriendsTask {

    private Integer taskID;
    private String taskName;
    private Integer taskType;
    private String endDate;
    private String startTime;
    private String endTime;
    private String duration;
    private String activityName;
    private List<String> apps;
    private List<String> friends;
    private int activityStartFlag;
    private int activityStopFlag;
    private int messageSet;
    private String trackingFriendId;
    private String message;
    private String friendName;
    private String friendImageUrl;
    private String trackedTaskId;

    public String getTrackedTaskId() {
        return trackedTaskId;
    }

    public void setTrackedTaskId(String trackedTaskId) {
        this.trackedTaskId = trackedTaskId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendImageUrl() {
        return friendImageUrl;
    }

    public void setFriendImageUrl(String friendImageUrl) {
        this.friendImageUrl = friendImageUrl;
    }

    public int getMessageSet() {
        return messageSet;
    }

    public void setMessageSet(int messageSet) {
        this.messageSet = messageSet;
    }

    public String getTrackingFriendId() {
        return trackingFriendId;
    }

    public void setTrackingFriendId(String trackingFriendId) {
        this.trackingFriendId = trackingFriendId;
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

    public List<String> getApps() {
        return apps;
    }

    public void setApps(List<String> apps) {
        this.apps = apps;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public int getActivityStartFlag() {
        return activityStartFlag;
    }

    public void setActivityStartFlag(int activityStartFlag) {
        this.activityStartFlag = activityStartFlag;
    }

    public int getActivityStopFlag() {
        return activityStopFlag;
    }

    public void setActivityStopFlag(int activityStopFlag) {
        this.activityStopFlag = activityStopFlag;
    }


}
