package com.tasktk.app.entity;


import java.util.Date;

public class Activity extends BaseEntity {
    //user_id
    //task_id

    private Type type;
    private Date timeStamp;
    private String details;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public enum Type{
        created,
        updated,
        completed
    }
}
