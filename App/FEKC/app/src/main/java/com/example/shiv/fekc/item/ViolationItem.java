package com.example.shiv.fekc.item;


import java.sql.Date;

/**
 * Created by shiv on 5/4/16.
 */
public class ViolationItem {

    private Integer taskID;
    private Integer violationType;
    private Date date;

    public ViolationItem(){
        taskID = 0;
        violationType = 0;
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public Integer getViolationType() {
        return violationType;
    }

    public void setViolationType(Integer violationType) {
        this.violationType = violationType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
