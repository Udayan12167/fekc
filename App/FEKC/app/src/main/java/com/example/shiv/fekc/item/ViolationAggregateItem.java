package com.example.shiv.fekc.item;

import java.sql.Date;

/**
 * Created by shiv on 5/4/16.
 */
public class ViolationAggregateItem {

    private Date date;
    private int violations;
    private int wins;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getViolations() {
        return violations;
    }

    public void setViolations(int violations) {
        this.violations = violations;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void increaseViolations(){
        this.violations++;
    }

    public void increaseWins(){
        this.wins++;
    }

    @Override
    public String toString() {
        return this.date.toString() + " wins : " + this.wins + " violations : " + this.violations;
    }
}
