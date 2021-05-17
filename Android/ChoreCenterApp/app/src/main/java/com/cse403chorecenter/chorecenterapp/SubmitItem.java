package com.cse403chorecenter.chorecenterapp;

import android.widget.Button;

public class SubmitItem {
    private String choreName;
    private int chorePoint;
    private String choreDescription;
    private String choreTime;
    private String choreID;
    private Button submit;

    public SubmitItem(String name, int point, String description, String time, String id) {
        choreName = name;
        chorePoint = point;
        choreDescription = description;
        choreTime = time;
        choreID = id;
    }

    public String getChoreName() {
        return choreName;
    }

    public int getChorePoint() {
        return chorePoint;
    }

    public String getChoreDescription() {
        return choreDescription;
    }

    public String getChoreTime() {
        return choreTime;
    }

    public String getChoreID() {
        return choreID;
    }

    public void setChoreName(String name) {
        this.choreName = name;
    }

    public void setChorePoint(int point) {
        this.chorePoint = point;
    }

    public void setChoreDescription(String description) {
        this.choreDescription = description;
    }

    public void setChoreTime(String time) {
        this.choreTime = time;
    }

    public void setChoreID(String id) {
        this.choreID = id;
    }

    public void changeText(String text) {

    }

}
